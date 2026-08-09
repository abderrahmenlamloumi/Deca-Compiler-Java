# ReadInt, ReadFloat


## readInt()

L'instruction `readInt()` permet à l'utilisateur de saisir un entier durant l'éxécution du code.

`readInt()` ne prend aucun paramètre et renvoi un entier. 

Elle interrompt l'exécution du code jusqu'à ce que l'utilisateur appuie sur `ENTER`.
Si la valeur saisie par l'utilisateur entre l'éxecution sur code et sa validation n'est pas un entier valide, une erreur est levée et le programme s'arrête.

```txt title="Exemple"
int a = readInt();

int b;
b = readInt();
```

## readFloat()

L'instruction `readFloat()` permet à l'utilisateur de saisir un décimal durant l'éxécution du code.

`readFloat()` ne prend aucun paramètre et renvoi un décimal.

Elle interrompt l'exécution du code jusqu'à ce que l'utilisateur appuie sur `ENTER`.
Si la valeur saisie par l'utilisateur entre l'éxecution sur code et sa validation n'est pas un décimal valide, une erreur est levée et le programme s'arrête.

```txt title="Exemple"
float a = readFloat();

float b;
b = readFloat();
```