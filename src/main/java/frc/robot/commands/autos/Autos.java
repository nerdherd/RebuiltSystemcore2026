package frc.robot.commands.autos;

import org.wpilib.command2.CommandScheduler;
import org.wpilib.command2.Commands;
import org.wpilib.tunable.Tunables;

import choreo.auto.AutoChooser;
import choreo.auto.AutoFactory;
import choreo.auto.AutoRoutine;
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
        autoChooser.addRoutine("Bottom 2.5 w Distance", this::BottomDoubleSweep);
        autoChooser.addRoutine("Top 3 Bump w Distance", this::TopBumpTripleSweep);
        autoChooser.addRoutine("Middle Depot", this::Depot);

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

    /**
     * Chains trajectories to a routine. Commands should be handled using bindings and event markers.
     * @param routine
     * @param trajectories
     */
    public void chainTrajectories(AutoRoutine routine, String... trajectories) {
        if (trajectories.length == 0) return;
        // starts the first trajectory
        routine.active().onTrue(
            Commands.sequence(
                routine.trajectory(trajectories[0]).resetOdometry(),
                routine.trajectory(trajectories[0]).cmd()
            ));
        // directly chains the rest of the trajectories
        for (int i = 0; i < trajectories.length - 1; i++) {
            routine.trajectory(trajectories[i]).done().onTrue(
                routine.trajectory(trajectories[i + 1]).cmd()
            );
        }
    }

    public AutoRoutine TopDoubleSweep() {
        AutoRoutine topDoubleSweep = autoFactory.newRoutine("topDoubleSweep");
        chainTrajectories(topDoubleSweep, "TopSweep1", "TopSweep2", "TopLeave");
        return topDoubleSweep;
    }
    
    public AutoRoutine BottomDoubleSweep() {
        AutoRoutine bottomDoubleSweep = autoFactory.newRoutine("bottomDoubleSweep");
        chainTrajectories(bottomDoubleSweep, "BottomSweep1", "BottomSweep2", "BottomLeave");
        return bottomDoubleSweep;
    }

    public AutoRoutine TopBumpTripleSweep() {
        AutoRoutine topBumpTripleSweep = autoFactory.newRoutine("topBumpTripleSweep");
        chainTrajectories(topBumpTripleSweep, "TopS1Top3Bump_1", "TopS1Top3Bump_2", "TopS1Top3Bump_3");
        return topBumpTripleSweep;
    }

    public AutoRoutine Depot() {
        AutoRoutine depot = autoFactory.newRoutine("depot");
        chainTrajectories(depot, "Depot");
        return depot;
    }
}
