// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.subsystems.drive.DriveSubsystem;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.commands.DriveCommands;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.SwerveModuleComp;

import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;
import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.commands.PathPlannerAuto;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.ConditionalCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and trigger mappings) should be declared here.
 */
public class RobotContainer {
    // Subsystems
    
    public final DriveSubsystem m_robotDrive;
    //private final LEDSubsystem m_LEDSubsystem;
    // Controller
    private final CommandXboxController m_driverController = new CommandXboxController(OperatorConstants.kDriverControllerPort);
  
    /** The container for the robot. Contains subsystems, OI devices, and commands.*/
    public RobotContainer() {
        //Hardware or SIM?
        switch (Constants.currentMode) {
        case REAL:
        // Real robot, instantiate hardware IO implementations
        m_robotDrive =
            new DriveSubsystem(
                new GyroIOPigeon2(true),
                new SwerveModuleComp(0),
                new SwerveModuleComp(1),
                new SwerveModuleComp(2),
                new SwerveModuleComp(3));
        // Configure AutoBuilder for holonomic (swerve) drive
        break;

      case SIM:
        // Sim robot, instantiate physics sim IO implementations
        m_robotDrive =
            new DriveSubsystem(
                new GyroIO() {},
                new ModuleIOSim(),
                new ModuleIOSim(),
                new ModuleIOSim(),
                new ModuleIOSim());
        break;

      default:
        // Replayed robot, disable IO implementations
        m_robotDrive =
            new DriveSubsystem(
                new GyroIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {},
                new ModuleIO() {});
        break;
    }

    // Set up named commands

    // Set up auto routines
    // Set up SysId routines

    // Configure the button bindings
    configureButtonBindings();
        // Configure the trigger bindings
    }

    /**
   * Use this method to define your button->command mappings. Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
      m_robotDrive.setDefaultCommand(
              DriveCommands.joystickDrive(
                      m_robotDrive,
                      () -> -m_driverController.getLeftY(),
                      () -> -m_driverController.getLeftX(),
                      () -> -m_driverController.getRightX()
                      ));
      
      m_driverController.x().onTrue(Commands.runOnce(m_robotDrive::stopWithX, m_robotDrive));
      
      
      m_driverController.start().onTrue(Commands.runOnce(() -> m_robotDrive.setPose(new Pose2d(m_robotDrive.getPose().getTranslation(), new Rotation2d())),m_robotDrive)
                .ignoringDisable(true));

        
  }   
  public Command getAutonomousCommand() {
    return Commands.print("No autonomous configured");
}
       
  public Command getAutoCommand(String autoName) {
      return new PathPlannerAuto(autoName);
  }
}
