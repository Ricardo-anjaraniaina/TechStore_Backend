# 🛒 TechStore - Système de Panier Complet

## ✅ Implémentation Réalisée

### **Fichiers Créés**

#### **Entités (Entity Layer)**
- `CartItem.java` - Représente un article dans le panier d'un utilisateur
  - Lié à User et Product
  - Avec quantité et prix sauvegardés
  - Calcul automatique du sous-total

#### **DTOs (Data Transfer Objects)**
- `CartItemRequest.java` - Requête d'ajout au panier
- `CartItemResponse.java` - Réponse avec détails de l'article
- `CartResponse.java` - Réponse du panier complet avec total

#### **Repository (Data Access Layer)**
- `CartItemRepository.java` - Accès aux articles du panier
  - Requêtes spécialisées par utilisateur/produit

#### **Service (Business Logic)**
- `CartService.java` - Logique métier du panier
  - ✅ Ajouter au panier
  - ✅ Mettre à jour la quantité
  - ✅ Supprimer du panier
  - ✅ Vider le panier
  - ✅ Récupérer le panier complet
  - ✅ Vérification du stock
  - ✅ Calcul du total

#### **Controller (Presentation Layer)**
- `CartController.java` - Endpoints REST
  - 6 endpoints protégés par JWT
  - Récupération automatique du userId

#### **Configuration**
- `SecurityConfig.java` - Mise à jour pour protéger les endpoints `/api/cart/**`

#### **Documentation**
- `CART_GUIDE.md` - Guide complet des endpoints avec exemples

---

## 🔐 Sécurité Implémentée

✅ **Authentification JWT requise** pour tous les endpoints du panier
✅ **Vérification du stock** avant ajout/modification
✅ **Isolation des données** - Les utilisateurs ne voient que leur propre panier
✅ **Gestion des erreurs** - Messages explicites et codes HTTP appropriés
✅ **Validation des quantités** - Pas de quantités négatives ou nulles

---

## 📊 Structure de Données

### Table `cart_items`
```sql
- id (PK)
- user_id (FK) - Lien vers l'utilisateur
- product_id (FK) - Lien vers le produit
- quantity (INT) - Quantité commandée
- price (DECIMAL) - Prix au moment de l'ajout
- created_at (TIMESTAMP)
- updated_at (TIMESTAMP)
```

**Contrainte unique:** (user_id, product_id) - Un utilisateur ne peut avoir qu'une seule ligne par produit

---

## 🚀 Endpoints Disponibles

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/cart` | Récupérer le panier complet |
| POST | `/api/cart/items` | Ajouter un article |
| PUT | `/api/cart/items/{id}?quantity=X` | Mettre à jour la quantité |
| DELETE | `/api/cart/items/{id}` | Supprimer un article |
| DELETE | `/api/cart` | Vider le panier |
| GET | `/api/cart/count` | Nombre d'articles |

---

## 💡 Fonctionnalités

### Gestion des Articles
- ✅ Ajouter un produit (ou incrémenter si déjà présent)
- ✅ Modifier la quantité d'un article
- ✅ Supprimer un article spécifique
- ✅ Vider entièrement le panier

### Calculs Automatiques
- ✅ Sous-total par article = prix × quantité
- ✅ Total du panier = somme de tous les sous-totaux
- ✅ Compteur d'articles

### Vérifications
- ✅ Stock disponible avant ajout
- ✅ Quantité valide (> 0)
- ✅ Accès utilisateur sécurisé

---

## 📝 Exemple d'Utilisation

### 1. Enregistrement et connexion
```bash
# Enregistrement
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"john","email":"john@example.com","password":"Pass123"}'

# Réponse: {"token":"jwt_token_here",...}
```

### 2. Ajouter au panier
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer jwt_token_here" \
  -H "Content-Type: application/json" \
  -d '{"productId":5,"quantity":2}'
```

### 3. Voir le panier
```bash
curl -X GET http://localhost:8080/api/cart \
  -H "Authorization: Bearer jwt_token_here"
```

### 4. Mettre à jour la quantité
```bash
curl -X PUT "http://localhost:8080/api/cart/items/1?quantity=5" \
  -H "Authorization: Bearer jwt_token_here"
```

---

## 🔧 Améliorations Futures

1. **Réduction de prix** - Codes promotionnels
2. **Calcul des taxes** - Basé sur la localisation
3. **Frais de port** - Selon le poids/destination
4. **Sauvegarde du panier** - Persistance côté client (localStorage)
5. **Notifications** - Prix en baisse
6. **Produits similaires** - Recommandations

---

## ✨ Prochaines Étapes

1. ✅ **Authentification** - FAIT ✓
2. ✅ **Panier** - FAIT ✓
3. ⏭️ **Gestion du Stock** - À venir
4. ⏭️ **Système de Paiement** - À venir
5. ⏭️ **Adresses de Livraison** - À venir

