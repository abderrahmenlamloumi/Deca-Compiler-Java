# User I/O

Cette extension du programme déca met à disposition du développeur une extension qui gère les entrées et sorties sur la sortie standard du terminal executant le programme.

Cette extension **est** capable de réagir à un appui sur le clavier, un clic sur la souris, modifier le style de la sortie (couleurs).

Cette extension **n'est pas** capable de donner l'état des touches de contrôle (CTRL, SHIFT), la position de la souris (uniquement au clic).

## Utilisation

L'extension fourni la classe `UserIO`, pour l'utiliser, il faut créer votre propre classe qui étend `UserIO` et configurer les différents `callback` en surchargeant les méthodes de la classe `UserIO`.

Pour lancer l'écoute de la sortie standard, il faut appeler la méthode `callbackOnInputExitable` en indiquant le code UTF8 de la clé de sortie.


```txt
class UserIOImplemented extends UserIO {
#### void onKeyInput(int key){
        print("Key n°", key, " have been pressed"); // Print l'identifiant UTF8 de key
        printUtf8(key) // Print l'interpretation UTF8
    }
    
    
#### void onMouseEvent(MouseEvent me){
        if (me.isPressed) {
            this.setCursorAt(me.x, me.y);
            print("X");
            this.cursorHome();
            this.eraseLine();
        }

    }

#### void onRPressed(){
        this.eraseFullScreen();
        this.cursorHome();
    }

}
{
    UserIO u = new UserIOCallback();

    u.cursorHome();
    u.enableMouseInput();
    u.callbackOnInputExitable(113);
    println("Exit program");

}
```

## Attributs

##### screenSize

Un objet de types `Coordinates` qui contient la taille du terminal de sortie.

:::warning
Certains terminaux ne supportent pas le changement de lignes et colonnes, cette taille est donc fictives et utiliser pour harmoniser les calculs.
:::

##### me

Un objet de types `MouseEvent` qui contient les données du dernier clic souris.

## Méthodes

#### int callbackOnInputExitable(int exitKey)

Méthode principale à appeler pour déclencher l'écoute sur la sortie standard.

Cette méthode est bloquante tant que la touche sur le clavier correspondant à exitKey en UTF8 n'a pas été appuyé.

#### MouseEvent getLastMouseEvent()

Getter de `me`

#### int readUtf8() 

Renvoi un entier correspondant au caractère UTF8 lu sur l'entrée standard,

Renvoi -1 si rien n'est lu.

:::note
N'est pas bloquant
:::

#### Coordinates getScreenSize()

Renvoie un objet Coordinates avec la taille de l'écran.

#### void enableMouseInput()

Active le callback des clicks sur la souris.

:::warning
Modifie le comportement du terminal même après execution du programme.
:::

#### void eraseFullScreen()

Efface tout l'écran.

#### void eraseLine()

Efface la ligne courante du curseur.


#### void setCursorAt(int x, int y)

Place le curseur à la position `x` et `y`.

#### void askCursorPosition()

Demande au terminal d'envoyer la position du curseur.

#### void cursorHome()

Déplace le curseur en haut à gauche de l'écran (0,0).

#### void printUtf8(int m)

Écrit dans la sortie standard l'interprétation UTF8 du décimal `m`

#### void sendCommandInt(int intCode)

Méthode utilitaire pour l'application des couleurs.

Envoie sur la sortie standard `ESC[intCode`

#### void setBlackBackground()

Change la couleur de fond pour les prochains print en noir.

#### void setRedBackground()

Change la couleur de fond pour les prochains print en rouge.

#### void setGreenBackground()

Change la couleur de fond pour les prochains print en vert.

#### void setYellowBackground()

Change la couleur de fond pour les prochains print en jaune.

#### void setBlueBackground()

Change la couleur de fond pour les prochains print en bleue.

#### void setMagentaBackground()

Change la couleur de fond pour les prochains print en magenta.

#### void setCyanBackground() 

Change la couleur de fond pour les prochains print en cyan.

#### void setWhiteBackground() 

Change la couleur de fond pour les prochains print en blanc.

#### void setDefaultBackground()

Change la couleur de fond pour les prochains print à la couleur par défaut.

#### void setScreenSize(int height)
Cette méthode change la taille de l'écran sur l'attribut `screenSize` et appel la méthode `setScreenSizeASM`.


#### void setScreenSizeASM(int height)
Cette méthode tente de changer la taille de l'écran au niveau du terminal.


#### void resetStyle()

Cette méthode supprime tous les changements de couleurs et de style pour les prochains print.

### Callback sur le clavier

#### void onKeyInput(int key)

Cette méthode est appelé à chaque fois qu'une touche (qui n'est pas une touche de contrôle) est appuié sur le clavier.

`key` correspond à son identifiant décimal en UTF8.

#### void onMouseEvent(MouseEvent me)

Cette méthode est appelé à chaque fois que le bouton gauche de la souris est appuyé ou relâché.

`me` est un objet `MouseEvent` contenant les coordonnées du clic et si c'est un clic ou un relâchement.

#### void onArrowUp()

Cette méthode est appelé lorsque la flèche haut est pressée.

#### void onArrowDown()
Cette méthode est appelé lorsque la flèche basse est pressée.

#### void onArrowLeft(){}

Cette méthode est appelé lorsque la flèche gauche est pressée.

#### void onArrowRight()
Cette méthode est appelé lorsque la flèche droite est pressée.


#### void on_Pressed()
Cette méthode est appelé lorsque chaque lettre de l'alphabet est pressée.

Les 26 lettres sont implémentés

> void onAPressed(){}  
> void onBPressed(){}  
> void onCPressed(){}  
> ...  
> void onZPressed(){}  

#### void onENTERPressed()
Cette méthode est appelé lorsque la touche ENTER est pressée.