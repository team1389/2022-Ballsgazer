// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Robot;

public class TurnTurretForTime extends CommandBase {
  /** Creates a new ManualAuto. */
  Timer timer;
  double timeout;

  public TurnTurretForTime(double timeout) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());

    timer = new Timer();
    this.timeout = timeout;

    addRequirements();
  }

  @Override
  public void initialize() {
    timer.reset();
    timer.start();
  }

  @Override
  public void execute() {
    Robot.shooter.setTurretPower(-0.176);
    SmartDashboard.putNumber("Turret Position", Robot.shooter.getTurretEncoder().getPosition());
  }

  @Override
  public boolean isFinished() {
    return timer.hasElapsed(timeout);
  }
  
  @Override
  public void end(boolean interrupted) {
    Robot.shooter.setTurretPower(0);
  }
}
