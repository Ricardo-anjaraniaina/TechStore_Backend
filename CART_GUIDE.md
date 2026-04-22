# Guide du Panier (Shopping Cart)

Le système de panier permet aux utilisateurs authentifiés de gérer leurs articles en attente d'achat.

## Endpoints du Panier

### 1. **Obtenir le panier complet**

**Endpoint:** `GET /api/cart`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
{
  "userId": 1,
  "items": [
    {
      "id": 1,
      "productId": 5,
      "productName": "RTX 4090",
      "productImage": "https://...",
      "quantity": 2,
      "price": 1599.99,
      "subtotal": 3199.98
    }
  ],
  "totalItems": 1,
  "totalPrice": 3199.98
}
```

---

### 2. **Ajouter un article au panier**

**Endpoint:** `POST /api/cart/items`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:**
```json
{
  "productId": 5,
  "quantity": 2
}
```

**Réponse (201):**
```json
{
  "id": 1,
  "productId": 5,
  "productName": "RTX 4090",
  "productImage": "https://...",
  "quantity": 2,
  "price": 1599.99,
  "subtotal": 3199.98
}
```

**Erreurs possibles:**
- `400 Bad Request` - Stock insuffisant
- `404 Not Found` - Produit inexistant

---

### 3. **Mettre à jour la quantité d'un article**

**Endpoint:** `PUT /api/cart/items/{cartItemId}?quantity=3`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
{
  "id": 1,
  "productId": 5,
  "productName": "RTX 4090",
  "productImage": "https://...",
  "quantity": 3,
  "price": 1599.99,
  "subtotal": 4799.97
}
```

---

### 4. **Supprimer un article du panier**

**Endpoint:** `DELETE /api/cart/items/{cartItemId}`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
"Item removed from cart"
```

---

### 5. **Vider le panier complet**

**Endpoint:** `DELETE /api/cart`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
"Cart cleared"
```

---

### 6. **Obtenir le nombre d'articles dans le panier**

**Endpoint:** `GET /api/cart/count`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
3
```

---

## 🔐 Sécurité

- ✅ Tous les endpoints du panier nécessitent une authentification JWT
- ✅ Les utilisateurs ne peuvent voir/modifier que leur propre panier
- ✅ Vérification du stock avant ajout au panier
- ✅ Les prix sont sauvegardés lors de l'ajout (anti-modification)

---

## 📝 Exemples cURL

### Ajouter un article

```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 5,
    "quantity": 2
  }'
```

### Voir le panier

```bash
curl -X GET http://localhost:8080/api/cart \
  -H "Authorization: Bearer <token>"
```

### Mettre à jour la quantité

```bash
curl -X PUT "http://localhost:8080/api/cart/items/1?quantity=5" \
  -H "Authorization: Bearer <token>"
```

### Supprimer un article

```bash
curl -X DELETE http://localhost:8080/api/cart/items/1 \
  -H "Authorization: Bearer <token>"
```

### Vider le panier

```bash
curl -X DELETE http://localhost:8080/api/cart \
  -H "Authorization: Bearer <token>"
```

---

## 💡 Fonctionnalités

✅ **Ajouter des articles** - Avec vérification du stock
✅ **Modifier les quantités** - De chaque article
✅ **Supprimer des articles** - Individuellement ou en masse
✅ **Calcul automatique** - Sous-total et total
✅ **Persistance** - Sauvegardée en base de données
✅ **Sécurité** - Liée à l'utilisateur connecté

