# 🏠 Implémentation des Adresses de Livraison

## ✅ Implémentation Réalisée

### **Fichiers Créés**

#### **Entité (Entity Layer)**
- `Address.java` - Entité pour les adresses
  - Lié à User (Many-to-One)
  - Champs: firstName, lastName, street, city, state, zipCode, country, phoneNumber
  - Types d'adresses: SHIPPING, BILLING
  - Une adresse par défaut par utilisateur
  - Instructions de livraison optionnelles

#### **DTOs**
- `AddressRequest.java` - Requête de création/mise à jour
  - Validation des champs obligatoires
  - Validation du format du code postal (regex)
- `AddressResponse.java` - Réponse avec tous les détails
  - Méthode `getFullAddress()` pour afficher l'adresse complète

#### **Repository**
- `AddressRepository.java` - Accès aux adresses
  - Requêtes spécialisées par utilisateur, type, défaut

#### **Service**
- `AddressService.java` - Logique métier
  - ✅ Créer une adresse
  - ✅ Récupérer toutes les adresses d'un utilisateur
  - ✅ Récupérer une adresse spécifique
  - ✅ Mettre à jour une adresse
  - ✅ Supprimer une adresse
  - ✅ Obtenir l'adresse par défaut
  - ✅ Définir une adresse par défaut
  - ✅ Récupérer les adresses par type
  - ✅ Gestion de l'adresse par défaut lors de la suppression

#### **Controller**
- `AddressController.java` - 8 endpoints protégés
  - POST /api/addresses
  - GET /api/addresses
  - GET /api/addresses/{id}
  - PUT /api/addresses/{id}
  - DELETE /api/addresses/{id}
  - GET /api/addresses/default
  - PUT /api/addresses/{id}/set-default
  - GET /api/addresses/type/{type}

#### **Configuration**
- `SecurityConfig.java` - Mise à jour pour protéger `/api/addresses/**`

#### **Documentation**
- `ADDRESS_GUIDE.md` - Guide complet avec tous les endpoints

---

## 🔐 Sécurité Implémentée

✅ **Authentification JWT requise** pour tous les endpoints  
✅ **Isolation des données** - Les utilisateurs ne voient que leurs adresses  
✅ **Gestion de l'adresse par défaut** - Une seule par utilisateur  
✅ **Validation des données** - Code postal, champs obligatoires  
✅ **Autorisation** - Les utilisateurs ne peuvent modifier que leurs adresses  

---

## 📊 Structure de Données

### Table `addresses`
```sql
- id (PK)
- user_id (FK) - Lien vers l'utilisateur
- first_name (VARCHAR)
- last_name (VARCHAR)
- street (VARCHAR)
- city (VARCHAR)
- state (VARCHAR)
- zip_code (VARCHAR)
- country (VARCHAR)
- phone_number (VARCHAR)
- is_default (BOOLEAN)
- address_type (VARCHAR) - SHIPPING, BILLING
- instructions (TEXT)
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

---

## 🚀 Endpoints Disponibles

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/addresses` | Créer une adresse |
| GET | `/api/addresses` | Voir toutes les adresses |
| GET | `/api/addresses/{id}` | Voir une adresse |
| PUT | `/api/addresses/{id}` | Mettre à jour une adresse |
| DELETE | `/api/addresses/{id}` | Supprimer une adresse |
| GET | `/api/addresses/default` | Obtenir l'adresse par défaut |
| PUT | `/api/addresses/{id}/set-default` | Définir comme défaut |
| GET | `/api/addresses/type/{type}` | Obtenir adresses par type |

---

## 💡 Fonctionnalités

### Gestion des Adresses
- ✅ Créer plusieurs adresses par utilisateur
- ✅ Mettre à jour les adresses existantes
- ✅ Supprimer les adresses
- ✅ Marquer une adresse comme défaut

### Flexibilité
- ✅ Types d'adresses (SHIPPING, BILLING)
- ✅ Instructions de livraison optionnelles
- ✅ Numéro de téléphone optionnel

### Gestion Intelligente
- ✅ Une seule adresse par défaut
- ✅ Lors de la suppression de l'adresse par défaut, une autre est désignée
- ✅ Basculer entre adresses par défaut facilement

---

## 📝 Exemple d'Utilisation

### 1. Créer une adresse de livraison
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

### 2. Voir toutes les adresses
```bash
curl -X GET http://localhost:8080/api/addresses \
  -H "Authorization: Bearer <token>"
```

### 3. Créer une adresse de facturation
```bash
curl -X POST http://localhost:8080/api/addresses \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jane",
    "lastName": "Smith",
    "street": "456 Oak Ave",
    "city": "Los Angeles",
    "state": "CA",
    "zipCode": "90001",
    "country": "USA",
    "addressType": "BILLING"
  }'
```

### 4. Obtenir les adresses de livraison
```bash
curl -X GET http://localhost:8080/api/addresses/type/SHIPPING \
  -H "Authorization: Bearer <token>"
```

---

## 🔧 Améliorations Futures

1. **Géolocalisation** - Récupérer les coordonnées GPS
2. **Validation d'adresse** - Vérifier que l'adresse existe
3. **Frais de port** - Calculer selon l'adresse
4. **Historique d'adresses** - Garder l'historique des adresses utilisées

---

## ✨ État du Projet

1. ✅ **Authentification** - FAIT ✓
2. ✅ **Panier** - FAIT ✓
3. ✅ **Adresses** - FAIT ✓
4. ⏭️ **Commandes** - À venir
5. ⏭️ **Paiement** - À venir

