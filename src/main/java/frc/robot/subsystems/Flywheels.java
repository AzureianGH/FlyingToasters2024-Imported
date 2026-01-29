package frc.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.MathUtil;
import frc.robot.Constants;
import frc.robot.Constants.FlywheelDirection;

public class Flywheels {
    private static TalonFX flywheelBottomTalon = new TalonFX(32, Constants.lowerCANBusName);
    private static TalonFX flywheelTopTalon = new TalonFX(31, Constants.lowerCANBusName);
    private static TalonFX feederTalon = new TalonFX(33, Constants.lowerCANBusName);
    private int currentDirection = 1; // 1 to OUTTAKE & -1 to INTAKE
    private double currentSpeed = 0;

    public Flywheels() {
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        flywheelBottomTalon.getConfigurator().apply(config);
    }

    public void setSpeed(double speed) {
        currentSpeed = MathUtil.clamp(speed, 0, .5) * currentDirection;
        flywheelBottomTalon.set(currentSpeed);
        flywheelTopTalon.set(currentSpeed);
    }
    
    public void increaseSpeed(double increase) {
        currentSpeed += increase;
        currentSpeed %= 1;
        currentSpeed = MathUtil.clamp(currentSpeed, 0, 1) * currentDirection;
        flywheelBottomTalon.set(currentSpeed);
        flywheelTopTalon.set(currentSpeed);
    }

    public void setFeed(double speed) {
        feederTalon.set(speed);
    }

    public void setDirection(FlywheelDirection direction) {
        if (direction == FlywheelDirection.OUTTAKE) {
            currentDirection = 1;
        } else {
            currentDirection = -1;
        }
    }
}
