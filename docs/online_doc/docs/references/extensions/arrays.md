# Tableaux

Les tableaux sont des structures permettant de ranger des données de manière ordonnées.

:::tip
Activer l'extension avec l'option `-farray`
:::

## Types autorisés

Un tableau peut contenir:
- Des `int`
- Des `float`
- Des `boolean`
- Des `string`
- Des objets
- Des tableaux

## Initialisation

Ils s'initialisent avec un `new` et prennent une **taille** entière à l'initialisation.

```
{
    int[] a = new int[5]; // tableau de 5 entiers
}
```

Tableau de `int` ou `float` : Initialisation par défaut à 0 et 0.0
Tableau de `boolean` : Initialisation par défaut à `false`
Tableau d'objets ou de tableaux : Initialisation par défaut à `null`

## Sélection et assignation

Pour récupérer ou assigner un élément du tableau, on appelle le tableau avec l'index de l'élément. 
Cet index doit être un entier. Il commence à 0, donc le 1er élément s'atteint via `tableau[0]`.

L'index ne peut pas être négatif.

```
{
    float[] b = new float[3]; // [0.0, 0.0, 0.0]
    float c;
    
    b[1] = 4.1; // [0.0, 4.1, 0.0]
    
    c = b[1]; // c = 4.1
}
```

## Tableaux imbriqués

Comme dit précédemment, on peut construire des tableaux de tableaux, utiles notamment pour des matrices.

```
{
    int[][] a = new int[][3]; // [null, null, null]
    
    a[0] = new int[2]; // [[0,0], null, null]
    
    a[0][1] = 5; // [[0,5], null, null]
}
```

## Accès à l'extérieur du tableau

En cas d'accès à un index égal ou supérieur à la taille du tableau, vous accéderez à une case mémoire non-allouée par l'initialisation du tableau.
Le comportement est alors indéfini.

```
{
    int[] a = new int[5];
    int b;
    b = a[5]; // b prend une valeur indéfinie
}
```

### Erreurs possibles

> [Logical error: Negative array index](/docs/references/errors/runtime#logical-error-negative-array-index)
> 
> [Array size must be an integer](/docs/references/errors/compilation#index-value-type-must-be-int)
> 
> [Index value type must be int](/docs/references/errors/compilation#index-value-type-must-be-int)
> 
> [Array type can't be void](/docs/references/errors/compilation#array-type-cant-be-void)
> 
> [Cannot index non-array type](/docs/references/errors/compilation#cannot-index-non-array-type)
> 
> [Arrays are not supported in this version of Deca](/docs/references/errors/compilation#arrays-are-not-supported-in-this-version-of-deca)