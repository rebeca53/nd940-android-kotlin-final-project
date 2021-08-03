# SpaceWallpaper
August 2nd, 2021
## Overview
The SpaceWallpaper app will change your wallpaper to a space image directly from NASA. Once a day, it is going to retrieve from NASA Api the so-called “Image of the day”. Then, it will update the wallpaper of your Android device.

## User Interface
- **Main screen:** TODO: Add image here.
- **Settings screen:** TODO: Add image here.
- **Saved screen:** TODO: Add image here. 
- **Detail screen:** TODO: Add image here.

## Requirements
1. App has the automatic mode, in which the wallpaper automatically, without asking the user. 
2. App also has the user-choice mode, in which the user is notified when there is a new wallpaper. Then, it can choose to apply the change or to decline it. 
3. User can choose how often the app will check for new images. 
4. User also can change the time in the day to check for new image. 
5. User can save an image by clicking the Favorite button.
6. The Favorite button has an animation. 
7. Plus: Animate Saved button in sync.
8. All saved images are listed in the Saved screen. 
9. All saved images are stored locally.
10. Saved screen has the image of the day fixed at the top.
11. Plus: Scroll the list in Saved screen will hide the image of the day at the top.
12. User can scroll the list of saved images.
13. User can click an image in the list to display more information about the clicked image in the Detail screen.
14. Detail screen displays image, name of the image and day of creation.
15. Detail screen has a Download button.
16. The app can be turned off by user.
17. User can download image of the day.
18. The Download button has an animation. 
19. Plus: Notify when download is done.
20. Detail screen displays the position of the image in the list. It receives this data using a bundle.

## Timeline
### 1. Design all screens
Create layouts. Implement navigation. 
### 2. Repository - Retrieve and store data from NASA Api
Get image of the day and additional data using API. Create database able to store image of the day. Create database able to store saved images. Test repository.
### 3. Implement Service to retrieve image in background
Implement background service. Be able to receive changes in its settings. Store received data in database. Test service, mock Repository.
### 4. Main Screen
Implement viewModel to store image of the day. Update image of the day. Trigger service using On/Off button. Implement favorite and download options. Test view model and fragment.
### 5. Settings Screen
Implement viewModel to get periodicity, time of change and the “Notify before apply” option. Update service and change is applied. Test view model and fragment.
### 6. Notification to Update Image
Implement Notification to launch when service attempts to change image. Test notification, mock service.
### 7. Saved Screen
Implement RecyclerView. Connect RecyclerView to Database. Get data from database (Repository). Test view model and fragment.
### 8. Detail Screen
Implement viewModel to get image detail. ViewModel retrieves data from repository. Implement download option. Test view model and fragment.
### 9. Animation
Animate favorite and download option in Main screen. Animate image on transition from Saved screen to Detail screen. 
### 10. Optional
Plus: Animate to hide image of the day when list is scrolled. Notify when download is done. Animate Saved button in sync. Test activity.
