package frc.robot.subsystems;

import com.ctre.phoenix6.CANBus;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.InvertedValue;

import edu.wpi.first.math.MathUtil;

public class Flywheels {
    private static final String CANbusName = "Lucas";
    private static TalonFX flywheelBottomTalon = new TalonFX(32, CANbusName);
    private static TalonFX flywheelTopTalon = new TalonFX(31, CANbusName);
    private static TalonFX feederTalon = new TalonFX(33, CANbusName);

    public Flywheels() {
        TalonFXConfiguration config = new TalonFXConfiguration();
        config.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        flywheelBottomTalon.getConfigurator().apply(config);
        System.out.println("The thingy ran.");
    }

    public void setSpeed(double speed) {
        double newSpeed = MathUtil.clamp(speed, 0, .5);
        System.out.println("Speed was set to: " + newSpeed);
        flywheelBottomTalon.set(-newSpeed);
        flywheelTopTalon.set(newSpeed);
    }

    public void setFeed(double speed) {
        feederTalon.set(speed);
    }
}
