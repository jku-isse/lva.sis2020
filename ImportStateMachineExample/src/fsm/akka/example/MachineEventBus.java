package fsm.akka.example;

import akka.actor.ActorRef;
import akka.event.japi.ScanningEventBus;
import fsm.akka.example.TurntableActor.TurntableStateChangeEvent;

public class MachineEventBus extends ScanningEventBus<TurntableActor.TurntableStateChangeEvent, ActorRef, String> {

	@Override
	public int compareClassifiers(String a, String b) {
		return a.compareTo(b);
	}

	@Override
	public int compareSubscribers(ActorRef a, ActorRef b) {
		return a.compareTo(b);
	}

	@Override
	public boolean matches(String classifier, TurntableStateChangeEvent event) {
		
		if (classifier.equals("*"))
			return true;
		else 
			return classifier.equals(event.getTurntableId());
	}

	@Override
	public void publish(TurntableStateChangeEvent event, ActorRef subscriber) {		
		subscriber.tell(event, ActorRef.noSender());
	}

}
