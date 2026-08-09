# Erreurs à l'exécution

###  Arithmetic error: Attempt to divide by zero.

Une division par 0 a eu lieu, résultant en un comportement non défini.

```txt title="Exemple
print(10 / 0);
```
:::tip[Pour corriger l'erreur]
Vérifier que le diviseur est différent de zéro avant de réaliser la division.
:::

---

### I/O error: Failed to read formatted input.

La valeur saisie au clavier par l'utilisateur ne correspond pas au type attendu par la fonction.

---

### Memory error: Stack Overflow

La pile d'appel de fonction a dépassé sa taille maximale.

:::tip[Pour corriger l'erreur]
Vérifier que toutes les boucles ou appels récursifs ont bien une condition de sortie/d'arrêt garanti.
:::

---

### Memory error: Heap Overflow

Le tas a dépassé sa taille maximale allouée.

:::tip[Pour corriger l'erreur]
Augmenter la mémoire allouée au tas via l'option `-t` d'IMA.
:::

---

### Memory error: Dereferencing a null value

Une sélection d'attribut ou de méthode est opérée sur une valeur `null`.

:::tip[Pour corriger l'erreur]
Retracer d'où est émise la valeur `null` (soit manuellement, soit comme valeur par défaut des attributs d'objets).
Si `null` est attendu à cet endroit, ajouter une condition vérifiant la valeur n'est pas nulle avant d'y accéder.
:::

---

### Overflow error: Attempt to manipulate an overflow value

La valeur manipulée dépasse les limites techniques du compilateur.

:::tip[Pour corriger l'erreur]
Changer la valeur.
:::

---

### Logical error: Missing expected return

Une méthode qui attend une valeur de retour s'est terminé sans l'instruction `return`

```txt title="Exemple"

class A {
    int foo() {
        print(1);
        // missing return, expected int but got void
    }
}
```

:::tip[Pour corriger l'erreur]
Ajouter les `return` manquants.
:::

---

### Cast error: Impossible to downcast to the target class, it is not an instance of the source class

Le transtypage entre deux classes s'est révélé impossible pendant l'exécution.
La class du type de retour n'hérite pas de la class source.

:::tip[Pour corriger l'erreur]
Assurer vous que le transtypage est possible entre deux classes.
Il est recommendé d'utiliser l'instruction 'instanceof' avant d'effectuer un cast sensible.
:::

---

### Assertion failed at line: x

L'assertion à la ligne *x* a échoué. Cela signifie que la condition booléenne a été évaluée à *false*.

:::tip[Pour corriger l'erreur]
Il pourrait être interessant de vérifier que la condition peut être vrai. 
:::

---

### Logical error: Negative array index
Cette erreur logique se produit lorsque vous tentez de créer ou d'accéder à un tableau en utilisant un index négatif, ce qui est invalide.

:::tip[Pour corriger l'erreur]
Assurez-vous que tous les indices utilisés sont des entiers compris entre 0 et la taille du tableau - 1.
:::

---