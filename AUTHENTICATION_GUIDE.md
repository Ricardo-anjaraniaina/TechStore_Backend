# Guide d'Authentification JWT

Cette application utilise JWT (JSON Web Token) pour l'authentification.

## Endpoints d'Authentification

### 1. **Inscription (Register)**

**Endpoint:** `POST /api/auth/register`

**Body:**
```json
{
  "username": "john_doe",
  "email": "john@example.com",
  "password": "SecurePassword123",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Réponse (201):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

---

### 2. **Connexion (Login)**

**Endpoint:** `POST /api/auth/login`

**Body:**
```json
{
  "email": "john@example.com",
  "password": "SecurePassword123"
}
```

**Réponse (200):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "role": "USER"
}
```

---

### 3. **Récupérer le profil utilisateur**

**Endpoint:** `GET /api/users/me`

**Headers:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Réponse (200):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "isActive": true
}
```

---

### 4. **Récupérer un utilisateur par ID**

**Endpoint:** `GET /api/users/{userId}`

**Réponse (200):**
```json
{
  "id": 1,
  "username": "john_doe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "isActive": true
}
```

---

## Utilisation du Token

Pour accéder aux endpoints protégés, incluez le token JWT dans l'en-tête `Authorization`:

```
Authorization: Bearer <token>
```

### Exemple avec cURL:
```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### Exemple avec JavaScript/Fetch:
```javascript
const response = await fetch('http://localhost:8080/api/users/me', {
  method: 'GET',
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});
```

---

## Endpoints Publics (sans authentification requise)

- `GET /api/products` - Liste tous les produits
- `GET /api/products/{id}` - Récupère un produit
- `GET /api/categories` - Liste toutes les catégories
- `POST /api/auth/register` - Enregistrement
- `POST /api/auth/login` - Connexion

## Endpoints Protégés (authentification requise)

- `GET /api/users/me` - Profil utilisateur actuel
- `GET /api/users/{userId}` - Profil d'un utilisateur
- `GET /api/orders` - Liste les commandes
- `POST /api/orders` - Créer une commande
- `GET /api/orders/{orderId}` - Détails d'une commande

---

## Configuration de Sécurité

- **Algorithme:** HS256
- **Durée de validité du token:** 24 heures
- **Mot de passe encodé:** BCrypt

> ⚠️ **Important:** Changez la clé secrète JWT en production dans `application.properties`

