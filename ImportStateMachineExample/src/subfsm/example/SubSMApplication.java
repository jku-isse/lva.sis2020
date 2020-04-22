package subfsm.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@SpringBootApplication
public class SubSMApplication implements CommandLineRunner {

	@Autowired
	private StateMachine<String, String> stateMachine;
	
    public static void main(String[] args) {
        SpringApplication.run(SubSMApplication.class, args);
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
		//stateMachine.start();
	}

	
	@Bean
    public StateMachineListener<String, String> listener() {
        return new StateMachineListenerAdapter<String, String>() {
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                String fromId = from == null ? "N/A" : from.getId();
            	System.out.println(String.format("State change from %s to %s ", fromId, to.getId()));
            	if (to.getId().equals("Starting")) {
					stateMachine.sendEvent("InputBeforeSub");
            	} else if (to.getId().equals("WaitForInput")) {
					stateMachine.sendEvent("InputToSub");
            	}
//                States state = States.valueOf(to.getId());
//                switch(state) {
//				case Idle:
//					//stateMachine.sendEvent("TurnToSignal");
//					Message<String> msg = MessageBuilder
//						.withPayload(Signals.TurnToSignal.toString())
//						.setHeader("orientation", "2")
//						.build();
//					stateMachine.sendEvent(msg);
//					break;
//				case Resetting:
//					break;
//				case Stopped:
//					stateMachine.sendEvent("ResetSignal");
//					break;
//				case Stopping:
//					break;
//				default:
//					break;                
//                }

            }
        };
    }
	
	@Bean
	public Action<String, String> doSubBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				System.out.println("doSubBean triggered");
				context.getStateMachine().sendEvent("SubDone");
			}
		};
	}
	
	@Bean
	public Action<String, String> doWaitBean() {
		return new Action<String, String>() {

			@Override
			public void execute(StateContext<String, String> context) {
				System.out.println("waiting triggered");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
}