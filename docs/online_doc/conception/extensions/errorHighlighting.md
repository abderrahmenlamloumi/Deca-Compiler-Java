# Messages d'erreurs

## Affichage des messages d'erreurs contextuelles

L'affichage des messages d'erreurs avec cette extension affiche la ligne du code `deca` qui cause l'erreur contextuelle, tout en colorant en rouge la partie exacte qui représente la source d'erreur.

## Choix de conception

La classe `LocationException` dans le package `tree` dispose d'une méthode `display` qui offre une façon générique
d'afficher les messages d'erreurs. 

Pour afficher la ligne du code `deca` contenant l'erreur, on utilise l'attribut `location` pour lire le fichier à compiler
et récupérer la ligne en question. 

Afin de pouvoir colorer seulement la source d'erreur, il fallait aussi déterminer la fin de l'expression causant l'erreur. Pour cela, on a ajouté un paramètre `endToken` dans
la méthode `tokenLocation` utilisé par le parseur pour définir la *location* d'une expression. 

Dans la classe `Location`, on a ajouté deux attributs, un pour stocker l'index de fin de l'expression, afin de délimiter 
la partie du code `deca` qui cause l'erreur et un deuxième pour stocker l'indice de la ligne de fin pour pouvoir gérer 
le cas des erreurs sur des multiples lignes.  