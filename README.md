[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c5a4c050771f4ae19aa181abae7e0ea3)](https://www.codacy.com/app/djuelg/Neuronizer?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=djuelg/Neuronizer&amp;utm_campaign=Badge_Grade)

# Overview

This is the project for Neuronizer, the Todo App based on TodolistOfDeath.
It will be served with a new UI design and furthermore a new project design.
The architecture is based on "Clean Architecture".

![App screenshots](/screenshots.png)

## Features
* Add, remove and update notes
* Add, remove and update ToDo lists
* Sort everything as you wish
* Designed for simple and fast usage
* Encrypted storage of data, on device only
* Create widgets
* Categorization in ToDo lists
* Mark tasks as done
* Add details to each task
* Share ToDo lists

## Upcoming features
* voice memo items in todo lists
* better hyperlink recognition
* pictures in details

## Used dependencies
* using [Realm](https://realm.io/docs/java/latest/) as database
* using [FlexibleAdapter](https://github.com/davideas/FlexibleAdapter) for list rendering
* using [Butterknife](http://jakewharton.github.io/butterknife/) for ViewBinding
* using [Arrow](https://github.com/android10/arrow) for more language features
* using [RichEditor](https://github.com/wasabeef/richeditor-android) for notes
* using [Clans-FAB](https://github.com/Clans/FloatingActionButton) for FAB menu
* using [Material Preference](https://github.com/consp1racy/android-support-preference) for material design preferences
* using [File Dialogs](https://github.com/RustamG/file-dialogs) for easier database import/export
* using [App Intro](https://github.com/apl-devs/AppIntro) for intro slides

## Other links
* [color palette](https://htmlpreview.github.com/?https://github.com/djuelg/Neuronizer/blob/master/palette.html) used for UI
* based on this [github repository](https://github.com/dmilicic/Android-Clean-Boilerplate)
* [reference project](https://github.com/dmilicic/android-clean-sample-app) to steal code from
* more detailed guide of [boilerplate code](https://medium.com/@dmilicic/a-detailed-guide-on-developing-android-apps-using-the-clean-architecture-pattern-d38d71e94029)
* even more detailed [article](https://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/) about Clean Architecture
* [widget tutorial](https://www.sitepoint.com/killer-way-to-show-a-list-of-items-in-android-collection-widget/) for ToDo list widget