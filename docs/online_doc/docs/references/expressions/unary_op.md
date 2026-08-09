# Opérateur Unaire

L'**opérateur unaire/inversion** (ou *not*) est une opération unaire qui s’applique à une valeur de type booléen (`True` ou `False`) et retourne sa valeur opposée. Elle est souvent représentée par l’opérateur `not`.


**Condition**

L'expression de l'**inversion** est valide uniquement si le **type_unary_op** respecte les règles suivantes :   
**type_unary_op** : Operator × Type → Type

```
type_unary_op(not, boolean) ≜ boolean
```

L'**inversion** s'applique uniquement sur un boolean.

**Exemple**
```
boolean b = true;
print(!b);

>> false
```

**Erreurs possibles**

> [Condition must be of type boolean](/docs/references/errors/compilation#condition-must-be-of-type-boolean)