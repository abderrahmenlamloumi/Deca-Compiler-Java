# Arithmétique

## Plus
    
**Plus** est un terme qui exprime l'addition de deux quantités. Si a et b sont deux nombres, alors `a + b` signifie *a plus b* et représente la somme des deux nombres.

**Condition**
L'expression de l'**addition** (*plus*) n'est valable que dans les cas où le type des deux opérandes est soit un entier, soit un décimal.

**Exemple**
```txt title="Exemple"
print(1 + 1); 

>> 2
```

**Erreurs possibles**
> [Arithmetic operations are only applicable to int and float types](/docs/references/errors/compilation#arithmetic-operations-are-only-applicable-to-int-and-float-types)


## Moins

**Moins** (*minus*) est un terme qui exprime la soustraction de deux quantités. Si a et b sont deux nombres, alors `a - b` signifie **a moins b** et représente la différence entre les deux nombres.

**Condition**

L'expression de la **soustraction** est valide uniquement dans les cas où le type des deux opérandes est soit un entier, soit un décimal.

**Exemple**
```
print(5 - 2);

>> 3
```

**Erreurs possibles**
> [Arithmetic operations are only applicable to int and float types](/docs/references/errors/compilation#arithmetic-operations-are-only-applicable-to-int-and-float-types)


## Multiplication

La **multiplication** (*mult*) est une opération qui exprime la répétition d'une quantité un certain nombre de fois. Si `a` et `b` sont deux nombres, alors `a * b` signifie *a multiplié par b* et représente leur produit.

**Condition**

L'expression de la **multiplication** est valide uniquement dans les cas où le type des deux opérandes est soit un entier, soit un décimal.

**Exemple**
```
print(3 * 4);

>> 12
```

**Erreurs possibles**

> [Arithmetic operations are only applicable to int and float types](/docs/references/errors/compilation#arithmetic-operations-are-only-applicable-to-int-and-float-types)



## Divison

La **division** (*divide*) est une opération qui consiste à répartir une quantité en parts égales. Si a et b sont deux nombres, alors `a / b` signifie **a divisé par b** et représente le quotient de a par b.


**Condition**

L'expression de la **division** est valide uniquement dans les cas où le type des deux opérandes est soit un entier, soit un décimal.

**Exemple**
```
print(10 / 2);

>> 5
```

**Erreurs possibles**
> [Arithmetic operations are only applicable to int and float types](/docs/references/errors/compilation#arithmetic-operations-are-only-applicable-to-int-and-float-types)


## Modulo
Le **modulo** (*mod*) est une opération mathématique donnant le reste de la division d’une variable par un nombre donné. Si a et b sont deux nombres entier, alors `a % b` signifie **a modulo b** et représente le reste de la division de a par b.


**Condition**

L'expression du **modulo** est valide uniquement entre deux entiers.

**Exemple**
```
print(10 % 2);

>> 0
```

**Erreurs possibles**
> [Arithmetic operations are only applicable to int and float types](/docs/references/errors/compilation#arithmetic-operations-are-only-applicable-to-int-and-float-types)


## Moins unaire

Le **moins unaire** (*unary minus*) est une opération unitaire (*unaire*) qui s’applique à un seul opérande numérique. Elle sert à inverser le signe d’un nombre. Si `a` est un entier, alors `-a` signifie *l'opposé de a*.


**Condition**

L'expression du **moins unaire** s'applique uniquement sur un entier ou un flottant.

**Exemple**
```
print(--10);

>> 10
```

**Erreurs possibles**

> [Incompatible type for unary minus : expected float or int, got X](/docs/references/errors/compilation#incompatible-type-for-unary-minus--expected-float-or-int-got-x)




