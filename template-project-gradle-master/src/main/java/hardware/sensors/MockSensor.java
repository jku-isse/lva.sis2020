package hardware.sensors;

/**
 * This is a Mock implementation of a Sensor. It can be used for testing, although it does not account for
 * errors in the construction.
 */
public class MockSensor extends Sensor {

    public MockSensor(){
        this.detectingInput = false;
    }

    public void setDetectedInput(boolean detectedInput) {
        this.detectingInput = detectedInput;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDetectingInput() {
        return detectingInput;
    }

}
