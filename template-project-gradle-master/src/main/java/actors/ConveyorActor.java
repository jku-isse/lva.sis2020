package actors;

import actors.conveyor.ConveyorHardware;
import actors.conveyor.stateMachine.ConveyorStates;
import actors.conveyor.stateMachine.ConveyorTriggers;
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import actors.conveyor.lego.LegoConveyorHardware;
import actors.conveyor.mock.ConveyorMockHardware;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.StateMachineConfig;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;

import java.time.Duration;

public class ConveyorActor extends AbstractActor {

    private StateMachine<ConveyorStates, ConveyorTriggers> stateMachine;

    private final boolean DEBUG;
    private ConveyorHardware conveyorHardware;

    LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    static public Props props(boolean isDebug) {
        return Props.create(ConveyorActor.class, () -> new ConveyorActor(isDebug));
    }

    public ConveyorActor(boolean isDebug) {
        DEBUG = isDebug;
        initHardware();
        configureStateMachine();
    }

    private void initHardware() {
        if (DEBUG) {
            conveyorHardware = new ConveyorMockHardware(200, 1000);
            log.info("Initialized mock hardware");
        } else {
            conveyorHardware = new LegoConveyorHardware(MotorPort.A, SensorPort.S2, SensorPort.S3);
            conveyorHardware.getConveyorMotor().setSpeed(500);
        }
    }

    private void configureStateMachine() {
        StateMachineConfig<ConveyorStates, ConveyorTriggers> config = new StateMachineConfig<>();
        config.configure(ConveyorStates.LOADED)
                .permit(ConveyorTriggers.UNLOAD, ConveyorStates.UNLOADED);
        config.configure(ConveyorStates.UNLOADED)
                .permit(ConveyorTriggers.LOAD, ConveyorStates.LOADED);
        stateMachine = new StateMachine<>(ConveyorStates.UNLOADED, config);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ConveyorTriggers.class, trigger -> {
                    if (stateMachine.canFire(trigger)) {
                        switch (trigger) {
                            case LOAD:
                                stateMachine.fire(trigger);
                                loadingToFullyOccupied();
                                break;
                            case UNLOAD:
                                stateMachine.fire(trigger);
                                unloadingToIdle();
                                break;
                            default: // all others are internal triggers
                                log.warning(String.format("Received internal transition trigger %s as an external request, ignoring", trigger));
                                break;
                        }
                    }else{
                        log.info("Received message " + trigger.toString() + " in state " + stateMachine.getState());
                    }
                })
                .matchAny(msg -> {
                    log.warning("Unexpected Message received: " + msg.toString());
                })
                .build();
    }

    private void loadingToFullyOccupied() {
        motorBackward();
        checkForFullyLoaded();
    }

    private void checkForFullyLoaded() {
        if (sensorLoadingHasDetectedInput()) {
            motorStop();
        } else {
            context().system()
                    .scheduler()
                    .scheduleOnce(Duration.ofMillis(100),
                            this::checkForFullyLoaded,
                            context().system().dispatcher());
        }
    }

    private void unloadingToIdle() {
        motorForward();
        checkForFullyUnloaded();
    }

    private void checkForFullyUnloaded() {
        if (!sensorUnloadingHasDetectedInput()) {
            motorStop();
        } else {
            context().system()
                    .scheduler()
                    .scheduleOnce(Duration.ofMillis(100),
                            this::checkForFullyUnloaded,
                            context().system().dispatcher());
        }
    }

    private boolean sensorLoadingHasDetectedInput() {
        return conveyorHardware.getLoadingSensor().isDetectingInput();
    }

    private boolean sensorUnloadingHasDetectedInput() {
        return conveyorHardware.getUnloadingSensor().isDetectingInput();
    }

    private void motorForward() {
        conveyorHardware.getConveyorMotor().forward();
    }

    private void motorBackward() {
        conveyorHardware.getConveyorMotor().backward();
    }

    private void motorStop() {
        conveyorHardware.getConveyorMotor().stop();
    }
}
