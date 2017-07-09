# Overview

This is the project for Neuronizer, the Todo App based on TodolistOfDeath.
It will be served with a new UI design and furthermore a new project design.
The architecture is based on "Clean Architecture".

## Links

* based on this [github repository](https://github.com/dmilicic/Android-Clean-Boilerplate)
* [reference project](https://github.com/dmilicic/android-clean-sample-app) to steal code
* more detailed guide of [boilerplate code](https://medium.com/@dmilicic/a-detailed-guide-on-developing-android-apps-using-the-clean-architecture-pattern-d38d71e94029)
* even more detailed [article](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/) about Clean Architecture
* using [timber](https://github.com/JakeWharton/timber) for logging
* using [Butterknife](http://jakewharton.github.io/butterknife/) for ViewBinding

# Use Cases

## Landingpage Use Cases

* Display Todolists
* Add, remove, edit Todolist

## Single Todolist Use Cases

* Display Todolist Headers and Items
    * Headers are collapsible
    * Headers can have different colors
    * Items can be important
    * Items can have notes or a deadline
* Display Details and Deadlines
* Display Todolist Widget 
* Add, remove, edit Todolist Header
* Add, remove, edit Todolist Item
* Mark Item as read

## Other Use Cases

* Share Todo List
* Display About Page
* Display Markdown Help
* Show all Todolists in Burger Menu