# Rubric checklist
## Build a navigable interface consisting of multiple screens of functionality and data.
### 1. Application includes at least three screens with distinct features using either the Android Navigation Controller or Explicit Intents.
The three screen are Main, Favorites and Settings (README.md). It is using navigation controller. 
### 2. The Navigation Controller is used for Fragment-based navigation and intents are utilized for Activity-based navigation.
It has fragment-based navigation. It uses navigation controller (SpaceWallpaper/app/src/main/res/navigation/nav_graph.xml)
### 3. An application bundle is built to store data passed between Fragments and Activities.
A bundle is used to send default image space from MainFragment to FavoritesFragment.

## Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.
### 1. Application UI effectively utilizes ConstraintLayout to arrange UI elements effectively and efficiently across application features, avoiding nesting layouts and maintaining a flat UI structure where possible.
Constraint layout used in activity_main.xml, fragment_favorites.xml, fragment_main.xml and list_view_item.
### 2. Data collections are displayed effectively, taking advantage of visual hierarchy and arrangement to display data in an easily consumable format.
In FavoritesFragment.
### 3. Resources are stored appropriately using the internal res directory to store data in appropriate locations including string* values, drawables, colors, dimensions, and more.
In res folder.
### 4. Every element within ConstraintLayout should include the id field and at least 1 vertical constraint.
Constraint layout elements in activity_main.xml, fragment_favorites.xml, fragment_main.xml and list_view_item.
### 5. Data collections should be loaded into the application using ViewHolder pattern and appropriate View, such as RecyclerView.
RecyclerView used in fragment_favorites.xml.  The ViewHolder pattern is implemented in DataBindingViewHolder, BaseRecyclerViewAdapter, FavoritesListAdapter.
This was done getting code from previous projects I delivered in the course.

## Animate UI components to better utilize screen real estate and create engaging content.
### 1. Application contains at least 1 feature utilizing MotionLayout to adapt UI elements to a given function. This could include animating control elements onto and off screen, displaying and hiding a form, or animation of complex UI transitions.
There is MotionLayout in fragment_main to animate the animatedSaveButton.
### 2. MotionLayout behaviors are defined in a MotionScene using one or more Transition nodes and ConstraintSet blocks.
MotionScene described in fragment_main_scene
### 3. Constraints are defined within the scenes and house all layout params for the animation.
Done in fragment_main_scene

## Connect to and consume data from a remote data source such as a RESTful API.
### 1. The Application connects to at least 1 external data source using Retrofit or other appropriate library/component and retrieves data for use within the application.
Retrofit used in NASAApiService to retrieve picture of day from api nasa. Picture of day used in application.
### 2. Data retrieved from the remote source is held in local models with appropriate data types that are readily handled and manipulated within the application source. Helper libraries such as Moshi may be used to assist with this requirement.
Moshi used in NASAApiService. Data model is SpaceImage.
### 3. The application performs work and handles network requests on the appropriate threads to avoid stalling the UI.
The network request is done with io context, as it is in PictureOfDayLocalRepository.refreshPictureOfDay().

## Store data locally on the device for use between application sessions and/or offline use.
### 1. The application utilizes storage mechanisms that best fit the data stored to store data locally on the device. Example: SharedPreferences for user settings or an internal database for data persistence for application data. Libraries such as Room may be utilized to achieve this functionality.
Room library is used in LocalDB.kt to create internal database to persist application data. 
### 2. Data stored is accessible across user sessions.
???
### 3. Data storage operations are performed on the appropriate threads as to not stall the UI thread.
Using CoroutineScope to perform operations.
### 4. Data is structured with appropriate data types and scope as required by application functionality.
Data structuring is in data package.

## Architect application functionality using MVVM.
MVVM is applied to the application.
### 1. Application separates responsibilities amongst classes and structures using the MVVM Pattern:
### 2. Fragments/Activities control the Views, Models houses the data structures, ViewModel controls business logic.
### 3. Application adheres to architecture best practices, such as the observer pattern, to prevent leaking components, such as Activity Contexts, and efficiently utilize system resources.

## Implement logic to handle and respond to hardware and system events that impact the Android Lifecycle.
### Beyond MVVM, the application handles system events, such as orientation changes, application switching, notifications, and similar events gracefully including, but not limited to:
### Storing and restoring state and information
### Properly handling lifecycle events in regards to behavior and functionality
### Implement bundles to restore and save data
### Handling interaction to and from the application via Intents
### Handling Android Permissions

## Utilize system hardware to provide the user with advanced functionality and features.
### Application utilizes at least 1 hardware component to provide meaningful functionality to the application as a whole. Suggestion options include:
It utilizes Notifications.
### Permissions to access hardware features are requested at the time of use for the feature.
### Behaviors are accessed only after permissions are granted.