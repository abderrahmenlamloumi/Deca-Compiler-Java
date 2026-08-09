# Paint

La bibliothèque Paint fourni un programme pour dessiner sur le terminal.

Elle repose sur l'extension `UserIO` et `Math`.

## Choix de conception

À l'heure actuelle, l'extension paint est limitée à une sélection de 4 points maximum, car elle a été créée avant d'avoir l'extension ajoutant les tableaux.

Paint a été créé par ajout successif de méthodes sans réusinage du code pour s'assurer d'avoir une version toujours fonctionnelle livrable.

Il n'y a pas de tests automatisés créés par manque de temps et de complexité de tester une boucle infinie et le contenu affiché dans un terminal ansi que le comportement différents selon différents terminaux.

## Problèmes connus

Certains dessins ne sont pas parfait à cause d'approximation de flottant en coordonnées décimale.

Certain clic sur la souris ou clavier s'écrivent dans le terminal. 
Un fix pour en éviter la majorité a été ajouter, mais n'est pas parfait, voici son fonctionnement lors d'un évènement :  
> Déplacer le cursor à l'endroit souhaité \
> dessiner\
> replacer le curseur en 0,0\
> réécrire la premiere ligne\ 

Dans la majorité des cas, le texte non souhaité s'écrit pendant que le curseur est en 0,0. 
Vu qu'on réécrit la ligne ce n'est pas gênant.  
Mais parfois le texte s'écrit au milieu du tableau.


## Détails d'implémentation

Le tracé des lignes repose sur la bibliothèque math qui propose les formules pour les courbes et droites de bezier.

Le remplissage d'une forme repose sur l'algorithme du `Ray-Casting`
