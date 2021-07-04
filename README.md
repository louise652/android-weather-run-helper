# Weather Run Helper

A lightweight Android app which will give you recommendations on the best time to go for a run and
 what conditions to expect for your location, along with a recommendation for what to wear.

## Installation

Clone this repository and import into Anroid Studio

```git
git clone git@github.com:louise652/android-weather-run-helper.git
```

To connect to the openweatherAPI, you need to register and get a free api key, follow [these instructions.](https://openweathermap.org/appid)
Add the following line into the gradle.properties file.

```weatherKey=YOUR_API_KEY```


Then add the following to your build.grdle:app file under defaultConfig
 ```
  buildConfigField "String", "WEATHER_KEY", CONFIG("weatherKey")
  ```

 You can now safely use your API key by calling BuildConfig.WEATHER_KEY.
 Ensure you do not commit the change to gradle.properties as this will result in your key becoming public.

## Kanban Board
A Kanban board outlining future work and progress to date can be found [here](https://trello.com/b/GRX8n57o/weather-run-app)

## Screenshots
Screenshots of UI as of 3rd July 2021. Images will be updated as UI changes are committed.

![Weather Result Screen](screenshots/ScreenshotWeather.png)
![Clothes Suggestion Dialog](screenshots/ScreenshotSuggestion.png)
![Weather Settings Screen 1](screenshots/ScreenshotSettings1.png)
![Weather Settings Screen 2](screenshots/ScreenshotSettings2.png)