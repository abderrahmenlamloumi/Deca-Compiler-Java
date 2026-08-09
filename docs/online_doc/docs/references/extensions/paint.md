# Paint

Cette extension du programme déca permet d'avoir une interface semi-graphique dans le terminal afin d'y réaliser des opérations de dessins à l'aide de méthodes géométrique.

## Utilisation

```txt title="Lancer le programme"
#include "Paint.decah"
{
    Paint paint = new Paint();
    paint.start();
}
```

:::danger
Le programme doit être lancé avec une mémoire allouée assez grande `-t 999999`
:::

### En-tête

La barre en haut donne les indications d'utilisation avec :

- Un message de bienvenue.
- Des potentiels messages d'erreurs.
- La couleur sélectionnée.
- Le choix des couleurs possibles et la touche du clavier pour la sélectionner.
- Les indications des touches

### Effacer le terrain de dessin

En appuyant sur `w` le tableau sera remis à 0.

### Mode dessin

Appuyer sur `D` pour activer le mode dessin.
Dans ce mode le clic de la souris dessine le pixel choisi.

### Mode géométrie

Le mode géométrie permet de réaliser différentes opérations à partir de points.

#### Placement de points

Il est possible de placer jusqu'à 4 points.
Pour se faire cliquer sur l'espace libre, puis une fois que vous êtes certains de la position, confirmer le placement en appuyant sur `ENTRER`.

Vous pouvez passer au point suivant.

Après avoir appliqué une opération géométrique, les points précédemment placer sont supprimés.

#### Ligne droite

> Il vous faut entre 2 et 4 points pour utiliser cet outil.

Tracer une ligne droite entre des points en appuyant sur `G`, En appuyant sur `H` le dernier point et le premier point seront également reliés (fermant la forme).

:::warning
Pour cause d'arrondi de décimaux, il se peut que les lignes droites soient parfois imparfaite.
:::

#### Remplissage 

Après avoir placé les 4 points, appuyer sur `F` pour remplir la zone entre les 4 points.

#### Rectangle

Après avoir placé 2 points, appuyer sur `M` pour placer les 2 autres points complémentaire pour former un rectangle parfait.