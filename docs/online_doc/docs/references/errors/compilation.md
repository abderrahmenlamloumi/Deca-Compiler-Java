# Erreurs à la compilation

## Erreurs de types

### X does not refer to a value/type.  

Cette erreur intervient lorsqu'une variable ou un type/class est utilisé sans avoir été déclaré.

:::tip[Pour corriger l'erreur]
Déclarer la variable, le type ou la classe avant de l'utiliser.
:::

---

### Variable/Param cannot have type void  

Cette erreur intervient lorsqu'une variable ou un paramètre de méthode est déclaré avec le type `void`.

:::tip[Pour corriger l'erreur]
Changer le type de la variable pour un type valide.
:::

---

### Variable X is already declared

Cette erreur intervient lorsqu'une variable est déclaré plusieurs fois dans la même portée.

:::tip[Pour corriger l'erreur]
Supprimer les déclarations en double ou changer le nom d'une des déclarations.
:::

---

### Incompatible types for assignment: expected X, got Y

Cette erreur intervient lorsque le type d'une variable et sa valeur ne sont pas compatible

:::tip[Pour corriger l'erreur]
Changer le type de la variable ou la valeur.
:::

---

### Compared operands aren't supported

Cette erreur intervient lorsque le type d'utilisé dans une comparaison n'est pas supporté. (Par exemple un string)

:::tip[Pour corriger l'erreur]
Changer le type de l'opérande.
:::

---

## Erreurs de types en conditions

### Condition must be of type boolean

Cette erreur intervient lorsqu'une expression dans une condition ne retourne pas un boolean.

```txt title="Exemples"
if (2) {
int a = 6;
if (a) {
```

:::tip[Pour corriger l'erreur]
Modifier la condition pour avoir une condition valide.
:::

---

## Erreurs dans une expression booléenne

### All Operands should be boolean

Cette erreur intervient lorsqu'une des deux expressions d'un opérateur logique (ET / OU logique) n'est pas un boolean.

```txt title="Exemples"
if (2 && true) {
```

:::tip[Pour corriger l'erreur]
Modifier l'expression pour avoir une expression valide.
:::

---

## Erreurs dans une expression arithmétique/logique

### Operands compared should be float or int

Cette erreur intervient lorsqu'une des deux expressions d'un opérateur arithmétique n'est pas un nombre.

```txt title="Exemples"
if (2 > true) {
```

:::tip[Pour corriger l'erreur]
Modifier l'expression pour avoir une expression valide.
:::

---

### Both sides of a binary expression must have the same type.

Identique à l'erreur suivante.

```
print(10 / "string");
```

---

### Cannot compare operands of different types.

Cette erreur intervient lorsqu'une des deux expressions d'un opérateur arithmétique ne sont pas du même type (entier et décimaux).

```txt title="Exemples"
if (2 > 2.0) {
```

:::tip[Pour corriger l'erreur]
Modifier l'expression pour avoir une expression valide.  
Convertir les entiers en décimal ou l'inverse.
:::

---

### Types of operands are not compatible for exact comparison: A and B

Les opérateurs `==` et `!=` ne peuvent pas être utilisé pour comparer deux types différents.

```txt title="Exemples"
if (2 == 2.0) {
```

:::tip[Pour corriger l'erreur]
Convertir les décimaux en entier ou l'inverse.
:::

--- 


### The modulo operator is only supported with integers.

Cette erreur intervient lorsque l'opérateur modulo est utilisé avec un autre type qu'un entier.

```txt title="Exemples"
float b = 2.5;
int a = 5 % b;
```

:::tip[Pour corriger l'erreur]
Utiliser des entiers à la place des décimaux
:::

---

### Arithmetic operations are only applicable to int and float types

Cette erreur intervient lorsque les opérateur arithmétiques sont utilisés avec un autre type qu'un entier ou un décimal.

```txt title="Exemples"
print("foo" + "bar");
```

:::tip[Pour corriger l'erreur]
Utiliser des entiers ou des décimaux.
:::

### Incompatible type for unary minus : expected float or int, got X

Cette erreur intervient lorsque l'opérateur `-` est utilisé avec un autre type qu'un entier ou un décimal.

```txt title="Exemples"
float b = 2.5;
int a = 5 % b;
```

:::tip[Pour corriger l'erreur]
Utiliser des entiers ou des décimaux.
:::

---

## Erreurs lors d'un print 

### Only string, int and float may be printed

Les instructions print peuvent prendre en paramètre uniquement des chaînes de caractères, des entiers, et des décimaux. 

Si d'autres types sont passés en paramètre cette erreur sera levé.

:::tip[Pour corriger l'erreur]
Supprimer les paramètres non valides.
:::

---

## Erreurs lors d'une déclaration de classe

---

### Field cannot have type void

Un attribut d'une classe est de type `void`.

```txt title="Exemples"
class Array {
    void buffer;
}
```

:::tip[Pour corriger l'erreur]
Changer le type de l'attribut pour un type valide.
:::

---

### Field X is already declared

Deux attributs d'une même classe portent le même nom.

```txt title="Exemples"
class Foo {
    int x, x;
}
```

:::tip[Pour corriger l'erreur]
Retirer les attributs définis en double ou les renommer de manière à ce qu'ils soient uniques.
:::

---

### Class already exist

Deux classes portent le même nom.

```txt title="Exemples"
class Foo {
}
class Foo {
}
```

:::tip[Pour corriger l'erreur]
Renommer les classes de manière à ce qu'elles soient uniques.
:::

---

### Superclass X is not a class

La classe étendue n'est pas une classe valide.

```txt title="Exemples"
class Foo extends int {
}
```

:::tip[Pour corriger l'erreur]
Etendre les classes uniquement avec d'autres classes et non pas des types pimitifs ou non déclarés.
:::

---

### Field X cannot shadow a Y in the superclass

La classe enfant possède un attributs Y du même nom que la classe parent.

```txt title="Exemples"
class Bar{
    int x = 6;
}
class Foo extends Bar {
    int x = 5;
}
```

:::tip[Pour corriger l'erreur]
Utiliser la valeur définis dans la classe parent.  
Changer le nom de l'attribut dans la classe enfant.
:::

---

## Erreurs relatives aux classes

### Unable to select a field of a non-class type

Une sélection est écrite sur un type primitif.

```txt title="Exemples"
{
    4.abs;
}
```

:::tip[Pour corriger l'erreur]
Vérifier que le type voulu est utilisé dans la sélection.
Si l'objectif est de créer des attributs pour des types primitifs, créer plutôt une méthode sur un nouvel objet.
:::

---

### Object X does not have an attribute Y

Une sélection est écrite sur un attribut qui n'est pas déclaré.

```txt title="Exemples"
class Pair {
    int left;
    int right;
}

{
    Pair pair = new Pair();
    pair.middle = 5;
}
```

:::tip[Pour corriger l'erreur]
Modifier la sélection pour accéder à un attribut existant sur le type donné.
Si l'attribut souhaité existe mais sur une classe enfante, il est nécessaire de changer le type déclaré pour y instancier.
:::

---

### Protected field cannot be accessed outside of its class

Un attribut en visibilité `protected` est lu en dehors de la classe.

```txt title="Exemples"
class A {
    protected int b;
}

{
    A a = new A();
    a.b;
}
```

:::tip[Pour corriger l'erreur]
Changer la visibilité de l'attribut.
Créer un getter ou un setter.
:::




## Erreurs relatives aux méthodes

### Param X is already declared

Le paramètre X est présent plusieurs fois dans la signature de la méthode.

```txt title="Exemples"
class A {
    int foo(int a, int a){}
}
```

:::tip[Pour corriger l'erreur]
Changer le nom d'un des paramètres
:::

---

### Method X is overridden with different number of parameters

Une méthodes définie dans la classe enfant est déjà défini dans la classe parent mais les paramètres ne sont pas identiques.

```txt title="Exemples"
class A {
    int foo(int a, int b){}
}

class B extends A {
    int foo(int a){}
}
```

:::tip[Pour corriger l'erreur]
Faire correspondre les paramètres de la méthode de la classe enfant à la méthode de la classe parent.
:::

---

### Method X is overridden with a different return type

Une méthodes définie dans la classe enfant à un type de retour différent du type de retour de la méthode défini dans la classe parent.

```txt title="Exemples"
class A {
    int foo(){}
}

class B extends A {
    float foo(){}
}
```

:::tip[Pour corriger l'erreur]
Faire coincider les types de retour.
:::

---

### Method X is overridden using a Y parameter, while expecting type Z

Une méthodes définie dans la classe enfant à le même nom de paramètre que la méthode défini dans la classe parent mais les types de certains paramètres ne correspondent pas.

```txt title="Exemples"
class A {
    int foo(int a){}
}

class B extends A {
    int foo(float a){}
}
```

:::tip[Pour corriger l'erreur]
Faire coincider les types des paramètres.
:::

---


### Unable to call a method on the X primitive type

Un appel de méthode a été réalisé sur un autre type qu'une classe

```txt title="Exemples"
{
    int.call();
}
```

:::tip[Pour corriger l'erreur]
Il n'est pas possible d'appeler une méthodes sur un types primitives.
:::


---

### X does not refer to any existing method

Un appel de méthode a été réalisé alors qu'aucune méthode avec ce nom n'est définie dans la classe.

```txt title="Exemples"
class A {
    void foo() {
    }
}
{
    new A().bar();
}
```

:::tip[Pour corriger l'erreur]
Utiliser une méthode définie.
:::



---

### Trying to use the X `Y` as a method

Un appel de méthode a été réalisé sur un attribut de la classe.

```txt title="Exemples"
class A {
    int foo;
}
{
    new A().foo();
}
```

:::tip[Pour corriger l'erreur]
Supprimer les parenthèses pour utiliser l'attributs.
Renommer la méthode ou l'attributs.
:::


---

### Does not match number of Method parameters of X, Y expected, but found Z


Un appel de méthode ne contient pas le bon nombre de paramètres.

```txt title="Exemples"
class A {
    void foo(int a, int b, int c){
    }
}
{
    new A().foo(1, 2);
}
```

:::tip[Pour corriger l'erreur]
Faire coincider les paramètres entre la signature de la méthode et l'appel à la méthode.
:::


---

### Parameter X does not match Method type of Y, expected A but found B


Un appel de méthode contient le bon nombre de paramètres mais ne sont pas du même type que la signature de la méthode.

```txt title="Exemples"
class A {
    void foo(int a, int b, int c){
    }
}
{
    new A().foo(1, true, 3);
}
```

:::tip[Pour corriger l'erreur]
Faire coincider les types des paramètres entre la signature de la méthode et l'appel à la méthode.
:::

### Cannot return a void value

Une méthode contient un `return` sans valeur de retour.

```txt title="Exemples"
class A {
    void foo(){
        return;
    }
}
```

:::tip[Pour corriger l'erreur]
Ajouter une valeur de retour.
Supprimer le `return`
:::


### Error while trying to access an attribute

L'accès à un attribut correspond à une méthode.

```txt title="Exemples"
class A {
    int a
}
{
    new A().a()
}
```

:::tip[Pour corriger l'erreur]
Supprimer l'appel à l'attribut a.
:::
## Erreur avec l'instruction *instanceof*

### Left operand of the instanceof needs to be an object

L'opérande gauche de l'instruction *instanceof* doit être un objet.

```txt title="Exemples"
class A {
}

{
    int entier = 4;
    boolean b = entier instanceof A;
}
```
:::tip[Pour corriger l'erreur]
Assurer vous que l'opérande gauche de l'*instanceof* est un objet instancié.
:::
### Right operand of the instance of needs to be a class

```txt title="Exemples"
class A {
}

{
    A a = new A();
    boolean b = a instanceof int;
}
```
:::tip[Pour corriger l'erreur]
Assurer vous que l'opérande droite de l'*instanceof* est bien une classe.
:::

## Erreurs lors d'un transtypage

### Cannot cast expression of type X to type Y

Un cast est effectué entre deux types non compatibles pour une assignation.

```txt title="Exemples"

class A {
}

{
    A objet = new A();
    int entier = (int) (objet);
}
```

:::tip[Pour corriger l'erreur]
Assurer vous que les deux types sont compatible avant d'effectuer un cast.
Le transtypage n'est pas possible entre des types primitifs et des classes ou leur instances.
De même, cette erreur sera levée en cas de cast entre des objets sans lien d'hériatage entre leur classes.
:::

---

### Cannot cast void type

Impossible de caster vers *void*, il ne s'agit pas d'un type instanciable.

```txt title="Exemples"
{
    int entier = 4;
    (void) (entier);
}
```

:::tip[Pour corriger l'erreur]
Assurer vous que le type cible d'un transtypage n'est pas *void*.
:::

---

### Cannot cast expression to type X

Un cast est effectué sur une méthode sans valeur de retour.

```txt title="Exemples"
class A {
        void method(){}
}

{
    A a = new A();
    int b = (int)(a.method());
}
```
:::tip[Pour corriger l'erreur]
Assurer vous que la méthode possède un type de retour.
Celui-ci doit être compatible pour un cast vers le type désiré.
:::

---

## Erreurs liés aux tableaux


### Index value type must be int
Cette erreur se produit lorsque vous essayez d'utiliser une valeur qui n'est pas un entier (`int`) comme index dans un tableau.
```txt title="Exemples"
{
    int[] a = new int[10];

    a["b"] = 4;
}
```

:::tip[Pour corriger l'erreur]
Remplacez l'index non entier par un entier valide.
:::

---


### Array type can't be void

Cette erreur se produit lorsque vous essayez de déclarer un tableau avec un type `void`, ce qui n'est pas autorisé, car `void` ne représente pas une valeur ou un type utilisable.

```txt title="Exemples"
{
    void[] a = new void[3];

    println("test");
}
```


:::tip[Pour corriger l'erreur]
Utilisez un type valide pour le tableau, comme int, float, boolean, ou encore un type personnalisé défini par une classe.
:::

---
### Arrays are not supported in this version of Deca

La version de Déca que vous avez lancé n'a pas l'option qui permet d'activer les tableaux

:::tip[Pour corriger l'erreur]
Lancer `décac` avec l'option `-farray` 
:::

---

### Array size must be an integer

L'expression passée en taille d'un tableau n'est pas un entier.

```txt title="Exemples"
{
    void[] a = new void[3.5];
}
```

:::tip[Pour corriger l'erreur]
Indiquer un entier en taille de tableau.
Convertir le décimal en entier.
:::

---

### Cannot index non-array type

Vous utilisez l'opérateur de sélection d'index d'un tableau sur une variable qui n'est pas un tableau.

```txt title="Exemples"
{
    int a;
    a[0];
}
```

:::tip[Pour corriger l'erreur]
Corriger l'erreur en changeant votre syntaxe, vérifiez le nom des variables.
:::

---