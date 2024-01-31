package frc.robot.motorcontrol.configurations.phoenix;

import com.ctre.phoenix6.StatusCode;
import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
import com.ctre.phoenix6.configs.GyroTrimConfigs;
import com.ctre.phoenix6.configs.MotorOutputConfigs;
import com.ctre.phoenix6.configs.MountPoseConfigs;
import com.ctre.phoenix6.configs.Pigeon2Configuration;
import com.ctre.phoenix6.configs.Slot0Configs;
import com.ctre.phoenix6.configs.Slot1Configs;
import com.ctre.phoenix6.configs.Slot2Configs;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import edu.wpi.first.wpilibj.DriverStation;
import java.util.function.Supplier;

public class PhoenixUtils {
    private static final double kEps = 1e-3;

  public static void retryUntilSuccess(
      final Supplier<StatusCode> setterFunction, final String description) {
    retryUntilSuccess(setterFunction, () -> true, 10, description);
  }

  public static void retryUntilSuccess(
      final Supplier<StatusCode> setterFunction,
      final Supplier<Boolean> validatorFunction,
      final String description) {
    retryUntilSuccess(setterFunction, validatorFunction, 10, description);
  }

  public static void retryUntilSuccess(
      final Supplier<StatusCode> setterFunction,
      final Supplier<Boolean> validatorFunction,
      final int numTries,
      final String description) {
    for (int i = 0; i < numTries; i++) {
      // Call Phoenix setter and check for success.
      final StatusCode code = setterFunction.get();
      if (!code.isOK()) {
        DriverStation.reportWarning(
            description
                + ": Retrying ("
                + (i + 1)
                + "/"
                + numTries
                + ") due to config error code: "
                + code.getName(),
            false);
        continue;
      }

      // Status code indicates success, but check to make sure it's actually correct.
      if (!validatorFunction.get()) {
        DriverStation.reportWarning(
            description
                + ": Retrying ("
                + (i + 1)
                + "/"
                + numTries
                + ") due to failed config validation",
            false);
        continue;
      }

      // We are actually successful!
      return;
    }

    // We exhausted numTries without success.
    DriverStation.reportError(description, false);
  }
  public static boolean CANcoderConfigsEqual(
    final CANcoderConfiguration expected, final CANcoderConfiguration actual) {
    if (!expected.MagnetSensor.SensorDirection.equals(actual.MagnetSensor.SensorDirection)) {
        DriverStation.reportWarning(
            "[SensorDirection] Expected: "
                + expected.MagnetSensor.SensorDirection
                + ", Actual: "
                + actual.MagnetSensor.SensorDirection,
            false);
        return false;
    }
    if (!epsilonEquals(
        expected.MagnetSensor.MagnetOffset, actual.MagnetSensor.MagnetOffset, kEps)) {
      DriverStation.reportWarning(
          "[MagnetOffset] Expected: "
              + expected.MagnetSensor.MagnetOffset
              + ", Actual: "
              + actual.MagnetSensor.MagnetOffset,
          false);
      return false;
    }
    if (!expected.MagnetSensor.AbsoluteSensorRange.equals(
        actual.MagnetSensor.AbsoluteSensorRange)) {
      DriverStation.reportWarning(
          "[AbsoluteSensorRange] Expected: "
              + expected.MagnetSensor.AbsoluteSensorRange
              + ", Actual: "
              + actual.MagnetSensor.AbsoluteSensorRange,
          false);
      return false;
    }
    return true;

    }
    public static boolean epsilonEquals(double a, double b, double epsilon) {
        return (a - epsilon <= b) && (a + epsilon >= b);
      }

      public static boolean isEqual(MountPoseConfigs a, MountPoseConfigs b) {
        return epsilonEquals(a.MountPoseRoll, b.MountPoseRoll, kEps)
            && epsilonEquals(a.MountPosePitch, b.MountPosePitch, kEps)
            && epsilonEquals(a.MountPoseYaw, b.MountPoseYaw, kEps);
      }
    
      public static boolean isEqual(GyroTrimConfigs a, GyroTrimConfigs b) {
        return epsilonEquals(a.GyroScalarX, b.GyroScalarX, kEps)
            && epsilonEquals(a.GyroScalarY, b.GyroScalarY, kEps)
            && epsilonEquals(a.GyroScalarZ, b.GyroScalarZ, kEps);
      }

      public static boolean TalonFXConfigsEqual(
      TalonFXConfiguration expected, TalonFXConfiguration actual) {
    if (!isEqual(expected.MotorOutput, actual.MotorOutput)) {
      DriverStation.reportWarning(
          "[MotorOutput] Expected: " + expected.MotorOutput + ", Actual: " + actual.MotorOutput,
          false);
      return false;
    }
    if (!isEqual(expected.CurrentLimits, actual.CurrentLimits)) {
      DriverStation.reportWarning(
          "[CurrentLimits] Expected: "
              + expected.CurrentLimits
              + ", Actual: "
              + actual.CurrentLimits,
          false);
      return false;
    }
    if (!isEqual(expected.Slot0, actual.Slot0)) {
      DriverStation.reportWarning(
          "[Slot0] Expected: " + expected.Slot0 + ", Actual: " + actual.Slot0, false);
      return false;
    }
    if (!isEqual(expected.Slot1, actual.Slot1)) {
      DriverStation.reportWarning(
          "[Slot1] Expected: " + expected.Slot1 + ", Actual: " + actual.Slot1, false);
      return false;
    }
    if (!isEqual(expected.Slot2, actual.Slot2)) {
      DriverStation.reportWarning(
          "[Slot2] Expected: " + expected.Slot2 + ", Actual: " + actual.Slot2, false);
      return false;
    }
    // TODO: Check other values
    return true;
  }

  public static boolean isEqual(Slot0Configs a, Slot0Configs b) {
    return epsilonEquals(a.kP, b.kP, kEps)
        && epsilonEquals(a.kI, b.kI, kEps)
        && epsilonEquals(a.kD, b.kD, kEps)
        && epsilonEquals(a.kS, b.kS, kEps)
        && epsilonEquals(a.kV, b.kV, kEps)
        && epsilonEquals(a.kA, b.kA, kEps)
        && epsilonEquals(a.kG, b.kG, kEps);
  }

  public static boolean isEqual(Slot1Configs a, Slot1Configs b) {
    return epsilonEquals(a.kP, b.kP, kEps)
        && epsilonEquals(a.kI, b.kI, kEps)
        && epsilonEquals(a.kD, b.kD, kEps)
        && epsilonEquals(a.kS, b.kS, kEps)
        && epsilonEquals(a.kV, b.kV, kEps)
        && epsilonEquals(a.kA, b.kA, kEps)
        && epsilonEquals(a.kG, b.kG, kEps);
  }

  public static boolean isEqual(Slot2Configs a, Slot2Configs b) {
    return epsilonEquals(a.kP, b.kP, kEps)
        && epsilonEquals(a.kI, b.kI, kEps)
        && epsilonEquals(a.kD, b.kD, kEps)
        && epsilonEquals(a.kS, b.kS, kEps)
        && epsilonEquals(a.kV, b.kV, kEps)
        && epsilonEquals(a.kA, b.kA, kEps)
        && epsilonEquals(a.kG, b.kG, kEps);
  }

  public static boolean isEqual(MotorOutputConfigs a, MotorOutputConfigs b) {
    return a.NeutralMode == b.NeutralMode
        && a.Inverted == b.Inverted
        && epsilonEquals(a.DutyCycleNeutralDeadband, b.DutyCycleNeutralDeadband, kEps);
  }

  public static boolean isEqual(CurrentLimitsConfigs a, CurrentLimitsConfigs b) {
    return a.StatorCurrentLimitEnable == b.StatorCurrentLimitEnable
        && epsilonEquals(a.StatorCurrentLimit, b.StatorCurrentLimit, kEps)
        && a.SupplyCurrentLimitEnable == b.SupplyCurrentLimitEnable
        && epsilonEquals(a.SupplyCurrentLimit, b.SupplyCurrentLimit, kEps)
        && epsilonEquals(a.SupplyCurrentThreshold, b.SupplyCurrentThreshold, kEps)
        && epsilonEquals(a.SupplyTimeThreshold, b.SupplyTimeThreshold, kEps);
  }
      
      
    
}
