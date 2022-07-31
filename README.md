## Description of project
This application is an energy related quiz game which is commissioned by the [OOPP team] from TU Delft. The back-end is written in `Java`, while the front-end uses `FXML`.
The quiz has 6 different question types; All of them are unique, and you can earn points for answering them correctly. The app also has a build-in message system to send
emotes, and there are 3 different joker types which can be used during the game. In the single player mode your name can end up on the leaderboard if your score is high enough, 
and the multiplayer game can be joined with as many people as you want. Feel free to contribute to the code and add new features to the project!

## How to run it 

- First make sure to have the following installed:
  - [Java](https://www.java.com/en/download/help/download_options.html)
  - An IDE, for example [Intellij](https://www.jetbrains.com/help/idea/installation-guide.html)
- Now, clone the project to a local directory and open it with your previous selected IDE
- To run the application, we need to do 2 things:
    - Add a configuration to run the server. In Intellij this can be done by going to `Edit configuration` -> `add new configuration` -> `gradle`.
    Now enter the following text in the `Run` field: `bootRun --stacktrace`. You can name the configuration `Server`.
    - Add a configuration to run the client. Follow the same steps as above, but now enter `run --stacktrace` in the `Run` field and name the configuration `Client`.
- Now everything is set up, and you should be ready to run the application. Always make sure to first start the server before running the client.

## Add activity bank

If you want to add activities to the application, you can do this by creating an activity bank. This is a file where all activities will be stored. 

### How to create an activity bank?
- Go to the following directory: ```server/src/main/recourses/ActivityBank/```
- Here you will create the activity bank folder. You can give it any name you like.
- Inside this folder you will store 2 different things:
    - The images from the activities
    - A `Json` file which stores all activity data named `activities.json`
  

### How to add activities to the activity bank?
- The format of the `Json` file will be as following:
```json
[
    {
        "id": "1-activity",
        "image_path": "activity.png",
        "title": "Doing an activity for 1 hour",
        "consumption_in_wh": 1000,
        "source": "www.source-to-activity-information.com"
    }
]
```

- You can add as many objects as you want, but make sure the image path is correct.
- All fields are a `strings`, except for the consumption; This will be an `integer`.    

### How to add the activity bank to the project?
If you already have an activity bank created which you would like to add to the project, this can be done by following the next few steps:
- Go to the following directory: ```server/src/main/recourses/ActivityBank/```
- Make sure the directory is empty
- Place the activity bank folder inside the directory

Now you can run the application and the questions will be generated from the activity bank.


<!-- Instructions (remove once assignment has been completed -->
<!-- - Add (only!) your own name to the table above (use Markdown formatting) -->
<!-- - Mention your *student* email address -->
<!-- - Preferably add a recognizable photo, otherwise add your GitLab photo -->
<!-- - (please make sure the photos have the same size) --> 

## Copyright
© OOPP Group 72 - 2021/2022
