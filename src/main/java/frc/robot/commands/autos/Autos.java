package frc.robot.commands.autos;

import org.wpilib.command2.Command;
import org.wpilib.command2.Commands;
import org.wpilib.tunable.Selectable;
import org.wpilib.tunable.Tunables;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.path.EventMarker;
import com.pathplanner.lib.path.EventMarker;

import frc.robot.subsystems.SuperSystem;


public final class Autos {
    public static final Selectable<Command> autoChooser = new Selectable<Command>();

    public static void initAutoChooser() {

        //top
        autoChooser.add("Top-S1Neutral2.5 w Distance", AutoBuilder.buildAuto("Top-S1Neutral2.5 w Distance"));
        autoChooser.add("Top-S1Neutral2.5 w Event", AutoBuilder.buildAuto("Top-S1Neutral2.5 w Event"));

        autoChooser.add("trench", AutoBuilder.buildAuto("trench"));

        //mid
        autoChooser.add("Mid-S3DepotTower", AutoBuilder.buildAuto("Mid-S3DepotTower"));
        autoChooser.add("Mid-S3OutpostTower", AutoBuilder.buildAuto("Mid-S3OutpostTower"));

        //bottom
        autoChooser.add("Bot-S5Neutral2.5 w Distance", AutoBuilder.buildAuto("Bot-S5Neutral2.5 w Distance"));
        autoChooser.add("Bot-S1Neutral2.5 w Event", AutoBuilder.buildAuto("Bot-S1Neutral2.5 w Event"));


        Tunables.publish("Autos/Chooser", autoChooser);
    }

    public static void initBindings(SuperSystem superSystem) {

        // intake rollers and intake slapdown
        NamedCommands.registerCommand("Intake Down", superSystem.intakeDownAuto());
        NamedCommands.registerCommand("Intake Down Only", superSystem.intakeDownOnlyAuto());
        NamedCommands.registerCommand("Intake Hold", superSystem.intakeHoldAuto());
        NamedCommands.registerCommand("Intake Start", superSystem.intake());
        NamedCommands.registerCommand("Intake Stop", superSystem.stopIntaking());
        NamedCommands.registerCommand("Intake Down Sequence", Commands.sequence(superSystem.intakeDownOnlyAuto(), Commands.waitSeconds(0.4), superSystem.intakeHoldAuto()));
        
        // indexer and conveyor
        NamedCommands.registerCommand("Auto Shoot Start", superSystem.startShootWithCondition());
        NamedCommands.registerCommand("Auto Shoot Stop", superSystem.stopShootWithCondition());

        // flywheel
        NamedCommands.registerCommand("Flywheel Start", superSystem.shootWithDistance());
        NamedCommands.registerCommand("Flywheel Start Distance", superSystem.startShootWithDistance());
        NamedCommands.registerCommand("Flywheel Stop Distance", superSystem.stopShootWithDistance());
        NamedCommands.registerCommand("Flywheel Stop", superSystem.stopFlywheel());
        NamedCommands.registerCommand("Turn to Hub", superSystem.turnToHub(3.0));
        NamedCommands.registerCommand("Shoot Distance", superSystem.shootWithDistance());

        
    }
   
}
