package frc.robot.commands.autos;

import org.wpilib.tunable.Tunables;
import org.wpilib.command2.Command;
import org.wpilib.command2.Commands;
import frc.robot.subsystems.SuperSystem;
import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;


public final class Autos {
    public static final AutoChooser autoChooser = new AutoChooser();
    public static AutoFactory autoFactory;
    public SuperSystem superSystem;

    public Autos (SuperSystem superSystem) {
        this.superSystem = superSystem;
    }

    public void initAutoChooser() {


        autoChooser.addRoutine("Top 2.5 w Distance", this::TopAuto);


            
    //     autoChooser.setDefaultOption("Do Nothing", Commands.none());
        
    //     // autoChooser.addOption("Test", AutoBuilder.buildAuto("test"));

    //     // EXAMPLE
    //     // autoChooser.addOption("Auto Name", AutoBuilder.buildAuto("PathPlanner Auto Name"));

    //     // TOP
    //     // autoChooser.addOption("Top-S1Neutral2.5", AutoBuilder.buildAuto("Top-S1Neutral2.5"));
    //     autoChooser.addOption("Top-S1Neutral2.5 w Distance", AutoBuilder.buildAuto("Top-S1Neutral2.5 w Distance"));
    //     autoChooser.addOption("trench", AutoBuilder.buildAuto("trench"));

    //     // autoChooser.addOption("Top-S1Neutral3", AutoBuilder.buildAuto("Top-S1Neutral3"));
    //     // autoChooser.addOption("Top-S1MidDepot", AutoBuilder.buildAuto("Top-S1MidDepot"));

    //     // MID
    //     // autoChooser.addOption("Mid-S3DepotTower", AutoBuilder.buildAuto("Mid-S3DepotTower"));
    //     // autoChooser.addOption("Mid-S3DepotTower2", AutoBuilder.buildAuto("Mid-S3DepotTower2"));


    //     // BOT
    //     // autoChooser.addOption("Bot-S5Neutral2.5", AutoBuilder.buildAuto("Bot-S5Neutral2.5"));
    //     autoChooser.addOption("Bot-S5Neutral2.5 w Distance", AutoBuilder.buildAuto("Bot-S5Neutral2.5 w Distance"));


    //     // TEST
    //     // autoChooser.addOption("Top-S1Trench2.5", AutoBuilder.buildAuto("Top-S1Trench2.5"));
    //     // autoChooser.addOption("Bot-S5Trench2.5", AutoBuilder.buildAuto("Bot-S5Trench2.5"));

        Tunables.publish("Autos/Chooser", autoChooser);

    }

    public void initNamedCommands() {
            autoFactory = new AutoFactory(
                superSystem.swerveDrivetrain::getPose, 
                superSystem.swerveDrivetrain::resetPose, 
                superSystem.swerveDrivetrain::followTrajectory, 
                true, superSystem.swerveDrivetrain);

            autoFactory.bind("Intake Down Sequence", Commands.sequence(superSystem.intakeDownOnlyAuto(), Commands.waitSeconds(0.25), superSystem.intakeHoldAuto()));
            
            autoFactory.bind("Intake Start", superSystem.intake());

            autoFactory.bind("Intake Stop", superSystem.stopIntaking());

            autoFactory.bind("Flywheel Start", superSystem.shootWithDistance());

    //     // SWERVE2
    //     NamedCommands.registerCommand("Reset Pose", swerveDrive.resetPoseWithAprilTags(0.2));

    //     // INTAKE
    //     NamedCommands.registerCommand("Intake Down", superSystem.intakeDown());
    //     NamedCommands.registerCommand("Intake Down Only", superSystem.intakeDownOnly());
    //     NamedCommands.registerCommand("Intake Hold", superSystem.intakeHold());
    //     // NamedCommands.registerCommand("Intake Up", superSystem.intakeUp());
    //     NamedCommands.registerCommand("Intake Start", superSystem.intake());
    //     NamedCommands.registerCommand("Intake Stop", superSystem.stopIntaking());
    //     NamedCommands.registerCommand("Intake Hold Stop", superSystem.stopIntakeHold());

    //     NamedCommands.registerCommand("Intake Down Sequence", 
    //         Commands.sequence(
    //             superSystem.intakeDown(),
    //             superSystem.intake()
    //         ));
    //     NamedCommands.registerCommand("Auto Shoot Start", superSystem.startShootWithCondition());
    //     NamedCommands.registerCommand("Auto Shoot Stop", superSystem.stopShootWithCondition());
    //     // NamedCommands.registerCommand("Intake Up Sequence", 
    //     //     Commands.sequence(
    //     //         superSystem.stopIntaking(), 
    //     //         superSystem.intakeUp()
    //     //     ));

    //     // SHOOTER
    //     NamedCommands.registerCommand("Flywheel Start", superSystem.spinUpFlywheel());
    //     NamedCommands.registerCommand("Flywheel Start Distance", superSystem.startShootWithDistance());
    //     NamedCommands.registerCommand("Flywheel Stop Distance", superSystem.stopShootWithDistance());
    //     NamedCommands.registerCommand("Hood Flywheel Start Distance", superSystem.startHoodShootWithDistance());
    //     NamedCommands.registerCommand("Hood Flywheel Stop Distance", superSystem.stopHoodShootWithDistance());
    //     NamedCommands.registerCommand("Flywheel Start 0", superSystem.spinUpFlywheel(34));
    //     NamedCommands.registerCommand("Flywheel Start 45", superSystem.spinUpFlywheel(35.65));
    //     NamedCommands.registerCommand("Flywheel Start 60", superSystem.spinUpFlywheel(42.9));
    //     NamedCommands.registerCommand("Flywheel Stop", superSystem.stopFlywheel());
    //     NamedCommands.registerCommand("Turn to Hub", superSystem.turnToHub(3.0));

    //     NamedCommands.registerCommand("Shoot", superSystem.shoot());
    //     NamedCommands.registerCommand("Shoot Stop", superSystem.stopShooting());
    //     NamedCommands.registerCommand("Shoot Distance", superSystem.shootWithDistance());
    //     NamedCommands.registerCommand("Shoot Ramp Up", 
    //         Commands.sequence(
    //             superSystem.spinUpFlywheel(), 
    //             Commands.waitSeconds(2),
    //             superSystem.shoot()
    //         ));
            
    //     NamedCommands.registerCommand("Shoot Ramp Down", 
    //         Commands.sequence(
    //             superSystem.stopShooting(),
    //             Commands.waitSeconds(1),
    //             superSystem.stopFlywheel()
    //         ));
    }

    public AutoRoutine TopAuto() {

        AutoRoutine topAuto = autoFactory.newRoutine("topAuto");

        AutoTrajectory first = topAuto.trajectory("Sweep1");
        AutoTrajectory second = topAuto.trajectory("Sweep2");
        AutoTrajectory third = topAuto.trajectory("leave");

        topAuto.active().onTrue(
            Commands.sequence(
                first.resetOdometry(),
                first.cmd()
            )
        );

        first.done().onTrue(
            Commands.sequence(
                Commands.parallel(
                    superSystem.turnToHub(0.7),
                    Commands.sequence(
                        Commands.waitSeconds(0.7),
                        superSystem.startShootCommand(),
                        Commands.waitSeconds(2.3),
                        superSystem.setFlywheelCommand(0),
                        superSystem.stopConveyor()  
                    )
                )
            ).andThen(second.cmd())
        );




        second.done().onTrue(
            Commands.sequence(
                Commands.parallel(
                    superSystem.turnToHub(0.7),
                    Commands.sequence(
                        Commands.waitSeconds(0.7),
                        superSystem.startShootCommand(),
                        Commands.waitSeconds(2.3),
                        superSystem.setFlywheelCommand(0),
                        superSystem.stopConveyor()  
                    )
                )
            ).andThen(third.cmd())
        );

        return topAuto;
    }

    public Command TopAutoCommand() {

        return Commands.sequence(
            autoFactory.resetOdometry("Sweep1"), 
            Commands.parallel(
                
            )

        );
    }
    
}
