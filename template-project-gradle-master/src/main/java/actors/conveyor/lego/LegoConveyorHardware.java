package actors.conveyor.lego;

import actors.conveyor.ConveyorHardware;
import hardware.actuators.motorsEV3.MediumMotorEV3;
import hardware.sensors.sensorsEV3.ColorSensorEV3;
import hardware.sensors.sensorsEV3.TouchSensorEV3;
import lejos.hardware.port.Port;

/**
 * When switching to real lego ev3 hardware, we can substitute our mock hardware using this class.
 * Be aware that the mock has predefined behaviour, whereas here the behaviour should emerge during usage.
 */
public class LegoConveyorHardware extends ConveyorHardware {

    public LegoConveyorHardware(Port motorPort, Port loadingSensorPort, Port unloadingSensorPort){
        conveyorMotor = new MediumMotorEV3(motorPort);
        loadingSensor = new TouchSensorEV3(loadingSensorPort);
        unloadingSensor = new ColorSensorEV3(unloadingSensorPort);
    }
}
