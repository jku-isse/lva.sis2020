package fsm.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import fsm.example.config.Signals;
import fsm.example.config.States;

@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private StateMachine<String, String> stateMachine;
	
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

	@Override
	public void run(String... args) throws Exception {
		stateMachine.addStateListener(listener());
		stateMachine.start();
		System.out.println("State: "+stateMachine.getState().toString());
		stateMachine.getTransitions().stream()
		.filter(t -> t.getTrigger() != null)
		.forEach(t -> {
			System.out.println(String.format(" to reach %s use %s ", t.getTarget().getId(), t.getTrigger().getEvent()));
		});
	}

	
	@Bean
    public StateMachineListener<String, String> listener() {
        return new StateMachineListenerAdapter<String, String>() {
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                System.out.println("State change to " + to.getId());
                States state = States.valueOf(to.getId());
                switch(state) {
				case Idle:
					//stateMachine.sendEvent("TurnToSignal");
					Message<String> msg = MessageBuilder
						.withPayload(Signals.TurnToSignal.toString())
						.setHeader("orientation", "2")
						.build();
					stateMachine.sendEvent(msg);
					break;
				case Resetting:
					break;
				case Stopped:
					stateMachine.sendEvent("ResetSignal");
					break;
				case Stopping:
					break;
				default:
					break;                
                }

            }
        };
    }
	
	@Bean
	public Action<String, String> doStoppingBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				System.out.println("doStopping triggered");
			}
		};
	}
	
	@Bean
	public Action<String, String> doResettingBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				System.out.println("doResetting triggered");
			}
		};
	}
	
	@Bean
	public Action<String, String> doTurningBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				Message<String> msg = context.getMessage();
				if (msg != null) {
					System.out.println("TurningHeaders: "+msg.getHeaders().toString());
					System.out.println("TurningPayload: "+msg.getPayload());
				}
				System.out.println("doTurning triggered");
			}
		};
	}
}