package actors.conveyor.mock;

import actors.conveyor.ConveyorHardware;
import hardware.actuators.motorsMock.ConveyorMockMotor;
import hardware.sensors.MockSensor;

public class ConveyorMockHardware extends ConveyorHardware {

    public ConveyorMockHardware(int speed, long delay) {
        loadingSensor = new MockSensor();
        unloadingSensor = new MockSensor();
        conveyorMotor = new ConveyorMockMotor((MockSensor) loadingSensor,
                (MockSensor) unloadingSensor, speed, delay);
    }

    public ConveyorMockMotor getMockConveyorMotor() {
        return (ConveyorMockMotor) conveyorMotor;
    }

    public MockSensor getMockLoadingSensor() {
        return (MockSensor) loadingSensor;
    }

    public MockSensor getMockUnloadingSensor() {
        return (MockSensor) unloadingSensor;
    }
}
