# WeatherLogger
Displays current Weather Temperature in Fahrenheit based on current location

This is an Android application for fetching temperature at your location.

The app has a single screen with Toolbar

[1] Ask for Location Permission. (Deny condition is handled)

[2] Fetch Current Latitude and Longitude.

[3] Pass location details to Weather Api and fetch current Temperature.

[4] Save the data (Temperature details and APi request Time) in Database on Clicking Save icon from Toolbar.

[5] Fetch Data using LiveData from Room Db and display in RecyclerView

[6] On Clicking "More Details" show Min, Max and Current Temp as a Bar Graph Below.

[7] Internet check handled.

API Used : https://openweathermap.org/api

Networking Library: Retrofit

Permission : Internet and Location.

Design : MaterialDesign (RecyclerView and MaterialCardView used)

Layout: ConstraintLayout

Bar Graph: com.github.PhilJay:MPAndroidChart:v2.2.4

Device Compatibility: Mobile and Tablet devices.

Android Version : Android 4.1 to Android X

Launcher App Icon generation: https://appicon.co/
