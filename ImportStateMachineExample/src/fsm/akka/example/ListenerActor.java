package fsm.akka.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fsm.akka.example.TurntableActor.TurntableStateChangeEvent;

public class ListenerActor extends AbstractActor{

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	
	ActorRef parent;
	
	public static Props props() {
		return Props.create(ListenerActor.class, ActorRef.noSender());
	}
	
	public static Props props(ActorRef parent) {
		return Props.create(ListenerActor.class, parent);
	}
	
	public ListenerActor(ActorRef parent) {
		this.parent = parent;
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(
		        	TurntableStateChangeEvent.class, msg -> {
		        		log.info(msg.toString());
		        		parent.tell(msg, getSelf());
		            })
		        .matchAny(o -> log.info("received unknown message"))
		        .build();
	}

}
