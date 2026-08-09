---
sidebar_position: 6
---

# Méthodes Assembleur

## Description

La construction `asm("...")` permet d'insérer directement des instructions assembleur IMA au sein du code généré, tout en conservant la possibilité de spécifier des paramètres venant en Deca.
Elle peut être utilisée pour optimiser des parties critiques du code ou pour accéder à des fonctionnalités spécifiques qui ne sont pas directement disponibles en Deca.

---

## Syntaxe générale

```txt
typeRetour ident(typeParam1 param1, typeParam2 param2...) asm("
    instructions
    RTS
");
```

## Accès aux paramètres

La convention de liaison utilisée par le compilateur permet d'accéder aux paramètres passés aux méthodes via la pile. Ils sont accessibles en utilisant des offsets négatifs depuis le registre de base de la pile (LB).

Le paramètre implicite `this` est toujours accessible via l'offset `-2(LB)`. Puis suivent les paramètres supplémentaires :

- -3(LB) pour le premier paramètre.
- -4(LB) pour le 2eme paramètre.
- -5(LB) pour le 3eme paramètre.
- -6(LB) pour le 4eme paramètre.
- ...

## Gestion des registres

La machine abstraite IMA dispose de 16 registres à usage général pour stocker des valeurs.
Comme ils sont partagés entre les méthodes, il est important de sauvegarder et de restaurer les registres utilisés dans une méthode ASM pour éviter de corrompre l'état du programme.

La convention d'utilisation des registres est la suivante :

- R0 et R1 sont des registres "scratch" et peuvent être utilisés sans être sauvegardés. Un appel à une méthode est susceptible de modifier ces registres. C'est à l'utilisateur de les sauvegarder s'il souhaite conserver leur valeur.
- R2 à R15 sont des registres "non-scratch". Chaque appelant à une méthode s'attend à ce que ces registres soient sauvegardés et restaurés par la méthode appelée. Si une méthode utilise ces registres, elle doit les sauvegarder au début de la méthode et les restaurer avant de retourner.

### Exemple correct

```plaintext
void write(int n) asm("
    LOAD -3(LB), R1
    WINT
    RTS
");
```

### Exemple incorrect

```plaintext
void write(int a, int b) asm("
    LOAD -3(LB), R2
    ADD -4(LB), R2 ; ! Modifie R2 sans le sauvegarder
    LOAD R2, R1
    WINT
    RTS
");
```

Dans cet exemple incorrect, le registre R2 est modifié sans être restauré, ce qui pourrait provoquer des erreurs imprévisibles dans le reste du programme.

```plaintext
void write(int a, int b) asm("
    PUSH R2 ; Sauvegarde R2
    LOAD -3(LB), R2
    ADD -4(LB), R2
    LOAD R2, R1
    WINT
    POP R2 ; Restaure R2
    RTS
");
```

## RTS

Une méthode ASM doit toujours se terminer par l'instruction `RTS` (Return from Subroutine) pour indiquer la fin de la méthode et retourner au point d'appel.

Elle doit placer l'éventuelle valeur de retour dans le registre R0 avant d'appeler `RTS`.

## Erreurs possibles

> [Comportements indéfinis](/docs/references/errors/undefined)
