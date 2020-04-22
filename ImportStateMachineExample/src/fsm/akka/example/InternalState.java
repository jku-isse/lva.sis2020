package fsm.akka.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

@Configuration
public class InternalState {
	private StateMachine<String, String> stateMachine;
	
		
	@Bean
	public static Action<String, String> doStoppingBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				System.out.println("doStopping triggered");
			}
		};
	}
	
	public void setStateMachine(StateMachine<String, String> stateMachine) {
		this.stateMachine = stateMachine;
	}
	
	

	public StateMachine<String, String> getStateMachine() {
		return stateMachine;
	}

	@Bean
	public static Action<String, String> doResettingBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				
				System.out.println("doResetting triggered in StateMachine: "+context.getStateMachine().getUuid());
			}
		};
	}
	
	@Bean
	public static Action<String, String> doTurningBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				//context.getStateMachine()
				Message<String> msg = context.getMessage();
				if (msg != null) {
					System.out.println("TurningHeaders: "+msg.getHeaders().toString());
					System.out.println("TurningPayload: "+msg.getPayload());
				}
				System.out.println("doTurning triggered");
			}
		};
	}
	
	@Bean
	public static ActorProducer turntableActorProducer(){
		return new ActorProducer() {

			@Autowired
			private StateMachine<String, String> stateMachine;
			@Autowired
			private ActorSystem system;
			@Autowired MachineEventBus bus;

			@Override
			public ActorRef produce(String actorInstanceName) {
				InternalState state = new InternalState();
				state.setStateMachine(stateMachine);
				ActorRef actor = system.actorOf(TurntableActor.props(state, bus), actorInstanceName);
				return actor;
			}
		};
	}
}
