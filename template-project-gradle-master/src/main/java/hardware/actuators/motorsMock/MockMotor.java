package hardware.actuators.motorsMock;

import hardware.actuators.Motor;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * This is a Mock implementation of a Motor. It can be used for testing, although it does not account for
 * errors in the construction.
 */
public class MockMotor extends Motor {

    private int motorSpeed;

    protected ScheduledExecutorService executorService;
    private ScheduledFuture forwardTask;    //Handle to stop task at any time
    private ScheduledFuture backwardTask;

    public MockMotor(int speed) {
        super();
        executorService = new ScheduledThreadPoolExecutor(1);
        motorSpeed = speed;
    }

    public int getMotorSpeed() {
        return motorSpeed;
    }

    public void setMotorSpeed(int motorSpeed) {
        this.motorSpeed = motorSpeed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forward() {
        super.forward();
        forwardTask = executorService.scheduleAtFixedRate(
                () -> System.out.println("===> Moving forward with speed: " + motorSpeed),
                0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void backward() {
        super.backward();
        backwardTask = executorService.scheduleAtFixedRate(
                () -> System.out.println("<=== Moving backward with speed: " + motorSpeed),
                0, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();
        System.out.println("Motor stopped");
        if (forwardTask != null) {
            forwardTask.cancel(true);
        }
        if (backwardTask != null) {
            backwardTask.cancel(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSpeed(int speed) {
        motorSpeed = speed;
        System.out.println("Set speed to " + speed);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void waitMs(long period) {
        if (period <= 0) return;
        long end = System.currentTimeMillis() + period;
        boolean interrupted = false;
        do {
            try {
                Thread.sleep(period);
            } catch (InterruptedException ie) {
                interrupted = true;
            }
            period = end - System.currentTimeMillis();
        } while (period > 0);
        if (interrupted)
            Thread.currentThread().interrupt();
    }
}
