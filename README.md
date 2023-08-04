# Create: Tweaked Controllers

<img src="src/main/resources/logo.png" width="" height="100">

A mod that let you control your contraptions using a gamepad instead of your keyboard !

## Building

Go to the root of the project, and run  
```./gradlew build```  
to build the project  

The output is located here :  
```build/libs```  

## Using the mod

The tweaked controller requires a gamepad to work. You can check if your gamepad is detected inside the UI of the controller.  
If multiple gamepads are found, the game will select the first gamepad with any button activity.  
You can rescan for gamepads with the "Research" button at the bottom.  

## Remarks

Due to a Create limitation, if a network is updated too many times it will break. This can happen if you connect an axis output to an Adjustable Chain Gearshift, for example.  

The joystick axis are directly read from GLFW (the input/output library used by Minecraft). With most if not all gamepads, the +X axis points to the right, and the +Y axis points down.