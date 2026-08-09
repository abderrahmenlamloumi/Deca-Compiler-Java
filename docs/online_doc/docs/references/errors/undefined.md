# Erreurs indéfinies

La spécification de Deca et de la machine abstraite IMA ne définit pas certains comportements, ce qui peut entraîner des erreurs indéfinies. Ces erreurs peuvent survenir dans les situations suivantes :

### Non-respect des conventions de liaison

Les méthodes assembleur doivent accéder correctement aux paramètres passés via la pile.
Elles doivent également sauvegarder et restaurer les registres non-scratch qu'elles utilisent.

### Accès à des variables non initialisées

L'accès à des variables qui n'ont pas été initialisées entraîne des comportements indéfinis.
Il est important de s'assurer que toutes les variables sont correctement initialisées avant leur utilisation.
