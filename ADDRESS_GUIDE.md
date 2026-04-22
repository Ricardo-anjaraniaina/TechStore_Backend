# 🏠 Guide des Adresses de Livraison

Le système d'adresses permet aux utilisateurs authentifiés de gérer plusieurs adresses de livraison et de facturation.

## Endpoints des Adresses

### 1. **Créer une nouvelle adresse**

**Endpoint:** `POST /api/addresses`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "street": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA",
  "phoneNumber": "+1234567890",
  "isDefault": true,
  "addressType": "SHIPPING",
  "instructions": "Leave at door"
}
```

**Réponse (201):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "street": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA",
  "phoneNumber": "+1234567890",
  "isDefault": true,
  "addressType": "SHIPPING",
  "instructions": "Leave at door",
  "createdAt": "2024-04-22T10:30:00",
  "updatedAt": "2024-04-22T10:30:00"
}
```

---

### 2. **Récupérer toutes les adresses**

**Endpoint:** `GET /api/addresses`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "phoneNumber": "+1234567890",
    "isDefault": true,
    "addressType": "SHIPPING",
    "instructions": "Leave at door",
    "createdAt": "2024-04-22T10:30:00",
    "updatedAt": "2024-04-22T10:30:00"
  }
]
```

---

### 3. **Récupérer une adresse spécifique**

**Endpoint:** `GET /api/addresses/{addressId}`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "street": "123 Main Street",
  "city": "New York",
  "state": "NY",
  "zipCode": "10001",
  "country": "USA",
  "phoneNumber": "+1234567890",
  "isDefault": true,
  "addressType": "SHIPPING",
  "instructions": "Leave at door",
  "createdAt": "2024-04-22T10:30:00",
  "updatedAt": "2024-04-22T10:30:00"
}
```

---

### 4. **Mettre à jour une adresse**

**Endpoint:** `PUT /api/addresses/{addressId}`

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:** (même format que la création)

**Réponse (200):** Adresse mise à jour

---

### 5. **Supprimer une adresse**

**Endpoint:** `DELETE /api/addresses/{addressId}`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):**
```json
"Address deleted successfully"
```

---

### 6. **Obtenir l'adresse par défaut**

**Endpoint:** `GET /api/addresses/default`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):** Adresse par défaut

---

### 7. **Définir une adresse comme adresse par défaut**

**Endpoint:** `PUT /api/addresses/{addressId}/set-default`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):** Adresse définie comme par défaut

---

### 8. **Récupérer les adresses par type**

**Endpoint:** `GET /api/addresses/type/{addressType}`

Types possibles: `SHIPPING`, `BILLING`

**Headers:**
```
Authorization: Bearer <token>
```

**Réponse (200):** Liste des adresses du type spécifié

---

## 🔐 Sécurité

- ✅ Tous les endpoints nécessitent une authentification JWT
- ✅ Les utilisateurs ne peuvent voir/modifier que leurs propres adresses
- ✅ Une seule adresse par défaut par utilisateur
- ✅ Validation des données (zip code, etc.)

---

## 📝 Exemples cURL

### Créer une adresse

```bash
curl -X POST http://localhost:8080/api/addresses \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Doe",
    "street": "123 Main Street",
    "city": "New York",
    "state": "NY",
    "zipCode": "10001",
    "country": "USA",
    "phoneNumber": "+1234567890",
    "isDefault": true,
    "addressType": "SHIPPING"
  }'
```

### Voir toutes les adresses

```bash
curl -X GET http://localhost:8080/api/addresses \
  -H "Authorization: Bearer <token>"
```

### Récupérer une adresse

```bash
curl -X GET http://localhost:8080/api/addresses/1 \
  -H "Authorization: Bearer <token>"
```

### Mettre à jour une adresse

```bash
curl -X PUT http://localhost:8080/api/addresses/1 \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Doe",
    "street": "456 Oak Avenue",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "country": "USA"
  }'
```

### Définir l'adresse par défaut

```bash
curl -X PUT http://localhost:8080/api/addresses/1/set-default \
  -H "Authorization: Bearer <token>"
```

### Supprimer une adresse

```bash
curl -X DELETE http://localhost:8080/api/addresses/1 \
  -H "Authorization: Bearer <token>"
```

### Voir les adresses de livraison

```bash
curl -X GET http://localhost:8080/api/addresses/type/SHIPPING \
  -H "Authorization: Bearer <token>"
```

---

## 💡 Fonctionnalités

✅ **CRUD complet** - Créer, lire, mettre à jour, supprimer  
✅ **Adresse par défaut** - Une adresse marquée comme défaut  
✅ **Types d'adresses** - SHIPPING, BILLING, etc.  
✅ **Instructions** - Notes de livraison (leave at door, etc.)  
✅ **Validation** - Format de code postal validé  
✅ **Sécurité** - Isolation des données par utilisateur  

---

## 📊 Champs Disponibles

| Champ | Type | Obligatoire | Description |
|-------|------|-------------|-------------|
| firstName | String | ✅ | Prénom |
| lastName | String | ✅ | Nom |
| street | String | ✅ | Rue |
| city | String | ✅ | Ville |
| state | String | ✅ | État/Province |
| zipCode | String | ✅ | Code postal (format: 12345 ou 12345-6789) |
| country | String | ✅ | Pays |
| phoneNumber | String | ❌ | Numéro de téléphone |
| isDefault | Boolean | ❌ | Adresse par défaut (défaut: false) |
| addressType | String | ❌ | Type (SHIPPING, BILLING) |
| instructions | String | ❌ | Instructions de livraison |

