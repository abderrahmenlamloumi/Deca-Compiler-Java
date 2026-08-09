
---

# Math

Cette librairie fournit une variété de méthodes mathématiques qui traitent des variables de type `float`.

## Fonctionnement

Pour utiliser les méthodes, il est nécessaire d’instancier un objet `Math` et d’inclure `"Math.decah"`.

## Méthodes trigonométriques

Dans ces méthodes, un développement en série de Taylor spécifique à chaque fonction a été utilisé, ce qui permet de renvoyer une valeur précise (grâce à l’ULP).

En effet, pour chacune de ces méthodes trigonométriques :

* **`float sin(float f)`**
* **`float cos(float f)`**
* **`float asin(float f)`**
* **`float atan(float f)`**

La précision est obtenue grâce à une approche fondée sur l’utilisation d’une série de Taylor et un critère d’arrêt basé sur l’ULP (Unité au Dernier Rang).
La fonction sinus, par exemple, est approximée à l’aide de son développement en série entière autour de zéro :
`sin(x) = x - x³/3! + x⁵/5! - x⁷/7! + ...`

Chaque terme successif améliore l’approximation, mais devient de plus en plus petit. Cependant, dans un environnement à virgule flottante (ici, en 32 bits), il existe une limite à la précision : en dessous d’une certaine valeur, les termes ajoutés n’ont plus d’effet réel sur le résultat final.

C’est là qu’intervient la méthode `ulp(f)` : dans chaque méthode, une boucle ajoute des termes à la somme tant que leur valeur absolue reste supérieure à l’ULP. Une fois qu’un terme devient plus petit que cette unité, on considère qu’il est numériquement négligeable et on interrompt le calcul.

Ainsi, l’approche adoptée ici ne dépend pas d’un nombre fixe d’itérations, mais d’un seuil d’arrêt dynamique basé sur la précision réelle du `float`.

---

### `float ulp(float f)`

La méthode `ulp` calcule la plus petite variation possible pour une valeur flottante donnée. Elle mesure le pas de discrétisation du nombre flottant à l’échelle de `f`, grâce à l’exploration de son exposant sous forme 32 bits.

---

En plus des méthodes trigonométriques et de `ulp`, d’autres méthodes ont été ajoutées, en s’inspirant de la librairie standard de Java, parmi lesquelles :

---

* **`float toDegrees(float rad)`** : Convertit un angle en radians en degrés avec la formule `rad × 180 / π`.
* **`float ln(float a)`** : Approxime le logarithme népérien à l’aide de la série de Taylor de `ln((1 + x)/(1 - x))`, où `x = (a - 1)/(a + 1)`, convergence rapide pour `a > 0`, arrêtée selon l’ULP.
* **`float exp(float x)`** : Calcule `e^x` via une série de Taylor : `1 + x + x²/2! + x³/3! + ...`, jusqu’à ce que le terme courant soit négligeable selon l’ULP.
* **`float pow(float a, float b)`** : Implémente `a^b` pour des exposants entiers (positifs ou négatifs) via des multiplications successives (cas général non géré).
* **`float abs(float a)`** : Renvoie la valeur absolue d’un `float` : retourne `a` si positif, `-a` sinon.
* **`float sqrt(float f)`** : Approxime la racine carrée par l’algorithme de Newton-Raphson : itère `x = (x + f/x)/2` jusqu’à convergence.
* **`float exp32bits(float f)`** : Estime l’exposant en base 2 d’un `float f` en le multipliant ou divisant par 2 jusqu’à ce que `f` soit dans `[1, 2)`.
* **`float floor(float a)`** : Renvoie la partie entière inférieure d’un `float` en décrémentant par pas de 1 jusqu’à atteindre la plus grande valeur ≤ `a`.
* **`float ceil(float a)`** : Renvoie le plus petit entier ≥ `a`, en appelant `floor(a)` puis en l’ajustant si nécessaire.
* **`float normalize(float f)`** : Ramène un angle `f` dans l’intervalle `[-π, π]` en retranchant des multiples de `2π` à l’aide de `floor()`.
* **`int fact(int n)`** : Calcule récursivement la factorielle d’un entier `n` : `n! = n × (n-1)!`, avec arrêt à `n = 1`.

---

### `Attributs de Math`

* **`float pi = 3.1415927;`**

* **`float two_pi = 2 * this.pi;`**

* **`float MIN_FLOAT = this.pow(2, -149);`**
  Correspond à la plus petite valeur positive normale que peut représenter un float 32 bits (le plus petit pas possible entre deux nombres flottants).





---

## `Classes Beziers`



### **Bezier**

La classe `Bezier` représente une **courbe de Bézier générique** et sert comme une **classe de base (abstraite)** pour modéliser des courbes définies par un ensemble de points de contrôle.


### **LinearBezier**

La classe `LinearBezier` est une **sous-classe de `Bezier`** qui représente **un segment de droite** entre deux points.


### **Point**

La classe `Point` représente un **point dans l’espace** qui est presenté grâce:

* Des coordonnées (float)
* Un angle

Cette classe est utilisée pour définir les points de contrôle des courbes de Bézier.

---


