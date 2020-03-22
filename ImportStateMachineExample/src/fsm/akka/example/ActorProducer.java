package fsm.akka.example;

import org.springframework.beans.factory.annotation.Autowired;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public abstract class ActorProducer {
	
	@Autowired
	protected ActorSystem system;
	
	public abstract ActorRef produce(String actorInstanceName);
	
	public ActorRef produce() {
		return produce(null);
	}
}
