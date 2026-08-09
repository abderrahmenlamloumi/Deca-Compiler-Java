# Tableaux

Du point de vue de la mémoire, un tableau est un espace mémoire réservé, contenant des données contiguës.
L'emplacement réservé prend donc `octets par mot * taille du tableau`.
Une variable contenant un tableau contient en réalité un pointeur vers la première case du tableau.

## Changements dans la grammaire

Pour ajouter les tableaux au parser, il a été nécessaire de modifier la grammaire.

Dans les expressions primaires, nous avons ajouté des règles à celles déjà existantes:

```
primary_expr ::= ... \
            | NEW type '[' expr ']' \
            | primary_expr '[' expr ']'
```

Ces règles nous permettent l'initialisation de tableaux et l'accès par index aux éléments des tableaux.

Afin que le parser considère les tableaux comme un type, nous avons également ajusté la règle `type`:

```
type ::= ident ('[' ']')*
```
Ainsi, chaque identifiant rencontré a la possibilité d'être vu comme un tableau, voire même un tableau à plusieurs dimensions en ajoutant plusieurs '[ ]'.


## Gestion de l'initialisation

Les éléments des tableaux initialisés ont une valeur par défaut:

- Tableaux de `int` et de `float` : `0` et `0.0` respectivement

- Tableau de `boolean` : `false`

- Tableaux d'objets, de tableaux ou de `string` : `null`

## Cast et instanceof

L'utilisation de l'instruction `instanceof` et des casts sur un tableau sont interdits.

Ce choix est dû au fait que dans le cas d'un tableau d'objet, ce tableau peut potentiellement contenir plusieurs objets de différentes classes à cause de l'héritage.

Il n'existe par ailleurs pas de type `array` visible par l'utilisateur.

## Print avec les tableaux

Il n'est pas possible d'utiliser `print` et ses dérivés avec les tableaux.
C'est un choix, étant donné que cela est impossible en Java, et que Deca est un mini-Java, nous avons décidé de ne pas supporter le `print` avec un tableau.

## IMA

Nous avons rajouté l'instruction IMA `RegisterIndex` à notre génération de code.
Elle nous permet d'accéder à l'index d'un tableau en utilisant un registre comme pointeur vers la première case du tableau, et un autre registre contenant le nombre de mots de décalage pour atteindre la case souhaitée
