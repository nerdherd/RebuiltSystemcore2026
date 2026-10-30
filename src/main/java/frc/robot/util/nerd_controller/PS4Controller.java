// VERSION 3.1
// Uses the generic CommandGamepad class

package frc.robot.util.nerd_controller;

import org.wpilib.command2.Commands;
import org.wpilib.command2.button.CommandNiDsPS4Controller;
import org.wpilib.command2.button.Trigger;
import org.wpilib.driverstation.POVDirection;

import dev.doglog.DogLog;

// thank you william
// you're welcome mason

// im wheler and i love duenas
// im also here hello
public class PS4Controller {
    private final CommandNiDsPS4Controller gamepad;
    private static final double triggerDeadband = 0.65;


    /**
     * Constructor for controller.<p>
     * @param port in Driver Station
     */
    public PS4Controller(int port) {
        gamepad = new CommandNiDsPS4Controller(port);
    }


    // ***** BUTTON METHODS ***** //

    public Trigger triggerLeft()        { return gamepad.L1();                   }
    public Trigger triggerRight()       { return gamepad.R1();                   }
    public Trigger bumperLeft()         { return gamepad.L2();                   }
    public Trigger bumperRight()        { return gamepad.R2();                   }
    public Trigger buttonUp()           { return gamepad.triangle();             }
    public Trigger buttonRight()        { return gamepad.circle();               }
    public Trigger buttonDown()         { return gamepad.cross();                }
    public Trigger buttonLeft()         { return gamepad.square();               }
    public Trigger dpadUp()             { return gamepad.getHID().povUp();       }
    public Trigger dpadRight()          { return gamepad.getHID().povRight();    }
    public Trigger dpadDown()           { return gamepad.getHID().povDown();     }
    public Trigger dpadLeft()           { return gamepad.getHID().povLeft();     }
    public Trigger joystickLeft()       { return gamepad.L3();                   }
    public Trigger joystickRight()      { return gamepad.R3();                   }
    public Trigger controllerLeft()     { return gamepad.share();                } 
    public Trigger controllerRight()    { return gamepad.options();              } 

    public double getLeftX()            { return gamepad.getLeftX();  }
    public double getLeftY()            { return gamepad.getLeftY();  }
    public double getRightX()           { return gamepad.getRightX(); }
    public double getRightY()           { return gamepad.getRightY(); }

    public boolean getTriggerLeft()     { return gamepad.getL2Axis() >= triggerDeadband;}
    public boolean getTriggerRight()    { return gamepad.getR2Axis() >= triggerDeadband;}  

    public boolean getBumperLeft()      { return gamepad.getNiDsPS4Controller().getL1Button();               }
    public boolean getBumperRight()     { return gamepad.getNiDsPS4Controller().getR1Button();               }    
    public boolean getButtonUp()        { return gamepad.getNiDsPS4Controller().getTriangleButton();         }
    public boolean getButtonRight()     { return gamepad.getNiDsPS4Controller().getCircleButton();           }
    public boolean getButtonDown()      { return gamepad.getNiDsPS4Controller().getCrossButton();            }
    public boolean getButtonLeft()      { return gamepad.getNiDsPS4Controller().getSquareButton();           }
    public boolean getDpadUp()          { return gamepad.getHID().getHID().getPOV() == POVDirection.UP;      }
    public boolean getDpadRight()       { return gamepad.getHID().getHID().getPOV() == POVDirection.RIGHT;   }
    public boolean getDpadDown()        { return gamepad.getHID().getHID().getPOV() == POVDirection.DOWN;    }
    public boolean getDpadLeft()        { return gamepad.getHID().getHID().getPOV() == POVDirection.LEFT;    }
    public boolean getJoystickLeft()    { return gamepad.getNiDsPS4Controller().getL3Button();               }
    public boolean getJoystickRight()   { return gamepad.getNiDsPS4Controller().getR3Button();               }
    public boolean getControllerLeft()  { return gamepad.getNiDsPS4Controller().getShareButton();            } 
    public boolean getControllerRight() { return gamepad.getNiDsPS4Controller().getOptionsButton();          } 
    

    // ***** STATE METHODS ***** //

    /**
     * returns analog value from left trigger
     * @return value on [0, 1]
     */
    public double getTriggerLeftAxis() {
        return gamepad.getL2Axis();
    }

    /**
     * returns analog value from right trigger
     * @return value on [0, 1]
     */
    public double getTriggerRightAxis() {
        return gamepad.getR2Axis();
    }

    // call once
    public static void configureDebugBindings(PS4Controller testController) {
        testController.buttonRight()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Right Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Right Test", "bye")));
        testController.buttonDown()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Down Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Down Test", "bye")));
        testController.buttonUp()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Up Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Up Test", "bye")));
        testController.buttonLeft()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Left Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Left Test", "bye")));

        testController.bumperLeft()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Bumper L Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Bumper L Test", "bye")));
        testController.bumperRight()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Bumper R Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Bumper R Test", "bye")));
        
        testController.triggerLeft()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Trigger L Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Trigger L Test", "bye")));
        testController.triggerRight()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Trigger R Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Trigger R Test", "bye")));

        testController.dpadUp()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Up Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Up Test", "bye")));
        testController.dpadRight()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Right Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Right Test", "bye")));
        testController.dpadDown()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Down Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Down Test", "bye")));
        testController.dpadLeft()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Left Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Dpad Left Test", "bye")));

        testController.controllerLeft()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Controller Left Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Controller Left Test", "bye")));
        testController.controllerRight()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Controller Right Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Controller Right Test", "bye")));
        
        testController.joystickLeft()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Left Joy Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Left Joy Test", "bye")));
        testController.joystickRight()
            .onTrue(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Right Joy Test", "hi")))
            .onFalse(Commands.runOnce(() -> DogLog.log("ControllerTest/Button Right Joy Test", "bye")));
    }

    // call repeatedly
    public void logAnalogValues() {
        DogLog.log("ControllerTest/Controller Joy Left X", getLeftX());
        DogLog.log("ControllerTest/Controller Joy Left Y", getLeftY());
        DogLog.log("ControllerTest/Controller Joy Right X", getRightX());
        DogLog.log("ControllerTest/Controller Joy Right Y", getRightY());

        DogLog.log("ControllerTest/Controller Left Trigger", getTriggerLeftAxis());
        DogLog.log("ControllerTest/Controller Right Trigger", getTriggerRightAxis());

        DogLog.log("ControllerTest/Right Button", getControllerRight());
    }
}
