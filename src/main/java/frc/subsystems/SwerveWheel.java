package frc.subsystems;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import com.revrobotics.SparkMaxPIDController;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveWheel extends SubsystemBase {
    private CANSparkMax rotateMotor, driveMotor;
    private SparkMaxPIDController rotatePIDController, drivePIDController;

    //Doesn't reset between matches, unlike the built in relative encoders
    private CANCoder rotateAbsEncoder;

    //Multiplied by the native output units (-1 to 1) to find position
    private final double ROTATION_POSITION_CONVERSION_FACTOR = 5.33 * 7;

    //Factor between RPM and m/s
    //TODO: Figure out what this value is
    private final double DRIVE_VELOCITY_CONVERSION_FACTOR = 1;


    //Create PID coefficients
    public double rotateP = 1;
    public double rotateI = 0;
    public double rotateD = 0;

    public double driveP = 1;
    public double driveI = 0;
    public double driveD = 0;

    public SwerveWheel(int driveMotorPort, int rotateMotorPort, int rotateEncoderPort) {
        //Instantiate motors/sensors
        driveMotor = new CANSparkMax(driveMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
        rotateMotor = new CANSparkMax(rotateMotorPort, CANSparkMaxLowLevel.MotorType.kBrushless);
        rotateAbsEncoder = new CANCoder(rotateEncoderPort);



        //Assign PID controllers' parameters
        rotatePIDController = rotateMotor.getPIDController();

        rotatePIDController.setP(rotateP);
        rotatePIDController.setI(rotateI);
        rotatePIDController.setD(rotateD);
        rotateMotor.getEncoder().setPositionConversionFactor(ROTATION_POSITION_CONVERSION_FACTOR * 180);
    }

    public void setSpeed(double speed) {
        driveMotor.set(speed);
    }

    //Angle should be measured in degrees, from -180 to 180
    public void setAngle(double angle) {
        double currentAngle = rotateMotor.getEncoder().getPosition();
        double setpointAngle = closestAngle(currentAngle, angle);
        double setpointAngleFlipped = closestAngle(currentAngle, angle + 180.0);

        //If the closest angle to setpoint is shorter
        if (Math.abs(setpointAngle) <= Math.abs(setpointAngleFlipped))
        {
            driveMotor.setInverted(false);
            rotatePIDController.setReference(currentAngle + setpointAngle, CANSparkMax.ControlType.kPosition);
        }

        //If the closest angle to setpoint + 180 is shorter
        else
        {
            driveMotor.setInverted(true);
            rotatePIDController.setReference(currentAngle + setpointAngleFlipped, CANSparkMax.ControlType.kPosition);
        }
    }

    //Set the relative encoder to its wheel's actual angle
    public void coordinateRelativeEncoder() {
        double absAngle = rotateAbsEncoder.getAbsolutePosition();
        if(absAngle<=180) {
            rotateMotor.getEncoder().setPosition(absAngle);
        }
        else {
            rotateMotor.getEncoder().setPosition(-360+absAngle);
        }
        
    }

    //Get the closest angle between the given angles.
   private static double closestAngle(double a, double b) {
        //Get direction
        double dir = (b % 360.0) - (a % 360.0);
   
        //Convert from -360 to 360 to -180 to 180
        if (Math.abs(dir) > 180.0)
        {
            dir = -(Math.signum(dir) * 360.0) + dir;
        }
        return dir;
   }

    //Only run this when training the angle, never in matches
    public void resetAbsEncoder() {
        rotateAbsEncoder.setPosition(0);
    }

    public SwerveModuleState getState() {
        //Return the current module position and speed
        return new SwerveModuleState(driveMotor.getEncoder().getVelocity()*DRIVE_VELOCITY_CONVERSION_FACTOR,
            Rotation2d.fromDegrees(-rotateMotor.getEncoder().getPosition()));
    }


}
