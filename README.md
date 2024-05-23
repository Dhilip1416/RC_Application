# RC-App
# Controller App
  - The Controller App is an Android application that sends UDP messages to control a device. 
  - It features manual control through buttons and an automatic mode using a switch.
  - It also includes a scrollable list of sent commands and a clear button to reset the list.

## Features
  - Manual Control: Control the device using directional buttons (forward, backward, left, right, stop).
  - Auto-Forward Mode: Automatically send forward commands at regular intervals when the switch is turned on.
  - Scroll View: Displays the list of sent commands.
  - Clear Button: Clears the list of sent commands.
- Validation: Ensures that required input fields are filled before sending commands.
## Requirements
- Android Studio
- Android device or emulator
## Setup
  - Clone the repository:
     <pre>
      [ git clone https://github.com/Dhilip1416/RC-App](https://github.com/Dhilip1416/RC_Application.git)
     </pre>
     <pre>
       cd ControllerApp
     </pre>
      
  - Open the project in Android Studio.

  - Build and run the project on your Android device or emulator.

## Usage
  1. Manual Control:
  - Use the directional buttons to send commands.
  - The sent commands will be displayed in the scroll view.
  2. Auto-Forward Mode:
  - Turn on the switch to start auto-forward mode.
  - The app will send forward commands every second.
  3. Clear Button:
  - Press the clear button to clear the list of sent commands.
  - If there are no commands to clear, a toast message "Nothing to Clear" will be displayed.
    
## Code Overview
### Controller.java
  - Initialization:
    - Retrieves server IP and port from the intent.
    - Initializes UDP socket and server address.
    - Sets up the UI components (buttons, switch, scroll view).
      
  - setupButtons():
    - Configures the directional buttons to send UDP messages and add sent commands to the scroll view.
    - Validates the input fields before sending commands.
      
  - setupClearButton():
    - Configures the clear button to remove all views from the scroll view.
    - Displays a toast message if there are no commands to clear.
      
  - setupAutoSwitch():
    - Configures the switch to enable auto-forward mode.
    - Validates the input fields before enabling auto-forward mode.
    - Sends forward commands at regular intervals when the switch is on.
      
### Layout (activity_controller.xml)
  - Buttons: Directional buttons and a clear button.
  - EditTexts: Input fields for device parameters.
  - Scroll View: Displays the list of sent commands.
  - Switch: Toggles auto-forward mode.
    
## Contributing
Contributions are welcome! Please open an issue or submit a pull request for any improvements or bug fixes.

## License
This project is licensed under the MIT License - see the LICENSE file for details.
