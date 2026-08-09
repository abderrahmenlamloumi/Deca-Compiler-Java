# Null

Le mot clé `null` permet d'initialiser une variable de classe sans instancier une instance de ladite classe.

Il est possible de comparer une variable avec null ou ses attributs (non primitif) pour vérifier s'ils ont été initialisé.

```txt title="Exemple"
class A{}

{
    A a = null
    if (a == null) { 
        ...
    }
}
```

:::note
Null ne peut être utilisé pour comparer des types primitifs tels que les valeurs numériques.
:::

**Erreurs liées**

> [Memory error: Dereferencing a null value](/docs/references/errors/runtime#memory-error-dereferencing-a-null-value)
> 
> [Cannot compare operands of different types](/docs/references/errors/compilation#cannot-compare-operands-of-different-types)