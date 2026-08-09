# Return


## return

L'instruction `return` permet de retourner une valeur à la fin de l'exécution d'une méthode.

Ce mot clé doit être utilisé uniquement si la fonction attend une valeur de retour (autre que void donc).

```txt title="Exemple"
class A {
    int a(){
        return 1;
    }
}
```

**Erreurs possibles**

[Cannot return a void value](/docs/references/errors/compilation#cannot-return-a-void-value)