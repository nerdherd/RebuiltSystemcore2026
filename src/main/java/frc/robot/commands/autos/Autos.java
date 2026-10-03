package frc.robot.commands.autos;

import frc.robot.subsystems.NerdDrivetrain;
import frc.robot.subsystems.SuperSystem;

import org.wpilib.command2.Command;
import org.wpilib.command2.Commands;
import org.wpilib.tunable.Selectable;
import org.wpilib.tunable.Tunables;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

public final class Autos {
    public static Selectable<Command> autoChooser = new Selectable<>();

    public static void initAutoChooser() {
        autoChooser.addDefault("Do Nothing", Commands.none());
        
        // autoChooser.add("Test", AutoBuilder.buildAuto("test"));

        // EXAMPLE
        // autoChooser.add("Auto Name", AutoBuilder.buildAuto("PathPlanner Auto Name"));

        // TOP
        // autoChooser.add("Top-S1Neutral2.5", AutoBuilder.buildAuto("Top-S1Neutral2.5"));
        autoChooser.add("Top-S1Neutral2.5 w Distance", AutoBuilder.buildAuto("Top-S1Neutral2.5 w Distance"));
        autoChooser.add("trench", AutoBuilder.buildAuto("trench"));

        // autoChooser.add("Top-S1Neutral3", AutoBuilder.buildAuto("Top-S1Neutral3"));
        // autoChooser.add("Top-S1MidDepot", AutoBuilder.buildAuto("Top-S1MidDepot"));

        // MID
        autoChooser.add("Mid-S3DepotTower", AutoBuilder.buildAuto("Mid-S3DepotTower"));
        autoChooser.add("Mid-S3OutpostTower", AutoBuilder.buildAuto("Mid-S3OutpostTower"));

        // autoChooser.add("Mid-S3DepotTower2", AutoBuilder.buildAuto("Mid-S3DepotTower2"));



        // BOT
        // autoChooser.add("Bot-S5Neutral2.5", AutoBuilder.buildAuto("Bot-S5Neutral2.5"));
        autoChooser.add("Bot-S5Neutral2.5 w Distance", AutoBuilder.buildAuto("Bot-S5Neutral2.5 w Distance"));


        // TEST
        // autoChooser.add("Top-S1Trench2.5", AutoBuilder.buildAuto("Top-S1Trench2.5"));
        // autoChooser.add("Bot-S5Trench2.5", AutoBuilder.buildAuto("Bot-S5Trench2.5"));

        Tunables.publish("Autos/Selected Auto", autoChooser);
    }

    public static void initNamedCommands(SuperSystem superSystem, NerdDrivetrain swerveDrive) {
        // SWERVE2
        NamedCommands.registerCommand("Reset Pose", swerveDrive.resetPoseWithAprilTags(0.2));

        // INTAKE
        NamedCommands.registerCommand("Intake Down", superSystem.intakeDownAuto());
        NamedCommands.registerCommand("Intake Down Only", superSystem.intakeDownOnlyAuto());
        NamedCommands.registerCommand("Intake Hold", superSystem.intakeHoldAuto());
        // NamedCommands.registerCommand("Intake Up", superSystem.intakeUp());
        NamedCommands.registerCommand("Intake Start", superSystem.intake());
        NamedCommands.registerCommand("Intake Stop", superSystem.stopIntaking());
        NamedCommands.registerCommand("Intake Hold Stop", superSystem.stopIntakeSlapdown());

        NamedCommands.registerCommand("Intake Down Sequence", 
            Commands.sequence(
                superSystem.intakeDownAuto(),
                superSystem.intake()
            ));
        NamedCommands.registerCommand("Auto Shoot Start", superSystem.startShootWithCondition());
        NamedCommands.registerCommand("Auto Shoot Stop", superSystem.stopShootWithCondition());
        // NamedCommands.registerCommand("Intake Up Sequence", 
        //     Commands.sequence(
        //         superSystem.stopIntaking(), 
        //         superSystem.intakeUp()
        //     ));

        // SHOOTER
        NamedCommands.registerCommand("Flywheel Start", superSystem.setFlywheelCommand(37));
        NamedCommands.registerCommand("Flywheel Start Distance", superSystem.startShootWithDistance());
        NamedCommands.registerCommand("Flywheel Stop Distance", superSystem.stopShootWithDistance());
        NamedCommands.registerCommand("Hood Flywheel Start Distance", superSystem.startHoodShootWithDistance());
        NamedCommands.registerCommand("Hood Flywheel Stop Distance", superSystem.stopHoodShootWithDistance());
        NamedCommands.registerCommand("Flywheel Start 0", superSystem.setFlywheelCommand(34));
        NamedCommands.registerCommand("Flywheel Start 45", superSystem.setFlywheelCommand(35.65));
        NamedCommands.registerCommand("Flywheel Start 60", superSystem.setFlywheelCommand(42.9));
        NamedCommands.registerCommand("Flywheel Stop", superSystem.stopFlywheelCommand());
        NamedCommands.registerCommand("Turn to Hub", superSystem.turnToHub(3.0));

        NamedCommands.registerCommand("Shoot", superSystem.startShootCommand());
        NamedCommands.registerCommand("Shoot Stop", superSystem.stopShootCommand());
        NamedCommands.registerCommand("Shoot Distance", superSystem.shootWithDistance());
        NamedCommands.registerCommand("Shoot Ramp Up", 
            Commands.sequence(
                superSystem.setFlywheelCommand(37), 
                Commands.waitSeconds(2),
                superSystem.startShootCommand()
            ));
            
        NamedCommands.registerCommand("Shoot Ramp Down", 
            Commands.sequence(
                superSystem.stopFlywheelCommand(),
                Commands.waitSeconds(1),
                superSystem.stopFlywheelCommand()
            ));
    }
    
}