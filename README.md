[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c5a4c050771f4ae19aa181abae7e0ea3)](https://www.codacy.com/app/djuelg/Neuronizer?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=djuelg/Neuronizer&amp;utm_campaign=Badge_Grade)

# Overview

This is the project for Neuronizer, the Todo App based on TodolistOfDeath.
It will be served with a new UI design and furthermore a new project design.
The architecture is based on "Clean Architecture".

## Links
* [color palette](http://htmlpreview.github.com/?https://github.com/djuelg/Neuronizer/blob/master/palette.html) used for UI
* based on this [github repository](https://github.com/dmilicic/Android-Clean-Boilerplate)
* [reference project](https://github.com/dmilicic/android-clean-sample-app) to steal code from
* more detailed guide of [boilerplate code](https://medium.com/@dmilicic/a-detailed-guide-on-developing-android-apps-using-the-clean-architecture-pattern-d38d71e94029)
* even more detailed [article](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/) about Clean Architecture

## Used Dependencies
* using [Realm](https://realm.io/docs/java/latest/) as database
* using [Stetho](https://github.com/uPhyca/stetho-realm) for database analysis
* using [FlexibleAdapter](https://github.com/davideas/FlexibleAdapter) for list rendering
* using [timber](https://github.com/JakeWharton/timber) for logging
* using [Butterknife](http://jakewharton.github.io/butterknife/) for ViewBinding

# Use Cases
Here are the different use cases for the whole app. 
They are grouped into 3 different independent parts.

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

* Import / Export Database
* Share Todo List
* Display About Page (App Intention, Data Privacy, Special Thanks)
* Display Markdown Help
* Introduction (Video or Slides)
* Show all Todolists in Burger Menu