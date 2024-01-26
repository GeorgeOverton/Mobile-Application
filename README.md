# README

### Android app by George Overton

## Introduction
The reason my app uses different activities is because it is not a small part of the UI that changes
between each screen but all of it which includes the menu bar
My app is a implementation of a timer app that will allow users to add timers to a recycler
view pausing and playing each timer as they please. They can add new timers via another activity by
pressing the plus button in the top right of the screen. Finally they can press the website button
in the overflow options menu to be taken to the rune lite website at
<a href="https://runelite.net/" target="https://runelite.net/">RuneLite</a>
Target API - API 30

## Design rationale
So i had to implement a recyclerview into my app as there was no other way to show multiple timers
of the same screen. Finding this out i realised there was a couple only a couple ways to tackle this
1)  Make each element in the recycler view into a card view and add eac component in the card
    view including buttons
2) Make each element a simple text view that will start as soon as it is made and just show a
   countdown of that timer

I opted for the first as it allowed the user to have more interaction with my app and visualise the
timer better.

To connect this to the internet i had multiple ideas:
1) Create an API request to the runelite api to retrieve data for a users timers and add it to
   the adapter
2) Allow the timer to have sounds when it finishes and add a option to play the radio
3) Add a option to allow the user to connect to the runelite website directly

I decided the later was better as it provided me a way to implement implicit intents into my code
while still connecting to the internet

Finally to keep the data in each activity i had to implement a mixture of Saved Instance States and
Shared Preferences this is because the shared preference gave me the functionality to keep a users
timers between sessions and so when they made new ones it would not delete the older timers. I then
also needed Saved Instance States as this allowed me to make sure that a timer was not added to the
adaptor twice if the screen was rotated.

## Novel Features
The application contains some nice feature to allow the user to have a easier time using it.
The first of which is a search algorithm to allow a user to search through the adaptor for the timer
they want instead of scrolling through all of them.
Secondly also contains a home screen to welcome you into the app

## Challanges
I have faced quite the handful of challenges from trying to implement a timer system into the app
via a broadcast service that did not work(i did this so they would continue running even when the
app was close). Then implementing them as an Async task this removes the benefit of them not running
when the app is closed but should have been easier but did not get it working. Then tha API i was
planning to use was out date so to get the new data i would have to learn how the runelite client
worked.I had problems with implementing Notifications however easily overcome them when i realised i
had to add it into the manifest.

To improve the app further I can add the following:
1) New images and colours to allow a toggle for dark mode
2) Allow a user to grab data from the API
3) Get the timers working when the user is not on the timer page(Use services)
4) Allow a user to change a timers name