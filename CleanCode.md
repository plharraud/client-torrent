# Clean Code

Regles et conseils pour écrire du code de bonne qualité.

## Intro

On va principalement parler des principes SOLID, ce sont des principes généraux, qui peuvent s'addapter a tout les concepts/ 

## Deux regles?

Avant de parler des principes, on va voir deux regles générales:
* Un developpeur doit être capable de facilement mettre a jour un logiciel selon les besoins utilisateurs: Un produit n'est pas figé, on va souvent avoir des nouveaux besoin utilisateur. Il faut aussi optimiser l'effort et ne pas repartir de zero
* Un logicel doit tourner de façon consistante sans bugs

## SOLID

### Single Reponsibility Principle (SRP)

*Une classe doit avoir une seul et unique résponsabilité => une seule raison d'être modifiée*

```java
class Employee {
    void calculatePay(){}
    void save(){}
    void describeEmployee(){}
    void fildById(){}
}
```

Ici, on a mauvais exemple. On a 3 responsabliltés qui se chevauchent

* Core
* Database
* Presentation

Si une classe a trop de reponsablités, fragilité lors des changements, c'est facile de casser core et Presentation en changeant database.

En metant tout au même endroit, on couple cette classe, et potentiellement recuperer de bouts de codes non adaptés

Respecter le SRP permet de faire des tests vraiement isolés les uns des autres.

Il faut donc separer cette classe en plusieurs.

La solution

|app|core|data|
|-|-|-|
| |Employee| |
|EmloyeeDescriptor|PayEmployeeUseCase|EmployeeDatabaseInteraction|

```Java
public class Square {
    public final int x;
    public final int y;
    public final int size;

    ...
}
public void drawColoredSquare (
    final Graphics g,
    final Square square,
    final int color
) {
    g.setColor(color);
    g.fillRect(square.x, square.y, square.size, square.size);
}
```
CCL : 
* Bien identifer les responsablités d'un programe 
* Important de les separer

### Open Closed Principle (OCP)

*Les classes sont ouvertes à l'extension, mais fermés à la modification*

```java
public void checkOut(final Receipt receipt) {
    Money total = Money.zero;
    for item: items {
        total += item.getPrice();
        receipt.addItem(item);
    }
    Payment p = acceptCash(total);
    receipt.addPayment(p);
}
```

Mieux :

```kotlin
public void checkOut(
    final Receipt receipt,
    final PaymentMethod paymentMethod
) {
    Money total = Money.zero;
    for item: items {
        total += item.getPrice();
        receipt.addItem(item);
    }
    Payment p = paymentMethod.acceptPayment(total);
    receipt.addPayment(p);
}
```

`PaymentMethod` est une interface, qui peut être implémentée de plein de façons. 

CCL

Dans un monde parfait, on ne fait que mettre à jour, sans modifier la codeBase. EN pratique, ce n'est pas realise, on a pas besoin de tout parametrer et respecter ce principe tout le temps.

### Liskov Substitution principle

*Une sous classe doit toujours pouvoir remplaçer sa classe de base*

Casser le LSP crée des comportement indefinits.

Exemple des carrés/rectanges

Les classes sont des représentation de la réalité. C'est pas parcequ'un carré est une forme de rectangle que la représentation d'un carré doit être une sous classe de la représentation d'un rectange.

```java
Class Integer: extends Real {}
Class Real: extends Complex {}
Class Complex {
    private Real real;
    private Real imaginary
}
```

Ne compile pas, un programe ne dois pas être une représentation de la réalité.

### Interface Segregation Principle

*Le client ne doit pas dépendre d'interfaces qu'il n'utilise pas*

Si on a un pb, on scinde l'interface

### Dependency inversion principle

Les abstractions ne doivent pas dépendre du détail, mais le detail doit dépendre des abstraction

Exemple: 

Description d'architecture qui permet d'affichier le classement des joueurs.

Module app : regroupe la vue et la présentation (présentation = vueModel & recuperation des interactions du )