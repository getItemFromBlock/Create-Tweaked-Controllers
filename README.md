# Create: Tweaked Controllers

<img src="src/main/resources/logo.png" width="" height="100">

A [Create](https://github.com/Creators-of-Create/Create) mod addon that let you control your contraptions using a gamepad instead of your keyboard !

## Building

First of all, you will need a JDK (java development kit) version 17 to build this mod.  
You can check your Java version by running the command  
```java --version```  

Now go to the root of the project, and run  
```./gradlew build```  
to build the project  

The output is located here :  
```build/libs```  

## Using the mod

The tweaked controller requires a gamepad to work. You can check if your gamepad is detected inside the UI of the controller.  
If multiple gamepads are found, the game will select the first gamepad with any button activity.  
You can rescan for gamepads with the "Research" button at the bottom.  

## Supported gamepads

This mod relies on GLFW (the input/output library used by Minecraft) to handle gamepad inputs.  
GLFW handle gamepads as follows:  
- If it is detected by GLFW, it will be labelled as a joystick; it will have all of its axis and buttons stored in an unorganised way (the way the driver read the values)  
- If it has a known mapping, GLFW will also register it as a gamepad. In this situations there will always be 6 axis ant 15 buttons which should be roughly the same on all gamepads (note that on some gamepads the A/B-X/Y buttons are inverted, and the two back triggers axis can just be shoulder buttons)  
  
For now only GLFW gamepads works in the mod, support for joystick is planed for later.  
Here is a list of supported gamepads:  
- Official gamepads such as Xbox controllers or Playstation DualShock are supported
- Knockoff/alternate gamepads (like a Logitech controller) should be supported
- Joysticks/hotas/racing wheels and other devices are probably not supported for now
- WiiMotes are (sadly) not supported

If you really want to try the mod with a joystick, you can try using an emulator such as [this one](https://github.com/x360ce/x360ce).

## Remarks

Due to a Create limitation, if a network is updated too many times it will break. This can happen if you connect an axis output to an Adjustable Chain Gearshift, for example.  

The joystick axis are directly read from GLFW. With most if not all gamepads, the +X axis points to the right, and the +Y axis points down.