package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.commands.ClimberLeftExtend;
import frc.commands.ClimberLeftRetract;
import frc.commands.ClimberRightExtend;
import frc.commands.ClimberRightRetract;
import frc.commands.RunIndexer;
import frc.commands.RunIntake;
import frc.commands.Shoot;
import frc.commands.StageOneClimb;
import frc.commands.StageTwoClimb;
import frc.commands.TeleOpDrive;
import frc.util.TwoButtonTrigger;


public class OI {


    public XboxController driveController, manipController;
    private JoystickButton manipXBtn, manipABtn, manipBBtn, manipRBumper, manipLBumper, manipLTrigger, manipRTrigger, manipUpDPad, manipDownDPad; 
    public JoystickButton driveRBumper, driveLBumper, manipYBtn;


    public OI() {
        initControllers();
        

        Robot.drivetrain.setDefaultCommand(new TeleOpDrive());
    }

    /**
     * Initialize JoystickButtons and Controllers
     */
    private void initControllers() {
        driveController = new XboxController(0);
        manipController = new XboxController(1);

        // Driver A Button --> Toggle Field Oriented
        driveRBumper = new JoystickButton(driveController, XboxController.Button.kRightBumper.value);
        driveRBumper.whenPressed(new InstantCommand(() -> Robot.drivetrain.toggleFieldOriented()));

        // Driver A Button --> Reset field oriented angle
        driveLBumper = new JoystickButton(driveController, XboxController.Button.kLeftBumper.value);
        driveLBumper.whenPressed(new InstantCommand(() -> Robot.drivetrain.setGyro(-90)));

        // Hold Manip A Button --> Run Intake
        manipABtn = new JoystickButton(manipController, XboxController.Button.kA.value);
        manipABtn.whenHeld(new RunIntake(true));

        // Hold Manip X Button --> Run Shooter System
        manipXBtn = new JoystickButton(manipController, XboxController.Button.kX.value);
        manipXBtn.whenHeld(new Shoot());

        // Press Manip B buttopn --> Extend or retract intake
        manipBBtn = new JoystickButton(manipController, XboxController.Button.kB.value);
        manipBBtn.whenPressed(new InstantCommand(() -> Robot.intake.toggleIntakePiston()));
        
        // JoystickButton is 1 indexed
        // Hold Manip DPad Up --> Reverse Indexer
        manipUpDPad = new JoystickButton(manipController, 12 + 1); // 12 is up on the D-Pad
        manipUpDPad.whenActive(new RunIndexer(false));
    
        // Hold Manip DPad Down --> Reverse Intake
        manipDownDPad = new JoystickButton(manipController, 13 + 1); // 13 is down on the D-Pad
        manipDownDPad.whenActive(new RunIntake(false));

        // Set climber controls to either manual or automatic
        manipRBumper = new JoystickButton(manipController, XboxController.Button.kRightBumper.value);
        manipLBumper = new JoystickButton(manipController, XboxController.Button.kLeftBumper.value);
        manipRTrigger = new JoystickButton(manipController, XboxController.Button.kA.value);
        manipLTrigger = new JoystickButton(manipController, XboxController.Button.kB.value);
        initAutomaticClimber();
    }

    public double getDriverLeftX() {
        return driveController.getLeftX();
    }
    public double getDriverLeftY() {
        return -driveController.getLeftY();
    }
    public double getDriverRightX() {
        return driveController.getRightX();
    }

    public void initManualClimber() {
        // Hold Manip LT --> Extend left climber
        manipLTrigger.whileHeld(new ClimberLeftExtend());

        // Hold Manip RT --> Extend right climber
        manipRTrigger.whileHeld(new ClimberRightExtend());

        // Hold Manip LB --> Retract left climber
        manipLBumper.whileHeld(new ClimberLeftRetract());

        // Hold Manip RB --> Retract right climber
        manipRBumper.whileHeld(new ClimberRightRetract());

        // Press Manip Y --> Toggle left piston
        manipYBtn = new JoystickButton(manipController, XboxController.Button.kY.value);
        manipYBtn.whenPressed(new InstantCommand(() -> Robot.climber.toggleLeftPiston()));


    }

    public void initAutomaticClimber() {
        // Hold Manip LB and RB --> Stage One Climb with 2 second wait
        TwoButtonTrigger stageOneTrigger = new TwoButtonTrigger(manipLBumper, manipRBumper);
        stageOneTrigger.whenActive(new StageOneClimb(2));  //check time

        // Hold Manip LT and RT --> Stage Two Climb
        TwoButtonTrigger stageTwoTrigger = new TwoButtonTrigger(manipLTrigger, manipRTrigger);
        stageTwoTrigger.whenActive(new StageTwoClimb());
    }
    

}