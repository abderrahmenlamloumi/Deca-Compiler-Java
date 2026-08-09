---
sidebar_position: 4
---

# Objet

Toutes les classes héritent implicitement de la classe `Object`.

Cette classe est instanciable. 

La classe `Object` définit une méthode `equals` qui permet de comparer deux objets.
Son implémentation par défaut compare les références en mémoire des objets, mais elle peut être redéfinie pour comparer les valeurs des attributs des objets.

Par exemple, si nous voulons créer une classe pour passer par référence un entier mutable, nous pouvons redéfinir la méthode `equals` pour comparer les valeurs de l'entier plutôt que les références.

```txt
class MutableInt {
    int value;

    boolean equals(Object other) {
        if (other instanceof MutableInt) {
            return this.value == ((MutableInt) (other)).value;
        }
        return false;
    }
}

{
    MutableInt x = new MutableInt();
    MutableInt y = new MutableInt();
    x.value = 5;
    y.value = x.value;
    if (x.equals(y)) { // Retourne vrai. Sans la redéfinition de equals, cette condition serait fausse
        println("x and y are equal");
    } else {
        println("x and y are not equal");
    }
}
```

