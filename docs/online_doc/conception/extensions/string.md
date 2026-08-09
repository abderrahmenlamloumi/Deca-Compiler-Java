# String

Contrairement à des formats de binaire comme l'ELF, la machine virtuelle IMA ne dispose pas d'une zone mémoire pour stocker des valeurs statiques.

## Représentation mémoire

Nous considérons les chaînes de caractères comme un tableau de scalaires UTF-8, avec comme toute première valeur la taille du tableau.
La chaîne "hello" est donc encodée comme une allocation de 6 mots : `5 | 104 | 101 | 108 | 108 | 111`.

Nous avons choisi de sauvegarder la taille directement plutôt que de terminer la chaîne de caractères par un octet nul puisque les algorithmes écrits dépendent d'un accès rapide à cette taille.

Les chaînes de caractères sont systématiquement allouées sur le tas.
Les littéraux sont générés comme des `LOAD` de chaque valeur UTF-8 successive dans un nouvel objet.

## Opérations sur les chaînes de caractères

### Affichage

L'affichage d'un littéral chaîne de caractères est optimisé pour faire appel à l'instruction `WSTR`.
Dans le cas des chaînes dynamiques, une boucle est générée pour itérer sur tous les scalaires UTF-8 et utiliser l'instruction `WUTF8`.

### Concaténation

La concaténation de chaînes de caractères s'effectue pour l'utilisateur comme une addition, mais elle génère un appel de procédure en arrière-plan.
Le code assembleur généré étant conséquent, déplacer cette procédure dans une fonction permet de réduire la taille du code final.

Nous additionnons la taille des deux chaînes de caractères pour allouer la chaîne de caractères résultante.
L'algorithme copie ensuite les scalaires UTF-8 de la première chaîne puis de la deuxième.

### Égalité

La méthode `equals` du type `string` est une pseudo-méthode (car ce n'est pas la méthode `Object` et elle n'utilise pas de _virtual table_).
Elle compare les longueurs des deux chaînes, et si elles sont identiques, vérifie l'égalité scalaire UTF-8 par scalaire.

### Longueur

La méthode `length` est aussi une pseudo-méthode. Elle récupère simplement le premier mot de la zone mémoire de la chaîne de caractères.

## Modification du système de types

Nous avons cherché à minimiser les changements sur notre compilateur pour conserver un compilateur respectant optionnellement la spécification Deca originelle.

Ainsi, le drapeau `-fstring-object` vient ajouter le type string dans l'environnement des types accessibles à l'utilisateur.
Ce même drapeau active dans `StringType` une modification de son comportement lorsque le type est comparé avec `null`.
