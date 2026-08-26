<!--
====================================================================
  LABORATOIRE 2 - 420-930-MA - Ete 2026 - gr. 25604
  Template README a remplir et deposer sur Teams avant la deadline
====================================================================

Instructions :
  1. Copiez ce fichier dans votre depot GitHub sous le nom README.md
  2. Remplissez toutes les sections marquees [A COMPLETER]
  3. Supprimez tous les commentaires HTML (<!-- ... -->) avant la remise
  4. Deposez ce fichier (rempli) sur Teams, canal du groupe
     avec le titre : "Lab2 - Sujet X - Nom1 Nom2 [Nom3]"
====================================================================
-->

# [A COMPLETER : Nom du projet, ex. "Netflix Catalog - Lab2"]

**Cours** : 420-930-MA — Algorithmes et modèles de programmation
**Session** : Été 2026, groupe 25604
**Laboratoire** : 2 (Application JavaFX v1)
**Date de remise** : 11 septembre 2026, 23h59

---

## Équipe

| Nom complet | Adresse courriel | Contribution principale |
|-------------|------------------|--------------------------|
| [A COMPLETER : Nom Prénom 1] | [courriel1] | [ex : Modèle, Service, Tris] |
| [A COMPLETER : Nom Prénom 2] | [courriel2] | [ex : UI FXML, Controller, CSS] |
| [A COMPLETER : Nom Prénom 3 si équipe de 3] | [courriel3] | [ex : Algorithmes, Benchmark] |

---

## Sujet choisi

**Numéro du sujet** : [A COMPLETER : 1, 2, 3, 4 ou 5]
**Nom du sujet** : [A COMPLETER : ex. "Netflix Catalog"]

---

## 🔗 Lien du dépôt GitHub PUBLIC

**URL** : [A COMPLETER : https://github.com/USERNAME/nom-du-projet]

> ⚠️ Vérifier que le dépôt est **PUBLIC** et accessible sans authentification.
> Tester le lien dans un navigateur privé avant la remise.

---

## Fonctionnalités implémentées

### ✅ Obligatoires (cocher ce qui est fait)

- [ ] Architecture MVC avec packages séparés (model / service / algorithmes / controller / util)
- [ ] Chargement des données depuis fichier CSV (nombre de lignes : [A COMPLETER])
- [ ] Interface JavaFX principale avec liste/tableau
- [ ] Panneau détail affichant l'élément sélectionné
- [ ] Pagination fonctionnelle (taille de page : [A COMPLETER])
- [ ] Filtres multi-critères combinables (nombre implémentés : [A COMPLETER] / [nombre demandé])
- [ ] Recherche par texte en temps réel
- [ ] Interface Algorithme définie
- [ ] Tri #1 implémenté : [A COMPLETER : nom du tri]
- [ ] Tri #2 implémenté : [A COMPLETER]
- [ ] Tri #3 implémenté : [A COMPLETER]
- [ ] Comparateur/benchmark des tris avec mesure du temps
- [ ] Wishlist / Favoris (ajout, retrait, pas de doublons)
- [ ] CSS appliqué (thème visuel du projet)

### 🎁 Bonus (cocher ce qui est fait)

- [ ] [Bonus 1 : ex. Mode sombre/clair]
- [ ] [Bonus 2 : ex. Statistiques]
- [ ] [Bonus 3 : ...]

### ❌ Non implémenté (assumer honnêtement)

- [A COMPLETER : liste ce qui n'a pas été fait et pourquoi, ex. "Recherche insensible aux accents — manque de temps"]

---

## Structure du projet

```
[A COMPLETER : arborescence de ton projet, ex.]

netflix-catalog/
├── pom.xml
├── src/main/
│   ├── java/
│   │   ├── module-info.java
│   │   └── netflix/
│   │       ├── MainFx.java
│   │       ├── model/
│   │       ├── service/
│   │       ├── algorithmes/
│   │       ├── controller/
│   │       └── util/
│   └── resources/
│       ├── fxml/principal.fxml
│       ├── styles/theme.css
│       └── data/films.csv
```

---

## Instructions pour lancer le projet

### Prérequis

- JDK [A COMPLETER : 17 ou 21]
- Maven [A COMPLETER : version 3.x]
- (optionnel) IntelliJ IDEA / Eclipse

### Étapes

```bash
# 1. Cloner le dépôt
git clone [URL DU DEPOT]
cd [nom-du-dossier]

# 2. Compiler
mvn clean compile

# 3. Lancer l'application
mvn javafx:run
```

### Alternative dans IntelliJ

1. Ouvrir le projet dans IntelliJ (File > Open > dossier du projet)
2. Attendre que Maven télécharge les dépendances
3. Ouvrir `MainFx.java`
4. Cliquer sur le bouton Run

---

## Choix techniques

### Version Java utilisée
[A COMPLETER : ex. Java 21 avec JavaFX 21]

### Format des données
[A COMPLETER : CSV / JSON, séparateur, encodage, nombre de lignes]

### Algorithmes de tri implémentés
[A COMPLETER : lister les 3+ tris avec leur complexité théorique]

### Bibliothèques externes utilisées
[A COMPLETER : liste des dépendances Maven au-delà de JavaFX]

---

## Difficultés rencontrées

[A COMPLETER : décrire les 2-3 principales difficultés rencontrées et comment vous les avez résolues. Cette section n'est pas notée, mais elle nous aide à améliorer les prochains labos.]

---

## Répartition du travail (auto-évaluation)

| Membre | % contribution estimée | Ce sur quoi j'ai travaillé |
|--------|-----------------------|------------------------------|
| [Nom 1] | [ex : 50%] | [description] |
| [Nom 2] | [ex : 50%] | [description] |
| [Nom 3] | [si applicable] | [description] |

---

## Notes pour le correcteur

[A COMPLETER (optionnel) : commentaires utiles pour le correcteur, ex. "Le benchmark est accessible via le menu Outils > Comparer les tris"]

---

## Captures d'écran (fortement recommandé)

[A COMPLETER (fortement recommandé) : mettre 2-3 captures d'écran de l'application dans un dossier `screenshots/` du dépôt et les référencer ici]

Exemple :
```markdown
### Écran principal
![Écran principal](screenshots/principal.png)

### Écran de benchmark
![Benchmark](screenshots/benchmark.png)
```

---

## Historique Git

**Nombre total de commits** : [A COMPLETER : ex. 47]
**Date du premier commit** : [A COMPLETER]
**Date du dernier commit** : [A COMPLETER]

Voir l'onglet **Insights > Contributors** de GitHub pour voir la contribution de chacun.

---

<!--
====================================================================
  CHECKLIST FINALE AVANT LA REMISE (a supprimer avant remise)
====================================================================

[ ] Tous les [A COMPLETER] ont ete remplaces par de vrais contenus
[ ] Tous les commentaires HTML <!-- ... --> ont ete supprimes
[ ] Le lien GitHub est valide (teste dans un navigateur prive)
[ ] Le depot est PUBLIC (pas Prive)
[ ] Le README.md est bien present a la RACINE du depot
[ ] Le projet compile avec "mvn clean compile" sans erreur
[ ] Le projet lance avec "mvn javafx:run" sans erreur
[ ] Les donnees (CSV) sont dans src/main/resources/data/
[ ] Le .gitignore exclut target/, .idea/, out/
[ ] Chaque membre de l'equipe a des commits a son nom
[ ] Ce fichier README rempli a ete deposé sur Teams

DATE LIMITE : 11 septembre 2026, 23h59
====================================================================
-->
