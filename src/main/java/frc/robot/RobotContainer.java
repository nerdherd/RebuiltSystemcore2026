// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import static frc.robot.Constants.SwerveDriveConstants.kRobotOrientedVelocity;

import java.util.NoSuchElementException;

import dev.doglog.DogLog;
import org.wpilib.math.geometry.Rotation2d;
import org.wpilib.math.geometry.Translation2d;
import org.wpilib.networktables.StringSubscriber;
import org.wpilib.driverstation.MatchState;
import org.wpilib.driverstation.RobotState;
import org.wpilib.driverstation.Alliance;
import org.wpilib.hardware.bus.CANPort;
import org.wpilib.hardware.power.PowerDistribution;
import org.wpilib.system.RobotController;
import org.wpilib.hardware.power.PowerDistribution.ModuleType;
import org.wpilib.command2.Command;
import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.Commands;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.Constants.Subsystems;
import frc.robot.Constants.ZoneConstants;
import frc.robot.Constants.SwerveDriveConstants.FieldPositions;
import frc.robot.commands.SwerveJoystickCommand;
import frc.robot.commands.autos.Autos;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.NerdDrivetrain;
import frc.robot.subsystems.SuperSystem;
import frc.robot.util.nerd_controller.Controller;
import frc.robot.util.nerd_logging.NerdLog;
import frc.robot.util.nerd_logging.Reportable.LOG_LEVEL;

public class RobotContainer {
  public NerdDrivetrain swerveDrive;
  public PowerDistribution pdp = new PowerDistribution(CANPort.CAN_D0, 1, ModuleType.REV);
  
  public SuperSystem superSystem;

  private final Controller driverController = new Controller(ControllerConstants.kDriverControllerPort);
  private final Controller operatorController = new Controller(ControllerConstants.kOperatorControllerPort);
  private final Controller testController = new Controller(ControllerConstants.kTestControllerPort);
  
  private static boolean isRedSide = false;
  
  /**
   * The container for the robot. Contains
   * subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    swerveDrive = TunerConstants.createDrivetrain();
    
    if (Constants.USE_SUBSYSTEMS) { // add subsystems
      superSystem = new SuperSystem(swerveDrive);
      superSystem.initializeLEDs();
      // Autos.initNamedCommands(superSystem, swerveDrive);
    }
    
    Subsystems.init();
    // Autos.initAutoChooser();
    initializeLogging();

    NerdLog.reportInfo("Initialization Complete");
  }

  public static void refreshAlliance() {
    var alliance = MatchState.getAlliance();
    if (alliance.isPresent())
      isRedSide = (alliance.get() == Alliance.RED);
  }

  public static boolean IsRedSide() {
    return isRedSide;
  }

  public static double kOffset = 0.05;
  /**
   * Teleop commands configuration 
   * used in teleop mode.
   */
  public void initDefaultCommands_teleop() {
    SwerveJoystickCommand swerveJoystickCommand =
    new SwerveJoystickCommand(
      swerveDrive,
      // Horizontal Translation
      () -> -driverController.getLeftY(), 
      // Vertical Translation
      () -> -driverController.getLeftX(), 
      // Turn
      () -> -driverController.getRightX(), 
      // use turn to angle
      () -> driverController.getBumperRight(),
      // turn to angle target direction, 0.0 to use manual
      () -> swerveDrive.angleToLookAheadPose(FieldPositions.HUB_CENTER, ShooterConstants.kLookAheadFactor) + kOffset,
      // robot oriented adjustment (dpad)
      () -> new Translation2d(
        (((driverController.getDpadUp() && !driverController.getBumperRight()) ? 1 : 0) - (driverController.getDpadDown() ? 1 : 0)) * kRobotOrientedVelocity, 
        ((driverController.getDpadLeft() ? 1 : 0) - (driverController.getDpadRight() ? 1 : 0)) * 1.5)
        .rotateBy((!driverController.getBumperRight()) ? Rotation2d.ZERO : 
            Rotation2d.fromRadians(swerveDrive.angleToLookAheadPose(FieldPositions.HUB_CENTER, ShooterConstants.kLookAheadRingDriveFactor) - swerveDrive.angleToLookAheadPose(FieldPositions.HUB_CENTER, ShooterConstants.kLookAheadFactor) - kOffset)),
      // joystick drive field oriented
      () -> true, 
      // tow supplier
      () -> driverController.getBumperLeft(), 
      // precision/programmer mode :)
      () -> driverController.getTriggerLeftAxis()
    );
    
    swerveDrive.setDefaultCommand(swerveJoystickCommand);
  }

  public void initDefaultCommands_test() {
    swerveDrive.removeDefaultCommand();
    // initDefaultCommands_teleop();
  }

  public void configureBindings_teleop() {
    configureDriverBindings_teleop();
    configureOperatorBindings_teleop();
  }

  ///////////////////////
  // Driver bindings
  //////////////////////
  public void configureDriverBindings_teleop() {

    driverController.controllerLeft() // Set Drive Heading
      .onTrue(Commands.runOnce(() -> swerveDrive.setRobotHeadingForward()));

    driverController.controllerRight() // Set Pose Heading (pressed)
      .onTrue(Commands.runOnce(() -> swerveDrive.recalibrateGyroMT1()));

    // driverController.triggerLeft().whileTrue(new RingDriveCommand( // Ring Drive (held)
    //   swerveDrive,
    //   () -> -driverController.getRightY(), // Horizontal Translation
    //   () -> driverController.getLeftX() // Vertical Translation
    // ));

    if (Constants.USE_SUBSYSTEMS) {
      driverController.triggerRight()
        .onTrue(superSystem.intake())
        .onFalse(superSystem.stopIntaking());

      // driverController.buttonDown()
      //   .whileTrue(superSystem.shootWithTuning())
      //   .onFalse(superSystem.stopFlywheel());
      // driverController.buttonUp()
      //   .whileTrue(superSystem.shootWithDistance())
      //   .onFalse(superSystem.stopFlywheel());
      // driverController.buttonLeft()
      //   .whileTrue(superSystem.shootWithCondition())
      //   .onFalse(superSystem.stopShooting());

      // driverController.bumperLeft()
      //   .whileTrue(superSystem.climbUp())
      //   .onFalse(superSystem.stopClimb());
      // driverController.buttonRight()
      //   .whileTrue(superSystem.climbDown())
      //   .onFalse(superSystem.stopClimb());
    }
  }

  ///////////////////////
  // Operator bindings
  //////////////////////
  public void configureOperatorBindings_teleop() {

    if (Constants.USE_SUBSYSTEMS) {
      operatorController.controllerLeft()
        .onTrue(superSystem.intakeHoldTeleop());
        // .onFalse(superSystem.stopIntakeHold());
      operatorController.controllerRight()
        .onTrue(superSystem.intakeUp())
        .onFalse(superSystem.stopIntakeHold());
      operatorController.bumperLeft()
        .onTrue(superSystem.intake())
        .onFalse(superSystem.stopIntaking());

      operatorController.triggerRight()
        .whileTrue(superSystem.shootWithDistance())
        // .whileTrue(superSystem.shootWithTuning()) // USE ELASTIC
        // .onTrue(superSystem.spinUpFlywheel())
        .onFalse(superSystem.stopFlywheel());
      operatorController.triggerLeft()
        .whileTrue(superSystem.spinUpFlywheel())
        .onFalse(superSystem.stopFlywheel());
      operatorController.bumperRight()
        .whileTrue(superSystem.shootWithCondition())
        .onFalse(superSystem.stopShooting());
        
      operatorController.buttonUp()
        .onTrue(superSystem.spinUpFlywheelFeeding())
        .onFalse(superSystem.stopFlywheel());
      operatorController.buttonRight()
        .onTrue(superSystem.outtake())
        .onFalse(superSystem.stopIntaking());
      operatorController.buttonDown()
        .onTrue(superSystem.reverseConveyor())
        .onFalse(superSystem.stopConveyor());
      // hood testing
      operatorController.buttonLeft()
        .onTrue(superSystem.setShooterCommand(45))
        .onFalse(superSystem.stopFlywheel());

      operatorController.dpadDown()
        .onTrue(superSystem.setHood(0.5))
        .onFalse(superSystem.hoodDown());
      operatorController.dpadUp()
        .onTrue(superSystem.hoodUp())
        .onFalse(superSystem.hoodDown());
     }
  }

  public void configureBindings_test() {
    Controller.configureDebugBindings(testController);
  }

  public StringSubscriber printLog = null;
  public void initializeLogging() {
    if (printLog == null) printLog = DogLog.tunable("Print", "", (value) -> NerdLog.reportInfo("" + value));
    NerdLog.logData("Robot/PDP", pdp, LOG_LEVEL.ALL);
    
    swerveDrive.initializeLogging();
    if (Constants.USE_SUBSYSTEMS) { 
      superSystem.initializeLogging();
    }

    NerdLog.logData("Robot/Command Scheduler", CommandScheduler.getInstance(), LOG_LEVEL.MEDIUM);
    NerdLog.logNumber("Robot/RAM Usage", () -> (double)Runtime.getRuntime().freeMemory(), LOG_LEVEL.MEDIUM);
    NerdLog.logNumber("Match Info/Shift Time", () -> {shiftTime = allianceShiftTime(); return shiftTime;}, LOG_LEVEL.MINIMAL);
    NerdLog.logNumber("Robot/Battery Voltage", RobotController::getBatteryVoltage, LOG_LEVEL.MEDIUM);
    NerdLog.logBoolean("Robot/Shooting Zone", () -> ZoneConstants.kShootingGroup.check(swerveDrive.getPose()), LOG_LEVEL.MEDIUM);
    NerdLog.logBoolean("Robot/Passing Zone", () -> ZoneConstants.kLongPass.get().check(swerveDrive.getPose()), LOG_LEVEL.MEDIUM);
    NerdLog.reportLogCount();
    NerdLog.reportLogCount();
  }
  
  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return Autos.autoChooser.getSelected();
  }

  public void disableAllMotors_Test() {
    swerveDrive.setBrake(true);
  }

  private static boolean gameEnded = false;
  public static double shiftTime = 0.0;
  /**
   * Displays a countdown for alliance shifts. NOT 100% ACCURATE
   * @return the number of seconds in the current phase, and the phase name
   */
  public static double allianceShiftTime() {
    // if (!RobotState.isFMSAttached()) { DogLog.forceNT.log("Match Info/Shift Name", "DriverStation not attached"); return 0.0; };
    boolean wonAuto = true;
    if (Constants.ROBOT_LOG_LEVEL == LOG_LEVEL.MEDIUM) {
      try {
        String data = MatchState.getGameData().get();
        if (!data.isEmpty()) switch (data.charAt(0)) {
          case 'B': wonAuto = !isRedSide; break;
          case 'R': wonAuto = isRedSide; break;
          default: break;
        } 
      } catch (NoSuchElementException e) {}
      DogLog.log("Match Info/Won Auto?", wonAuto);
    }

    double time = MatchState.getMatchTime();
    DogLog.log("Match Info/time", time);

    if (RobotState.isAutonomous()) {
      if (time < 0.0) { DogLog.log("Match Info/Shift Name", (gameEnded) ? "Good Job Team!" : "Get Ready..."); return 0.0; }
      DogLog.log("Match Info/Shift Name", "Auto");
      gameEnded = false;
      return time;
    } else if (RobotState.isTeleop()) {
      if (time < 0.0) { DogLog.log("Match Info/Shift Name", (gameEnded) ? "Good Job Team!" : "Good Luck! -nerdherd"); return 0.0; }
      else if (time >= 130.0) { DogLog.log("Match Info/Shift Name", "Transition"); return time - 130; } // transition
      else if (time >= 30.0) { 
        int shift = (int)((130 - time) / 25) + 1; 
        DogLog.log("Match Info/Shift Name", "Shift " + shift + " " + (((shift % 2 == 1) == wonAuto) ? "Feeding" : "Scoring")); return (time - 30) % 25; 
      } // shifts 1-4
      else { DogLog.log("Match Info/Shift Name", "Endgame"); if (time <= 1.0) gameEnded = true; return time; } // endgame
    } else { DogLog.log("Match Info/Shift Name", "Inactive"); return 0.0; }
  }
}
