// VERSION 3.1
// Uses the generic CommandGamepad class

package frc.robot.util.controller;

import org.wpilib.command2.Commands;
import org.wpilib.command2.button.CommandGamepad;
import org.wpilib.command2.button.Trigger;
import org.wpilib.telemetry.Telemetry;;
// thank you william
// you're welcome mason

// im wheler and i love duenas
// im also here hello
// "add some message here" ~ aadi
public class Controller {
    private final CommandGamepad gamepad;
    private static final double triggerDeadband = 0.65;


    /**
     * Constructor for controller.<p>
     * @param port in Driver Station
     */
    public Controller(int port) {
        gamepad = new CommandGamepad(port);
    }


    // ***** BUTTON METHODS ***** //

    public Trigger triggerLeft()        { return gamepad.leftTrigger();  }
    public Trigger triggerRight()       { return gamepad.rightTrigger(); }
    public Trigger bumperLeft()         { return gamepad.leftBumper();   }
    public Trigger bumperRight()        { return gamepad.rightBumper();  }
    public Trigger buttonUp()           { return gamepad.faceUp();    }
    public Trigger buttonRight()        { return gamepad.faceRight();     }
    public Trigger buttonDown()         { return gamepad.faceDown();    }
    public Trigger buttonLeft()         { return gamepad.faceRight();     }
    public Trigger dpadUp()             { return gamepad.dpadUp();       }
    public Trigger dpadRight()          { return gamepad.dpadRight();    }
    public Trigger dpadDown()           { return gamepad.dpadDown();     }
    public Trigger dpadLeft()           { return gamepad.dpadLeft();     }
    public Trigger joystickLeft()       { return gamepad.leftStick();    }
    public Trigger joystickRight()      { return gamepad.rightStick();   }
    public Trigger controllerLeft()     { return gamepad.back();        } // TODO: this is probably wrong
    public Trigger controllerRight()    { return gamepad.start();        } // TODO: this is probably wrong

    public double getLeftX()            { return gamepad.getLeftX();  }
    public double getLeftY()            { return gamepad.getLeftY();  }
    public double getRightX()           { return gamepad.getRightX(); }
    public double getRightY()           { return gamepad.getRightY(); }

    public boolean getTriggerLeft()     { return gamepad.getLeftTrigger() >= triggerDeadband;}
    public boolean getTriggerRight()    { return gamepad.getRightTrigger() >= triggerDeadband;}  

    public boolean getBumperLeft()      { return gamepad.getHID().getLeftBumperButton();  }
    public boolean getBumperRight()     { return gamepad.getHID().getRightBumperButton(); }    
    public boolean getButtonUp()        { return gamepad.getHID().getNorthFaceButton();   }
    public boolean getButtonRight()     { return gamepad.getHID().getEastFaceButton();    }
    public boolean getButtonDown()      { return gamepad.getHID().getSouthFaceButton();   }
    public boolean getButtonLeft()      { return gamepad.getHID().getWestFaceButton();    }
    public boolean getDpadUp()          { return gamepad.getHID().getDpadUpButton();      }
    public boolean getDpadRight()       { return gamepad.getHID().getDpadRightButton();   }
    public boolean getDpadDown()        { return gamepad.getHID().getDpadDownButton();    }
    public boolean getDpadLeft()        { return gamepad.getHID().getDpadLeftButton();    }
    public boolean getJoystickLeft()    { return gamepad.getHID().getLeftStickButton();   }
    public boolean getJoystickRight()   { return gamepad.getHID().getRightStickButton();  }
    public boolean getControllerLeft()  { return gamepad.getHID().getBackButton();       } // TODO: this is probably wrong
    public boolean getControllerRight() { return gamepad.getHID().getStartButton();       } // TODO: this is probably wrong
    

    // ***** STATE METHODS ***** //

    /**
     * returns analog value from left trigger
     * @return value on [0, 1]
     */
    public double getTriggerLeftAxis() {
        return gamepad.getLeftTrigger();
    }

    /**
     * returns analog value from right trigger
     * @return value on [0, 1]
     */
    public double getTriggerRightAxis() {
        return gamepad.getRightTrigger();
    }

    public static void configureDebugBindings(Controller testController) {
        testController.buttonRight()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Button Right Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Button Right Test", "bye")));
        testController.buttonDown()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Button Down Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Button Down Test", "bye")));
        testController.buttonUp()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Button Up Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Button Up Test", "bye")));
        testController.buttonLeft()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Button Left Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Button Left Test", "bye")));

        testController.bumperLeft()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Bumper L Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Bumper L Test", "bye")));
        testController.bumperRight()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Bumper R Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Bumper R Test", "bye")));
        
        testController.triggerLeft()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Trigger L Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Trigger L Test", "bye")));
        testController.triggerRight()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Trigger R Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Trigger R Test", "bye")));

        testController.dpadUp()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Dpad Up Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Dpad Up Test", "bye")));
        testController.dpadRight()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Dpad Right Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Dpad Right Test", "bye")));
        testController.dpadDown()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Dpad Down Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Dpad Down Test", "bye")));
        testController.dpadLeft()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Dpad Left Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Dpad Left Test", "bye")));

        testController.controllerLeft()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Controller Left Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Controller Left Test", "bye")));
        testController.controllerRight()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Controller Right Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Controller Right Test", "bye")));
        
        testController.joystickLeft()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Button Left Joy Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Button Left Joy Test", "bye")));
        testController.joystickRight()
            .onTrue(Commands.runOnce(() -> Telemetry.log("Button Right Joy Test", "hi")))
            .onFalse(Commands.runOnce(() -> Telemetry.log("Button Right Joy Test", "bye")));
    }

    public void logAnalogValues() {
        Telemetry.log("Controller Joy Left X", getLeftX());
        Telemetry.log("Controller Joy Left Y", getLeftY());
        Telemetry.log("Controller Joy Right X", getRightX());
        Telemetry.log("Controller Joy Right Y", getRightY());

        Telemetry.log("Controller Left Trigger", getTriggerLeftAxis());
        Telemetry.log("Controller Right Trigger", getTriggerRightAxis());

        Telemetry.log("Right Button", getControllerRight());
    }
}
