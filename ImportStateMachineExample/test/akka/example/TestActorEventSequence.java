package akka.example;

import java.time.Duration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.statemachine.StateMachine;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import fsm.akka.example.ActorProducer;
import fsm.akka.example.AkkaSpringApplication;
import fsm.akka.example.InternalState;
import fsm.akka.example.ListenerActor;
import fsm.akka.example.MachineEventBus;
import fsm.akka.example.TurntableActor;
import fsm.akka.example.TurntableActor.TurntableStateChangeEvent;
import fsm.example.config.Signals;
import fsm.example.config.States;

public class TestActorEventSequence {

	private static ActorSystem system;
	private MachineEventBus bus;	
	private ApplicationContext context;
	
	@Autowired
	private StateMachine<String, String> stateMachine;
	
	@BeforeEach
	public void setup() throws Exception{
		system = ActorSystem.create("TEST_ROOT_SYSTEM");		
		context = SpringApplication.run(AkkaSpringApplication.class);		
		bus = (MachineEventBus) context.getBean("machineEventBus");
	}
	
	@AfterEach
	public void teardown() {
	    TestKit.shutdownActorSystem(system);
	    system = null;
	  }
	
	
	@Test
	void testActorSequence() {
		new TestKit(system) { 
			{ 
				InternalState state = new InternalState();
				state.setStateMachine(stateMachine);
				ActorRef ttActor = ((ActorProducer) context.getBean("turntableActorProducer")).produce("TestActor1");
//				bus.subscribe(getRef(), "*");		// DONT DO THIS, APPARENTLY THIS CAUSES A MESSAGE SENT INTERRUPTION, as a workaround have another actor forward the message just like the listener here				
				ActorSystem system = (ActorSystem) context.getBean("actorSystem");
		        ActorRef listener = system.actorOf(ListenerActor.props(getRef()));
				bus.subscribe(listener, "*");
				
				ttActor.tell(new TurntableActor.BaseTurntableMessage(Signals.ResetSignal), ActorRef.noSender());
				boolean doRun = true;
				while(doRun) {
					TurntableStateChangeEvent event = expectMsgAnyClassOf(Duration.ofSeconds(3), TurntableStateChangeEvent.class);
					System.out.println("Event received: "+event);
					if (event.getState().equals(States.Idle)) {
						doRun = false;
					}						
				}
				
			}};
	}
}
