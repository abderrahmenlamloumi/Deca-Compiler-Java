# Comparaison logique



## ET logique

L'opérateur ET logique `&&` (conjonction logique) renvoie `vrai` si et seulement si ses deux opérandes sont `vrai` ou équivalents à `vrai`. 

```txt title="Syntaxe"
expr1 && expr2
```

```txt title="Exemple"
print(1 == 1 && 2 == 2) // true
print(1 == 1 && 2 == 3) // false
```

**Table de vérité**

| A     | B     | A && B |
|-------|-------|:------:|
| **V** | **V** | **V**  |
| **V** | F     |   F    |
| F     | **V** |   F    |
| F     | F     |   F    |


**Comportement interne**

Le ET logique est paresseux.  

Si `expr1` est faux, `expr2` ne sera pas évalué et la valeur retournée par le ET logique sera `faux`

**Erreurs possibles**

> [All Operands should be boolean](/docs/references/errors/compilation#condition-must-be-of-type-boolean)

---

## OU logique

L'opérateur OU logique `||` (disjonction logique) renvoie `vrai` si et seulement si une des deux opérandes est `vrai` ou équivalents à `vrai`.

```txt title="Syntaxe"
expr1 || expr2
```

```txt title="Exemple" 
print(1 == 1 || 2 == 2) // true
print(1 == 1 || 2 == 3) // true
print(1 == 2 || 2 == 3) // false
```

**Table de vérité**

| A     | B     | A && B  |
|-------|-------|:-------:|
| **V** | **V** |  **V**  |
| **V** | F     |  **V**  |
| F     | **V** |  **V**  |
| F     | F     |    F    |


**Comportement interne**

Le OU logique est paresseux.

Si `expr1` est vrai, `expr2` ne sera pas évalué et la valeur retournée par le ET logique sera `vrai`

**Erreurs possibles**

> [All Operands should be boolean](/docs/references/errors/compilation#condition-must-be-of-type-boolean)

---

## Inférieur strict

L'opérateur Inférieur strict `<` renvoie `vrai` si et seulement si l'opérande gauche est numériquement plus petite que l'opérande droit.

```txt title="Syntaxe"
expr1 < expr2
```

```txt title="Exemple" 
print(1 < 2) // true
print(1 < 1) // false
print(2 < 1) // false
```

**Erreurs possibles**

> [Cannot compare operands of different types](/docs/references/errors/compilation#cannot-compare-operands-of-different-types)  
> [Compared operands aren't supported](/docs/references/errors/compilation#compared-operands-arent-supported)


---

## Inférieur ou égal

L'opérateur Inférieur ou égale `<=` renvoie `vrai` si et seulement si l'opérande gauche est numériquement plus petite ou égale que l'opérande droit.

```txt title="Syntaxe"
expr1 <= expr2
```

```txt title="Exemple" 
print(1 <= 2) // true
print(1 <= 1) // true
print(2 <= 1) // false
```

**Erreurs possibles**

> [Cannot compare operands of different types](/docs/references/errors/compilation#cannot-compare-operands-of-different-types)  
> [Compared operands aren't supported](/docs/references/errors/compilation#compared-operands-arent-supported)

---

## Égale

L'opérateur Égale `==` renvoie `vrai` si et seulement si l'opérande gauche est numériquement égale à l'opérande droit.

```txt title="Syntaxe"
expr1 == expr2
```

```txt title="Exemple" 
print(1 == 2) // false
print(1 == 1) // true
print(2 == 1) // false
```

**Erreurs possibles**

> [Cannot compare operands of different types](/docs/references/errors/compilation#cannot-compare-operands-of-different-types)  
> [Compared operands aren't supported](/docs/references/errors/compilation#compared-operands-arent-supported)

---

## Différent 

L'opérateur Différent `!=` renvoie `vrai` si et seulement si l'opérande gauche est numériquement différent à l'opérande droit.

```txt title="Syntaxe"
expr1 != expr2
```

```txt title="Exemple" 
print(1 != 2) // true
print(1 != 1) // false
print(2 != 1) // true
```

**Erreurs possibles**

> [Cannot compare operands of different types](/docs/references/errors/compilation#cannot-compare-operands-of-different-types)  
> [Compared operands aren't supported](/docs/references/errors/compilation#compared-operands-arent-supported)

---

## Supérieur ou égal 

L'opérateur Supérieur ou égale `>=` renvoie `vrai` si et seulement si l'opérande gauche est numériquement plus grande ou égale que l'opérande droit.

```txt title="Syntaxe"
expr1 >= expr2
```

```txt title="Exemple" 
print(1 >= 2) // false
print(1 >= 1) // true
print(2 >= 1) // true
```

**Erreurs possibles**

> [Cannot compare operands of different types](/docs/references/errors/compilation#cannot-compare-operands-of-different-types)  
> [Compared operands aren't supported](/docs/references/errors/compilation#compared-operands-arent-supported)

---

## Supérieur strict

L'opérateur Supérieur strict  `>` renvoie `vrai` si et seulement si l'opérande gauche est numériquement plus grande que l'opérande droit.

```txt title="Syntaxe"
expr1 > expr2
```

```txt title="Exemple" 
print(1 > 2) // false
print(1 > 1) // false
print(2 > 1) // true
```

**Erreurs possibles**

> [Cannot compare operands of different types](/docs/references/errors/compilation#cannot-compare-operands-of-different-types)  
> [Compared operands aren't supported](/docs/references/errors/compilation#compared-operands-arent-supported)

---