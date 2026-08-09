# UserIO

La bibliothèque UserIO fournit des méthodes pour échanger des informations avec le terminal. 

Pour cela, elle utilise les fonctionnalités proposées avec le standard `ANSI` et les instructions IMA `WUTF8` et `RUTF8`.

Le standard `ANSI` permet d'échanger des messages sous la forme :

> ESC[xxxxx

Où xxxx est le code associé. 
En fonction des codes, on peut activer les inputs de la souris, changer la couleur du fond, modifier la position du curseur, etc.

La bibliothèque intègre un certain nombre de méthodes implémentées en assembleur pour écrire directement dans le terminal.

## Mécanique

La bibliothèque UserIO repose sur un système de callback. 
La classe UserIO a une méthode principale `start()` qui écoute en permanence les entrées utilisateur et les informations du terminal, et en fonction des entrées, appelle les différentes méthodes que le développeur aura remplies en créant sa classe qui étend UserIO.

## Modification du langage Déca et du compilateur

Aucun ajout au Lexer et Parser ni à l'AST n'a été fait. 

Les instructions `WUTF8` et `RUTF8` ont été implémentées via des méthodes en assembleur directement pour ne pas modifier le code Java. 
Ça permet d'être plus efficace et de ne pas avoir à gérer un flag d'activation de ces instructions.