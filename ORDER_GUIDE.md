# 🛍️ Guide des Commandes

Le système de commandes permet aux utilisateurs de créer des commandes à partir de leur panier avec les adresses de livraison et de facturation.

## Endpoints des Commandes

### 1. **Créer une commande**

**Endpoint:** `POST /api/orders`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:**
```json
{
  "shippingAddressId": 1,
  "billingAddressId": 2,
  "paymentMethod": "CREDIT_CARD",
  "notes": "Leave at door",
  "useCartItems": true
}
```

**Réponse (201):**
```json
{
  "id": 1,
  "orderNumber": "ORD-1713779400000-A1B2C3D4",
  "status": "PENDING",
  "items": [
    {
      "id": 1,
      "productId": 5,
      "productName": "RTX 4090",
      "productImage": "https://...",
      "quantity": 2,
      "unitPrice": 1599.99,
      "subtotal": 3199.98
    }
  ],
  "shippingAddress": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA"
  },
  "billingAddress": {
    "id": 2,
    "firstName": "John",
    "lastName": "Doe",
    "street": "456 Oak Avenue",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "country": "USA"
  },
  "subtotal": 3199.98,
  "shippingFee": 5.00,
  "taxAmount": 256.00,
  "totalAmount": 3460.98,
  "paymentMethod": "CREDIT_CARD",
  "trackingNumber": null,
  "notes": "Leave at door",
  "createdAt": "2024-04-23T10:30:00",
  "shippedAt": null,
  "deliveredAt": null
}
```

---

### 2. **Récupérer toutes les commandes de l'utilisateur**

**Endpoint:** `GET /api/orders`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
[
  {
    "id": 1,
    "orderNumber": "ORD-1713779400000-A1B2C3D4",
    "status": "PENDING",
    "items": [...],
    "shippingAddress": {...},
    "billingAddress": {...},
    "totalAmount": 3460.98,
    ...
  }
]
```

---

### 3. **Récupérer une commande spécifique**

**Endpoint:** `GET /api/orders/{orderId}`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):** Commande détaillée

---

### 4. **Mettre à jour le statut d'une commande**

**Endpoint:** `PUT /api/orders/{orderId}/status`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:**
```json
{
  "status": "SHIPPED",
  "trackingNumber": "FDX123456789"
}
```

**Statuts possibles:** `PENDING`, `PROCESSING`, `SHIPPED`, `DELIVERED`, `CANCELLED`

**Réponse (200):** Commande mise à jour

---

### 5. **Annuler une commande**

**Endpoint:** `DELETE /api/orders/{orderId}`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):** Commande annulée

---

### 6. **Récupérer les commandes par statut (Admin)**

**Endpoint:** `GET /api/orders/status/{status}`

**Headers:**
```
Authorization: Bearer <token>
```

**Exemple:** `GET /api/orders/status/SHIPPED`

**Réponse (200):** Liste des commandes avec ce statut

---

## 🔄 Flux de Création de Commande

```
1. Client a un panier rempli
   ├─ CartItem 1: RTX 4090 × 2
   └─ CartItem 2: Ryzen 9 × 1

2. Client sélectionne les adresses
   ├─ shippingAddressId: 1 (123 Main St, NY)
   └─ billingAddressId: 2 (456 Oak Ave, LA)

3. Client crée la commande
   └─ POST /api/orders
      ├─ Panier converti en OrderItems ✅
      ├─ Stock réduit (-2 GPU, -1 CPU) ✅
      ├─ Panier vidé ✅
      ├─ Frais de port calculés ✅
      ├─ Taxe calculée ✅
      └─ Total calculé ✅

4. Réponse: Order avec PENDING status
```

---

## 💡 Calculs Automatiques

### **Frais de Port (shippingFee)**
```
Si USA:
  - CA ou NY: $5.00
  - Autre état: $10.00
Sinon (International): $25.00
```

### **Taxe (taxAmount)**
```
Taxe = Subtotal × 8%
```

### **Total (totalAmount)**
```
Total = Subtotal + Shipping Fee + Tax
```

---

## 🔐 Sécurité

- ✅ Authentification JWT requise
- ✅ Les utilisateurs ne peuvent voir que leurs commandes
- ✅ Vérification du stock avant création
- ✅ Validation des adresses
- ✅ Restauration du stock en cas d'annulation

---

## 📝 Exemples cURL

### Créer une commande

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "shippingAddressId": 1,
    "billingAddressId": 2,
    "paymentMethod": "CREDIT_CARD",
    "notes": "Leave at door"
  }'
```

### Voir toutes les commandes

```bash
curl -X GET http://localhost:8080/api/orders \
  -H "Authorization: Bearer <token>"
```

### Voir une commande

```bash
curl -X GET http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <token>"
```

### Mettre à jour le statut

```bash
curl -X PUT http://localhost:8080/api/orders/1/status \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "status": "SHIPPED",
    "trackingNumber": "FDX123456789"
  }'
```

### Annuler une commande

```bash
curl -X DELETE http://localhost:8080/api/orders/1 \
  -H "Authorization: Bearer <token>"
```

---

## 🚀 Statuts de Commande

| Statut | Description | Accessible |
|--------|-------------|-----------|
| **PENDING** | En attente de paiement | ✅ |
| **PROCESSING** | Préparation en cours | ✅ |
| **SHIPPED** | Envoyé (+ numéro de suivi) | ✅ |
| **DELIVERED** | Livré | ✅ |
| **CANCELLED** | Annulé (stock restauré) | ✅ |

---

## ⚙️ Fonctionnalités

✅ **Création depuis le panier** - Conversion automatique  
✅ **Gestion du stock** - Réduction et restauration  
✅ **Calculs automatiques** - Frais, taxe, total  
✅ **Suivi en temps réel** - Statuts et numéro de suivi  
✅ **Annulation** - Avec restauration du stock  
✅ **Adresses multiples** - Livraison et facturation séparées  

---

## 🎯 Prochaines Étapes

1. ✅ **Authentification** - FAIT
2. ✅ **Panier** - FAIT
3. ✅ **Adresses** - FAIT
4. ✅ **Commandes** - FAIT ✓
5. ⏭️ **Paiement (Stripe/PayPal)** - À venir
6. ⏭️ **Recherche/Filtres** - À venir

