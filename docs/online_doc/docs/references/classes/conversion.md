---
sidebar_position: 5
---

# Conversion

La conversion de type permet de transformer une expression d'un type en un autre type compatible.

La conversion de type s'effectue via la syntaxe suivante :

```txt
(type) (z); // L'expression z est converti en (type)
```

Cette syntaxe décrit un cast explicite, mais le langage Deca permet également des conversions implicites dans certains cas :

- Conversion vers le même type.
- Conversion d'un type `int` en `float`.
- Conversion d'une classe enfant en sa classe parente (upcast).

Ces conversions implicites sont effectuées automatiquement par le compilateur sans nécessiter de cast explicite.

### Transtypage

Le transtypage définit la capacité de conversion d'un type primitif à un autre.

Il est possible de convertir des entiers (type `int`) en décimaux (type `float`) et inversement.
Les décimaux convertis en entier sont arrondi à l'entier inférieur.

```txt title="Exemples"
{
    float a = 3.14;
    int b = (int) (a);
    println(b); // 3
    
    int a = 3;
    float b = (float) (a);
    println(b); // 3.00000e+00

}
```

:::warning[Attention]
La conversion d'un nombre vers un autre format peut entraîner une perte de précision.
:::

### Upcast

Un objet enfant peut être typé comme son parent.

```txt
class A{
    int x = 0;
}

class B extends A{
    int x = 1;
}

{
    B a = new B();
    println( ((A) (a)).x ); // Explicitation de l'upcast nécessaire pour accéder à l'attribut x de A
}
```

### Downcast

Il est possible de convertir un objet parent en un de ses enfants.

```txt
class A{
    int x = 0;
}

class B extends A{
    int x = 1;
}

{
    A a = new B();
    println( ((B) (a)).x );
}
```

**Erreurs Possibles**

> [Cannot cast expression of type X to type Y](/docs/references/errors/compilation#cannot-cast-expression-of-type-x-to-type-y)
>
> [Cannot cast void type](/docs/references/errors/compilation#cannot-cast-void-type)
>
> [Cannot cast expression to type X](/docs/references/errors/compilation#cannot-cast-expression-to-type-x)