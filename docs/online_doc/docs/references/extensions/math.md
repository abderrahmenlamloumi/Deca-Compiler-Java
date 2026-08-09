# Math

L'extension *Math* permet à l'utilisateur d'utiliser une varieté de méthodes mathématiques.

## Utilisation


L'extension Math permet à l'utilisateur d'accéder à une large variété de méthodes mathématiques.

Ces méthodes sont accessibles en instanciant un objet de type Math après l'inclusion du fichier de classe, puis en appelant les méthodes depuis cet objet.

```txt title="Exemple"

#include "Math.decah"

class TestMath {
Math m = new Math();
    // Exemple : m.sin(m.pi / 2);
}
```



## Méthodes disponibles


| Signature                     | Description                                                        |
|-------------------------------|--------------------------------------------------------------------|
| float abs(float a)            | Retourne la valeur absolue de a.                                   |
| float asin(float a)           | Retourne l’arc sinus de a, en radians (intervalle [-π/2, π/2]).    |
| float atan(float a)           | Retourne l’arc tangente de a, en radians (intervalle [-π/2, π/2]). |
| float ceil(float a)           | Retourne le plus petit entier supérieur ou égal à a.               |
| float cos(float a)            | Retourne le cosinus trigonométrique de l’angle a (en radians).     |
| float exp(float a)            | Retourne l’exponentielle de a, soit e^a.                           |
| float floor(float a)          | Retourne le plus grand entier inférieur ou égal à a.               |
| float ln(float a)             | Retourne le logarithme népérien de a.                              |
| float normalize(float a)      | Ramène un angle en radians dans l’intervalle [-π, π].              |
| float pow(float a, float b)	  | Retourne a élevé à la puissance b (a^b).                           |
| float sin(float a)            | Retourne le sinus trigonométrique de l’angle a (en radians).       |
| float sqrt(float a)           | Retourne la racine carrée de a.                                    |
| float tan(float a)            | Retourne la tangente trigonométrique de l’angle a (en radians).    |
| float toDegrees(float rad)    | Convertit un angle de radians en degrés.                           |
| float ulp(float f)            | Retourne la plus petite différence représentable autour de f.      |
| int fact(int n)               | Retourne la factorielle de n.                                      |
| int random()                  | Retourne un entier pseudo-aléatoire entre 1 et 4.                  |

## Constantes

| Attribut        | Valeur    | Description                          |
|-----------------|-----------|--------------------------------------|
| float pi        | 3.1415927 | Valeur approchée de π.               |
| float two_pi    | 2 * pi    | Valeur de 2π.                        |
| float MIN_FLOAT | 2^-149    | Plus petite valeur positive non null |




## Point

Une classe `point` est fourni avec comme attribut x et y.

```txt title="Exemple"
#include "Point.decah"
{
    Point p1 = new Point();
    p1.x = 1;
    p1.y = 2;
}
```

## Bezier

```txt title="Exemple"

#include "LinearBezier.decah"

class A {
    LinearBezier b = new LinearBezier();
}
```

La classe `LinearBezier` qui étend `Bezier` est fournis pour créer des courbes suivant l'algorithme de bézier.

Les classes basées sur `Bezier` ont des méthodes initialisables via la méthode `set` pour passer les points nécessaires à la courbe.

`LinearBezier` : `set(Point start, Point end)`

La méthode `getPointAtCurve(float x)` permet d'obtenir la coordonnée de la courbe à l'indice X compris entre 0 et 1.

:::warning
QuadricBezier et CubicBezier sont encore en cours de développement et seront rajoutés pour vendredi.
:::
