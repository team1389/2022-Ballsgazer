package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.commands.RunIndexer;
import frc.commands.RunIntake;
import frc.commands.Shoot;
import frc.commands.ShootWithSensors;
import frc.commands.StageOneClimb;
import frc.commands.StageTwoClimb;
import frc.commands.TeleOpDrive;
import frc.util.TwoButtonTrigger;


public class OI {


    public XboxController driveController, manipController;
    private JoystickButton shootXBtn, intakeABtn, climbRBumper, climbLBumper, climbLTrigger, climbRTrigger, BIndexerUpDPad, BIntakeDownDPad; 
    public JoystickButton driveRBumper, driveLBumper;


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
        intakeABtn = new JoystickButton(manipController, XboxController.Button.kA.value);
        intakeABtn.whenHeld(new RunIntake(true));

        // Hold Manip X Button --> Run Shooter System
        shootXBtn = new JoystickButton(manipController, XboxController.Button.kX.value);
        shootXBtn.whenHeld(new Shoot());

        // Hold Manip LB and RB --> Stage One Climb with 2 second wait
        climbRBumper = new JoystickButton(manipController, XboxController.Button.kRightBumper.value);
        climbLBumper = new JoystickButton(manipController, XboxController.Button.kLeftBumper.value);
        TwoButtonTrigger stageOneTrigger = new TwoButtonTrigger(climbRBumper, climbLBumper);
        stageOneTrigger.whenActive(new StageOneClimb(2));  //check time

        // Hold Manip LT and RT --> Stage Two Climb
        climbLTrigger = new JoystickButton(manipController, XboxController.Button.kA.value);
        climbRTrigger = new JoystickButton(manipController, XboxController.Button.kB.value);
        TwoButtonTrigger stageTwoTrigger = new TwoButtonTrigger(climbLTrigger, climbRTrigger);
        stageTwoTrigger.whenActive(new StageTwoClimb());

        // JoystickButton is 1 indexed
        // Hold Manip DPad Up --> Reverse Indexer
        BIndexerUpDPad = new JoystickButton(manipController, 12 + 1); // 12 is up on the D-Pad
        BIndexerUpDPad.whenActive(new RunIndexer(false));
        
        // Hold Manip DPad Down --> Reverse Intake
        BIntakeDownDPad = new JoystickButton(manipController, 13 + 1); // 13 is down on the D-Pad
        BIntakeDownDPad.whenActive(new RunIntake(false));
        
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
    

}