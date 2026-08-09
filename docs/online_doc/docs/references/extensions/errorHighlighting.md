# Messages d'erreurs

## Affichage des messages d'erreurs contextuelles

Cette extension a pour but d'ajouter plus de précision dans le message d'erreur contextuelle, en affichant la ligne qui
a causé l'erreur et en colorant en <span style={{color:"red"}}>rouge</span> la source exacte d'erreur.

:::tip
Activer l'extension avec l'option `-ffancy-errors`
:::

### Utilisation 

À fin de visualiser le message d'erreur coloré, il suffit de lancer le compilateur `decac` en lui
passant en paramètre un fichier `.deca` contenant une erreur contextuelle.

```bash
decac src/test/deca/context/invalid/class/03AssignNullToInt.deca
```
Dans ce fichier de test, on initialise une variable **a** de type **int** à **null** ce qui n'est pas autorisé dans deca
vu que **a** n'est pas un objet. Il s'agit donc d'une erreur contextuelle.
La source d'erreur étant le **null**, le message d'erreur aura la ligne qui a causé l'erreur avec le mot **null** coloré en rouge comme ceci: 

> int a = <span style={{color:"red"}}>null</span>;



