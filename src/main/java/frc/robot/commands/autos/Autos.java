package frc.robot.commands.autos;

import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.Commands;
import org.wpilib.tunable.Tunables;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
import choreo.auto.AutoTrajectory;
import frc.robot.subsystems.SuperSystem;

public final class Autos {
    public static final AutoChooser autoChooser = new AutoChooser();
    
    public static AutoFactory autoFactory;
    public SuperSystem superSystem;

    public Autos (SuperSystem superSystem) {
        this.superSystem = superSystem;
        autoFactory = new AutoFactory(
            superSystem.swerveDrivetrain::getPose, 
            superSystem.swerveDrivetrain::resetPose, 
            superSystem.swerveDrivetrain::followTrajectory, 
            true, superSystem.swerveDrivetrain);
        CommandScheduler.getInstance().schedule(autoFactory.warmupCmd());
    }

    public void initAutoChooser() {
        autoChooser.addRoutine("Top 2.5 w Distance", this::TopDoubleSweep);
        // autoChooser.addRoutine("Top 2.5 w Distance", this::TopDoubleSweepDelayed);
        // autoChooser.addRoutine("Bottom 2.5 w Distance", this::BottomDoubleSweep);
        // autoChooser.addRoutine("Top 3 Bump w Distance", this::TopBumpTripleSweep);
        autoChooser.addRoutine("Middle Depot", this::Depot);
        autoChooser.addRoutine("Middle Outpost", this::Outpost);

        Tunables.publish("Autos/Chooser", autoChooser);
    }

    public void initBindings() {
        // intake rollers and intake slapdown
        autoFactory
            .bind("Intake Down", superSystem.intakeDownAuto())
            .bind("Intake Down Only", superSystem.intakeDownOnlyAuto())
            .bind("Intake Hold", superSystem.intakeHoldAuto())
            .bind("Intake Start", superSystem.intake())
            .bind("Intake Stop", superSystem.stopIntaking())
            .bind("Intake Down Sequence", Commands.sequence(superSystem.intakeDownOnlyAuto(), Commands.waitSeconds(0.25), superSystem.intakeHoldAuto()));
        
        // indexer and conveyor
        autoFactory
            .bind("Auto Shoot Start", superSystem.startShootWithCondition())
            .bind("Auto Shoot Stop", superSystem.stopShootWithCondition());

        // flywheel
        autoFactory
            .bind("Flywheel Start", superSystem.shootWithDistance())
            .bind("Flywheel Start Distance", superSystem.startShootWithDistance())
            .bind("Flywheel Stop Distance", superSystem.stopShootWithDistance())
            .bind("Flywheel Stop", superSystem.stopFlywheel())
            .bind("Turn to Hub", superSystem.turnToHub(3.0))
            .bind("Shoot Distance", superSystem.shootWithDistance());
    }
    public AutoRoutine Depot() {

        AutoRoutine depot = autoFactory.newRoutine("depot");

        AutoTrajectory first = depot.trajectory("Depot");

        depot.active().onTrue(
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
            )
        );

        return depot;
    }
    public AutoRoutine TopDoubleSweepDelayed() {

        AutoRoutine topDoubleSweepDelayed = autoFactory.newRoutine("topDoubleSweepDelayed");

        AutoTrajectory first = topDoubleSweepDelayed.trajectory("TopSweep1Delayed");
        AutoTrajectory second = topDoubleSweepDelayed.trajectory("TopSweep2Delayed");
        AutoTrajectory third = topDoubleSweepDelayed.trajectory("TopLeaveDelayed");

        topDoubleSweepDelayed.active().onTrue(
            Commands.sequence(
                first.resetOdometry(),
                first.cmd()
            )
        );

        first.done().onTrue(
            Commands.sequence(
                Commands.waitSeconds(3),
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

        return topDoubleSweepDelayed;
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
    public AutoRoutine TopDoubleSweep() {

        AutoRoutine topDoubleSweep = autoFactory.newRoutine("topDoubleSweep");

        AutoTrajectory first = topDoubleSweep.trajectory("TopDoubleSweep_1");
        AutoTrajectory second = topDoubleSweep.trajectory("TopDoubleSweep_2");
        AutoTrajectory third = topDoubleSweep.trajectory("TopDoubleSweep_3");

        topDoubleSweep.active().onTrue(
            Commands.sequence(
                first.resetOdometry(),
                Commands.parallel(
                    Commands.sequence(
                        Commands.waitSeconds(0.4),
                        first.cmd()
                    ), 
                    Commands.sequence(superSystem.intakeDownOnlyAuto(), Commands.waitSeconds(0.25), superSystem.intakeHoldAuto())

                )
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

        public AutoRoutine Outpost() {

        AutoRoutine outpost = autoFactory.newRoutine("Outpost");

        AutoTrajectory first = outpost.trajectory("Outpost");

        outpost.active().onTrue(
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
            )
        );

        return outpost;
    }
}
