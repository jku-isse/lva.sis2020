package actors.conveyor;

import hardware.actuators.Motor;
import hardware.sensors.Sensor;

/**
 * This is a base class for a simple actors.conveyor belt. A actors.conveyor belt has one loading sensor that detects
 * input when fully loaded and one for unloading. The unloading sensor detects input as long there is an
 * object on the actors.conveyor.
 * <p>
 * As this class is designed with a TurnTable in mind, you might need to adapt or create your own.
 */
public abstract class ConveyorHardware {

    protected Motor conveyorMotor;
    protected Sensor loadingSensor;
    protected Sensor unloadingSensor;

    public Motor getConveyorMotor() {
        return conveyorMotor;
    }

    public Sensor getLoadingSensor() {
        return loadingSensor;
    }

    public Sensor getUnloadingSensor() {
        return unloadingSensor;
    }

}
