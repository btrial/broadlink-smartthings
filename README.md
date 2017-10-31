A simple device handler to have a double momentary button, for sending commands to broadlink RM device.

As broadlink device doesn't return the state, you never know if your device is indeed on or off.

Use case 1:
You try to turn on your device using broadlink and ST, and it failed.

Use case 2:
You turned ON your device using ST and broadlink, and later you turned it off using physical switch.

In both cases above, normal 2 state Virtual switch, would stay in ON state. 
So if you want to press ON again, you cannot do it as it is already ON. So you need to press OFF and then ON.

With 2 momentary button, it is not needed, as the state is not kept on buttons.

