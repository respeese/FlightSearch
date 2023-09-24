# FlightSearch

Created by: **Ryan Speese**

## Summary

**Flight Search** is an android app that allows users to search for all the routes from a specific airport and favorite the routes that their interested in.

## Application Features

- Remember API for saving state across recompositions
- Reusable composable by using state hoisting and callback passing
- LazyColumn for displaying list of routes and airports
- Room library for the database holding the routes (uses the Repository Pattern for access)
- Material3 design and theming

## Future features

- A fragment to show a map of where the airports are located for a route upon clicking it
- Implicit intent to email user their favorite routes

## Video Demo

<img src='https://github.com/respeese/FlightSearch/blob/master/FlightSearch.gif' title='Video Demo' width='240' alt='Video Demo' />

## Notes

I used this project to learn about the Jetpack Compose basics. Specifically, using a Room database and state hoisting in Compose.

## License

Copyright **2023** **Ryan Speese**

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
