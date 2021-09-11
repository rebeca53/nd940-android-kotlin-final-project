# SpaceWallpaper
August 2nd, 2021
## Overview
The SpaceWallpaper app will change your wallpaper to a space image directly from NASA. Once a day, it is going to retrieve from NASA Api the so-called “Image of the day”. Then, it will update the wallpaper of your Android device.

## User Interface
- **Main screen:** TODO: Add image here.
- **Settings screen:** TODO: Add image here.
- **Favorites screen:** TODO: Add image here. 

## Requirements
1. App has the automatic mode, in which the wallpaper automatically, without asking the user. 
2. App also has the user-choice mode, in which the user is notified when there is a new wallpaper. Then, it can choose to apply the change or to decline it. 
3. User can choose how often the app will check for new images. 
4. User also can change the time in the day to check for new image. 
5. User can save an image by clicking the Favorite button.
6. The Favorite button has an animation. 
7. Plus: Animate Favorites list icon in sync with Favorite button.
8. All saved images are listed in the Favorites screen.
9. Plus: Saved images are stored locally up to a certain amount.
10. Favorites screen displays image of the day when no item is clicked. It receives this data using a bundle.
11. User can scroll the list of saved images.
12. User can click an image in the list to display the image in the top of the screen.
13. List item displays name of the image and day of creation.
14. List item has a Download button.
15. The app can be turned off by user.
16. User can download image of the day.
17. The Download button has an animation. 
18. Plus: Notify when download is done.

## Timeline
### 1. Design all screens
Create layouts. Implement navigation. 
Delivered in August 7. TAG: v0.1.0
### 2. Repository - Retrieve and store data from NASA Api
Get image of the day and additional data using API. Create database able to store image of the day. Create database able to store saved images. Test repository.
Delivered in August 9. TAG: v0.2.0
### 3. Implement Worker to retrieve image in background
Implement background Worker. Be able to receive changes in its settings. Store received data in database. Change wallpaper. 
Delivered in August 17. TAG: v0.3.0
### 4. Main Screen
Implement viewModel to store image of the day. Update image of the day. Implement favorite and download options.
Delivered in August 25. TAG:v0.4.0
### 5. Settings Screen
Implement viewModel to get periodicity, time of change and the “Notify before apply” option. Update service and change is applied. Trigger service using On/Off button. Delivered in August 29. TAG:v0.5.0
### 6. Notification to Update Image
Implement Notification to launch when service attempts to change image. Test notification, mock service.
### 7. Favorites Screen
Implement RecyclerView. Connect RecyclerView to Database. Get data from database (Repository). Implement download option. Test view model and fragment.
### 8. Animation
Animate favorite and download option in Main screen. Animate image on transition from Saved screen to Detail screen. 
### 9. Optional
Plus: Notify when download is done. Add Info button in Main Screen. Animate Favorite button in MainScreen in sync with the list icon. Test activity. When item is clicked in the list, open a drawer with explanation. Test service, mock Repository. Test all view model and fragment.

## Known issues
### Android compatibility. 
The buttons to save image and to download image in the main screen are not working in LG K9, Android 7. The application crashes.
Notification is not appearing in the exact scheduled time in LG K9, Android 7. It has 2 minutes of delay. Must be an issue with work manager, maybe try to replace it with a alarme service.
APK is using too much space in in LG K9, Android 7. Also, remember to add my name, and people involved in the About session.
Give some feedback on download progress.