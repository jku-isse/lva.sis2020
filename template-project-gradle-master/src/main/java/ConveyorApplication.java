import actors.ConveyorActor;
import actors.conveyor.stateMachine.ConveyorTriggers;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.time.Duration;

/**
 * Test Application showcasing the use of actors.
 * See https://doc.akka.io/docs/akka/current/typed/guide/index.html
 * or https://www.ev3dev.org/
 * for more information.
 */
public class ConveyorApplication {
    public static final boolean DEBUG = true;   //set true if running the program on pc

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef conveyorActor = system.actorOf(ConveyorActor.props(DEBUG));

        system.scheduler().scheduleAtFixedRate(Duration.ZERO, Duration.ofMillis(4000),
                () -> conveyorActor.tell(ConveyorTriggers.LOAD, ActorRef.noSender()),
                system.dispatcher());

        system.scheduler().scheduleAtFixedRate(Duration.ofMillis(2000), Duration.ofMillis(4000),
                () -> conveyorActor.tell(ConveyorTriggers.UNLOAD, ActorRef.noSender()),
                system.dispatcher());

        system.scheduler().scheduleOnce(Duration.ofSeconds(10), () -> {
            system.terminate().value();
            System.exit(0);
        }, system.dispatcher());

    }
}
