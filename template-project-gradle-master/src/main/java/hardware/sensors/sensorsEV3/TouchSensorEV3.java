package hardware.sensors.sensorsEV3;

import ev3dev.sensors.ev3.EV3TouchSensor;
import hardware.sensors.Sensor;
import lejos.hardware.port.Port;

/**
 * This class can be used to get an input from a lego ev3 touch sensor.
 */
public class TouchSensorEV3 extends Sensor {

    private final EV3TouchSensor touchSensor;

    public TouchSensorEV3(Port sensorPort) {
        this.touchSensor = new EV3TouchSensor(sensorPort);
    }

    @Override
    public boolean isDetectingInput() {
        return this.touchSensor.isPressed();
    }
}
