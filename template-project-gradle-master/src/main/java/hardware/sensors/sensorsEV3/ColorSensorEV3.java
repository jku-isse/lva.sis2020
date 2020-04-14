package hardware.sensors.sensorsEV3;

import ev3dev.sensors.ev3.EV3ColorSensor;
import hardware.sensors.Sensor;
import lejos.hardware.port.Port;
import lejos.robotics.Color;

/**
 * This class can be used to detect objects using a lego ev3 color sensor.
 */
public class ColorSensorEV3 extends Sensor {

    private final EV3ColorSensor colorSensor;

    public ColorSensorEV3(Port sensorPort) {
        this.colorSensor = new EV3ColorSensor(sensorPort);
    }

    @Override
    public boolean isDetectingInput() {
        return !(colorSensor.getColorID() == Color.NONE);
    }

}
