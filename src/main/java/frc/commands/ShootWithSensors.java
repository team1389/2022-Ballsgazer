// package frc.commands;

// import edu.wpi.first.wpilibj.Timer;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
// import edu.wpi.first.wpilibj2.command.InstantCommand;
// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import edu.wpi.first.wpilibj2.command.WaitCommand;
// import frc.robot.Robot;

// public class ShootWithSensors extends ParallelCommandGroup {

//     //TODO: Find this time
//     // Time from the indexer starting to the last ball being shot
//     private final double SHOOT_TIME = 4.5;

//     private Timer timer;
//     public ShootWithSensors() {
//         // addRequirements(Robot.shooter);
//         addRequirements();
        
//         timer = new Timer();
        
        
//         // To shoot, first spin up the flywheel while turning to the target
//         // When facing the target, run the indexer and hopper to feed balls to the flywheel and shoot
//         addCommands(
//             new SetShooterRPM(),
//             new SequentialCommandGroup(
//                 //new TurretTracking(),
//                 new WaitCommand(1),
//                 new InstantCommand(() -> timer.start()),
                
//                 //Run indexer and hopper:
//                 new ParallelCommandGroup(
//                     new RunIndexer(), 
//                     new RunHopper()
//                 )
//             )
//         );
//     }

//     @Override
//     public void initialize() {
//         super.initialize();

        
//         timer.reset();
//         timer.start();

//         SmartDashboard.putString("Shooting", "yep");
//     }

//     @Override
//     public boolean isFinished() {
//         // return false;
//         return timer.hasElapsed(SHOOT_TIME);
//     }

//     @Override
//     public void end(boolean interrupted) {
//         SmartDashboard.putString("Shooting", "nope");
        
//         Robot.shooter.stopShooter();
//         Robot.shooter.stopIndexer();
//         Robot.hopper.stopHopper();
//     }
// }
