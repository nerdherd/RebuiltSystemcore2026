package frc.robot.commands.autos;

import org.wpilib.tunable.Selectable;
import org.wpilib.tunable.Tunables;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import org.wpilib.command2.Command;
import org.wpilib.command2.Commands;
import frc.robot.subsystems.SuperSystem;
import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;



public final class Autos {
    public static final AutoChooser autoChooser = new AutoChooser();
    public static Selectable<Command> pathPlannerAutoChooser = new Selectable<>(); 
    
    public static AutoFactory autoFactory;
    public SuperSystem superSystem;

    public Autos (SuperSystem superSystem) {
        this.superSystem = superSystem;
    }

    public void initAutoChooser() {
        pathPlannerAutoChooser.add("Top 2.5 w Distance", AutoBuilder.buildAuto(""));
        pathPlannerAutoChooser.add("Bot-S5Neutral2.5 w Distance", AutoBuilder.buildAuto("Bot-S5Neutral2.5 w Distance"));
        pathPlannerAutoChooser.add("trench only", AutoBuilder.buildAuto("trench"));
        pathPlannerAutoChooser.add("Mid-S3DepotTower", AutoBuilder.buildAuto("Mid-S3DepotTower"));
        pathPlannerAutoChooser.add("Mid-S3OutpostTower", AutoBuilder.buildAuto("Mid-S3OutpostTower"));



        autoChooser.addRoutine("Top 2.5 w Distance", this::TopDoubleSweep);
        autoChooser.addRoutine("Bottom 2.5 w Distance", this::BottomDoubleSweep);
        autoChooser.addRoutine("Top 3 Bump w Distance", this::TopBumpTripleSweep);


            
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

        // SWERVE2

        // INTAKE
        NamedCommands.registerCommand("Intake Down", superSystem.intakeDownAuto());
        NamedCommands.registerCommand("Intake Down Only", superSystem.intakeDownOnlyAuto());
        NamedCommands.registerCommand("Intake Hold", superSystem.intakeHoldAuto());
        // NamedCommands.registerCommand("Intake Up", superSystem.intakeUp());
        NamedCommands.registerCommand("Intake Start", superSystem.intake());
        NamedCommands.registerCommand("Intake Stop", superSystem.stopIntaking());

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
        NamedCommands.registerCommand("Flywheel Start", superSystem.shootWithDistance());
        NamedCommands.registerCommand("Flywheel Start Distance", superSystem.startShootWithDistance());
        NamedCommands.registerCommand("Flywheel Stop Distance", superSystem.stopShootWithDistance());
        NamedCommands.registerCommand("Flywheel Stop", superSystem.stopFlywheel());
        NamedCommands.registerCommand("Turn to Hub", superSystem.turnToHub(3.0));
        NamedCommands.registerCommand("Shoot Distance", superSystem.shootWithDistance());
    }

    public AutoRoutine TopDoubleSweep() {

        AutoRoutine topDoubleSweep = autoFactory.newRoutine("topDoubleSweep");

        AutoTrajectory first = topDoubleSweep.trajectory("TopSweep1");
        AutoTrajectory second = topDoubleSweep.trajectory("TopSweep2");
        AutoTrajectory third = topDoubleSweep.trajectory("TopLeave");


        topDoubleSweep.active().onTrue(
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

        return topDoubleSweep;
    }

    public AutoRoutine BottomAuto() {

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

    public AutoRoutine BottomDoubleSweep() {

        AutoRoutine bottomDoubleSweep = autoFactory.newRoutine("bottomDoubleSweep");

        AutoTrajectory first = bottomDoubleSweep.trajectory("BottomSweep1");
        AutoTrajectory second = bottomDoubleSweep.trajectory("BottomSweep2");
        AutoTrajectory third = bottomDoubleSweep.trajectory("BottomLeave");

        bottomDoubleSweep.active().onTrue(
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

        return bottomDoubleSweep;
    }

        public AutoRoutine TopBumpTripleSweep() {

        AutoRoutine topBumpTripleSweep = autoFactory.newRoutine("topBumpTripleSweep");

        AutoTrajectory first = topBumpTripleSweep.trajectory("TopS1Top3Bump_1");
        AutoTrajectory second = topBumpTripleSweep.trajectory("TopS1Top3Bump_2");
        AutoTrajectory third = topBumpTripleSweep.trajectory("TopS1Top3Bump_3");

        topBumpTripleSweep.active().onTrue(
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
                    superSystem.turnToHub(0.3), // TODO: test value
                    Commands.sequence(
                        Commands.waitSeconds(0.7),
                        superSystem.startShootCommand(),
                        Commands.waitSeconds(2.7), // TODO test value
                        superSystem.setFlywheelCommand(0),
                        superSystem.stopConveyor()  
                    )
                )
            ).andThen(third.cmd())
        );

        third.done().onTrue(
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
            )
        );

        return topBumpTripleSweep;
    }
}
