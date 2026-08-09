# Projet Génie Logiciel, Ensimag.
gl10, 08/04/2025.

## Rendu initial

16/05/2025

Implémente l'incrément print-variable et :
- Traite les assignations de variables comme des expressions.
- Supporte l'addition, la soustraction, la multiplication, le quotient et le reste de deux nombres sur des expressions de n'importe quelle longueur.
- Gère les instructions if, sans optimisation, avec les littéraux booléens et les comparaisons (`==`, `!=`, `<`, `<=`, `>`, `>=`) de deux expressions.

## Rendu intermédiaire

23/05/2025

Implémente l'incrément devinette dichotomique et :
- Gère les erreurs de division par zéro

### Manuel

Le compilateur prend en entrée un ou plusieurs fichiers .deca. Si le programme est valide, un fichier .ass est généré.

En cas d'erreur de syntaxe ou de type, une erreur est levée dans la sortie d'erreur.

Sont implémentés :
- La vérification du type des variables ("X does not refer to a value/type."/"Variable cannot have type void"/"Variable X is already declared")
- La vérification du type des rvalues ("Incompatible types for assignment: expected X, got Y")
- La vérification du type des conditions ("Condition must be of type boolean")
- La vérification du type des expressions booléennes ("All Operands should be boolean"/"Operands compared should be float or int"")
- La vérification du type des expressions arithmétiques ("Both sides of a binary expression must have the same type."/"The modulo operator is only supported with integers.")
- La vérification du type des print ("Only string, int and float may be printed")

À l'exécution, peuvent être levées des erreurs liées à :
- Une lecture dans un format incorrect ("I/O error: Failed to read formatted input.")
- Une division par zéro ("Arithmetic error: Attempt to divide by zero.")

## Deca sans objet

06/06/2025

Implémente l'incrément de Deca sans la gestion des objets.

Le compilateur peut gérer :
- Toutes les expressions arithmétiques (moins unaire)
- L'échappement des caractères
- Le TSTO
- Optimisation des DVal
- Inclusion des fichiers (#include)
- Gestion du débordement des flottants


On a également ajouté des options sur le compilateur :

- -P : Permet la compilation en parallèle de plusieurs 
- -n : No check, supprime des tests à l'exécution (exemple division par zéro, overflow flottant, etc.)


## Rendu final pré-soutenance

### Instructions
- [Variables](https://gl.glrm.fr/docs/references/instructions/variable)
- [Conditions](https://gl.glrm.fr/docs/references/instructions/conditions)
- [while](https://gl.glrm.fr/docs/references/instructions/while)
- [readInt(), readFloat()](https://gl.glrm.fr/docs/references/instructions/read)
- [print(), println(), printx(), printlnx()](https://gl.glrm.fr/docs/references/instructions/conditions)
- [return](https://gl.glrm.fr/docs/references/instructions/return)

### Expressions
- [Comparaisons logiques](https://gl.glrm.fr/docs/references/expressions/cmp)
- [Opérations arithmétiques](https://gl.glrm.fr/docs/references/expressions/arithmetique)
- [Opérateur unaire](https://gl.glrm.fr/docs/references/expressions/unary_op)
- [Null](https://gl.glrm.fr/docs/references/expressions/null)

### Classes

- [Classes](https://gl.glrm.fr/docs/references/classes/class)
- [Héritage](https://gl.glrm.fr/docs/references/classes/heritage)
- [Objet](https://gl.glrm.fr/docs/references/classes/objet)
- [Conversion](https://gl.glrm.fr/docs/references/classes/conversion)
- [Méthodes Assembleur](https://gl.glrm.fr/docs/references/classes/assembleur)

### Erreurs

- [Erreurs à la compilation](https://gl.glrm.fr/docs/references/errors/compilation)
- [Erreurs à l'exécution](https://gl.glrm.fr/docs/references/errors/runtime)

### Extensions

:::tip
Activer toutes les extensions avec l'option `-f`
:::

- [Tableaux](https://gl.glrm.fr/docs/references/extensions/arrays) `-farray`
- [Math](https://gl.glrm.fr/docs/references/extensions/math)
- [Messages d'erreurs](/docs/references/extensions/errorHighlighting.md) `-ffancy-errors`
- [String](https://gl.glrm.fr/docs/references/extensions/string) `-fconcat-string`
- [User IO](https://gl.glrm.fr/docs/references/extensions/userio)
- [Paint](https://gl.glrm.fr/docs/references/extensions/paint)
- [Assertions](https://gl.glrm.fr/docs/references/extensions/assert) `-fassert`










