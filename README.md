# tankvsplanes
A Java game that extends GraphicsProgram in acm project

https://github.com/user-attachments/assets/20fd9c03-ad6e-4ec3-a161-8de09d3a0268


## How to run the game
1. Download a Java IDE such as Eclipse
2. You need a 32-bit JDK to use Stanford ACM libraries as it. Download [JDK Temurin 8.0.452+9 - 4/20/2025](https://adoptium.net/temurin/releases/?os=any&arch=any&version=8) from Adoptium. In Eclipse, go to `Window` -> `Preferences` -> `Java` -> `Installed JREs` -> `Add` and select the downloaded zip as JRE source. Set it as the default JRE and click `Apply and Close`.
3. In Eclipse, go to `Window` -> `Preferences` -> `Java` -> `Compiler` and set the compliance level to `1.8`.
4. Create a new Java project and name it `game_plane`, for example. In the project creation panel, select `jdk8u452-b09` as the project-specific JRE.
5. Add the source code files to the project. Copy the files from the `src` folder of this repository into your project. Rename the package to `finalProject` under `src`. The main files you need are:
   - `finalProject/GameInterface.java`
   - `finalProject/Armament.java`
   - `finalProject/Bullet.java`
   - `finalProject/EnemyAircraft.java`
   - `finalProject/EnemyProjectile.java`
   - `finalProject/Sound.java`
   - `finalProject/Vehicle.java`
   Also put the `bin` folder and all contents inside it under `src`.
6. This project uses acm.graphics package from the [ACM library](https://cs.stanford.edu/people/eroberts/jtf/javadoc/student/), so you need to download it from [here](https://cs.stanford.edu/people/eroberts/jtf/). Download the `acm.jar` file. Then click on the project in Eclipse, go to `Build Path` -> `Add External Archives`, in the dialog, select the `acm.jar` file you just downloaded and click `Open`. The library will be added as a referenced library to your project.
7. In Eclipse, compile the project by clicking on the `Project` -> `Clean` and then `Project` -> `Build Automatically`.
8. Finally, run the game by going to `Run` -> `Run As` -> `Java Applet`. Select `finalProject.GameInterface` as the Applet to run. The game should start in a new window.