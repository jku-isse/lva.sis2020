package hardware.actuators.motorsMock;


import hardware.sensors.MockSensor;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class simulates the behavior a actors.conveyor could exhibit.
 * When loading, the object will move for a time towards the loadingSensor,
 * until it triggers it. The unloadingSensor will also see input as the object will block it.
 * When unloading, the object will no longer touch the loadingSensor and pass the unloadingSensor,
 * causing both of them to no longer detecting input.
 */
public class ConveyorMockMotor extends MockMotor {

    private MockSensor sensorLoading;
    private MockSensor sensorUnloading;

    private ScheduledFuture loadingTask;
    private ScheduledFuture unloadingTask;
    private long delay;

    public ConveyorMockMotor(MockSensor sensorLoading, MockSensor sensorUnloading, int speed, long delay) {
        super(speed);
        this.sensorLoading = sensorLoading;
        this.sensorUnloading = sensorUnloading;
        this.delay = delay;
    }

    public MockSensor getSensorLoading() {
        return sensorLoading;
    }

    public MockSensor getSensorUnloading() {
        return sensorUnloading;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forward() {
        super.forward();
        if (sensorLoading != null && sensorUnloading != null) {
            sensorLoading.setDetectedInput(false);
            unloadingTask = executorService.schedule(() -> sensorUnloading.setDetectedInput(false), delay, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backward() {
        super.backward();
        if (sensorLoading != null && sensorUnloading != null) {
            sensorUnloading.setDetectedInput(true);
            loadingTask = executorService.schedule(() -> sensorLoading.setDetectedInput(true),
                    delay, TimeUnit.MILLISECONDS);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
        if (loadingTask != null) {
            loadingTask.cancel(true);
        }
        if (unloadingTask != null) {
            unloadingTask.cancel(true);
        }
    }
}
