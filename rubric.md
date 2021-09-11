# Rubric checklist
## Build a navigable interface consisting of multiple screens of functionality and data.
### Application includes at least three screens with distinct features using either the Android Navigation Controller or Explicit Intents.
The three screen are Main, Favorites and Settings (README.md). It is using navigation controller. 
### The Navigation Controller is used for Fragment-based navigation and intents are utilized for Activity-based navigation.
It has fragment-based navigation. It uses navigation controller (SpaceWallpaper/app/src/main/res/navigation/nav_graph.xml)
### An application bundle is built to store data passed between Fragments and Activities.
A bundle is used to send default image space from MainFragment to FavoritesFragment.

## Construct interfaces that adhere to Android standards and display appropriately on screens of different size and resolution.
### Application UI effectively utilizes ConstraintLayout to arrange UI elements effectively and efficiently across application features, avoiding nesting layouts and maintaining a flat UI structure where possible.
Constraint layout used in activity_main.xml, fragment_favorites.xml, fragment_main.xml and list_view_item.
### Data collections are displayed effectively, taking advantage of visual hierarchy and arrangement to display data in an easily consumable format.
In FavoritesFragment.
### Resources are stored appropriately using the internal res directory to store data in appropriate locations including string* values, drawables, colors, dimensions, and more.
In res folder.
### Every element within ConstraintLayout should include the id field and at least 1 vertical constraint.
Constraint layout elements in activity_main.xml, fragment_favorites.xml, fragment_main.xml and list_view_item.
### Data collections should be loaded into the application using ViewHolder pattern and appropriate View, such as RecyclerView.
RecyclerView used in fragment_favorites.xml.  The ViewHolder pattern is implemented in DataBindingViewHolder, BaseRecyclerViewAdapter, FavoritesListAdapter.
This was done getting code from previous projects I delivered in the course.

## Animate UI components to better utilize screen real estate and create engaging content.
### Application contains at least 1 feature utilizing MotionLayout to adapt UI elements to a given function. This could include animating control elements onto and off screen, displaying and hiding a form, or animation of complex UI transitions.

### MotionLayout behaviors are defined in a MotionScene using one or more Transition nodes and ConstraintSet blocks.

### Constraints are defined within the scenes and house all layout params for the animation.