# Objet string

L'extension *string* autorise l'utilisateur à utiliser le type `string`.
Ce type peut être affiché et être concaténé à une autre instance de `string`.

## Utilisation

:::tip
L'extension doit être activée avec l'option `-fstring-object` de Decac.
:::

Les chaînes de caractères sont immutables et supportent toute séquence UTF-8 valide.
Il est alors possible de placer une chaîne de caractères dans une variable, de la stocker dans un attribut et de la passer en paramètre d'une méthode.

```
class Greetings {
    void greet(string name) {
        println("Hello ", name);
    }
}

{
    string alice = "Alice";
    new Greetings().greet(alice); // Affiche "Hello, Alice"
    new Greetings().greet("Bob"); // Affiche "Hello, Bob"
}
```

L'opérateur + permet de concaténer deux chaînes de caractères, sous condition qu'aucune ne soit nulle. Par exemple :

```
{
    string alice = "Alice";
    string bob = "Bob";
    println(alice + ", " + bob); // Affiche "Alice, Bob"
}
```

`null` peut être assigné à une variable de type `string`. Il s'agit par ailleurs de sa valeur par défaut dans les attributs d'un objet.

Il n'est pas possible d'afficher des chaînes de caractères nulles. Il peut par conséquent être utile de vérifier que la chaîne de caractères n'est pas nulle avant d'effectuer un traitement :

```
class Greetings {
    void greet(string name) {
        if (name == null) {
            println("Hello unknown!");
        } else {
            println("Hello ", name);
        }
    }
}

{
    new Greetings().greet(null); // Affiche "Hello unknown!"
}
```

Deux chaînes de caractères peuvent être comparés avec la méthode `equals` :

```
{
    string a = "abc";
    string b = "cba";
    if (!a.equals(b)) {
        println("different"); // Affiche "different"
    }
}
```

Enfin, il est possible de connaître la taille de la chaîne de caractères avec la méthode `length` :

```
{
    println("".length()); // Affiche 0
    println("abc".length()); // Affiche 3
}
```

## Relation avec les objets

`string` est un type nullable sans être un type objet : il n'hérite donc pas de la classe `Object` et ne peut pas être étendu.
