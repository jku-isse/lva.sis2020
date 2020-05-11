package twofsm.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@SpringBootApplication
public class TwoSMApplication implements CommandLineRunner {

	@Autowired
	@Qualifier("stateMachineHierarchical")
	private StateMachine<String, String> stateMachineHierarchical;

	@Autowired
	@Qualifier("stateMachineRegular")
	private StateMachine<String, String> stateMachineRegular;

	public static void main(String[] args) {
		SpringApplication.run(TwoSMApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		
			System.out.println("Regular of Type: "+stateMachineRegular.getId());
			stateMachineRegular.addStateListener(listener());
			stateMachineRegular.start();	
		

	
			System.out.println("Hierarchical of Type: "+stateMachineHierarchical.getId());
			stateMachineHierarchical.addStateListener(listener());

			stateMachineHierarchical.start();
		
	}


	@Bean
	public StateMachineListener<String, String> listener() {
		return new StateMachineListenerAdapter<String, String>() {
			@Override
			public void stateChanged(State<String, String> from, State<String, String> to) {
				String fromId = from == null ? "N/A" : from.getId();
				System.out.println(String.format("State change from %s to %s ", fromId, to.getId()));
				            	if (to.getId().equals("Starting")) {
									stateMachineHierarchical.sendEvent("InputBeforeSub");
				            	} else if (to.getId().equals("WaitForInput")) {
									stateMachineHierarchical.sendEvent("InputToSub");
				            	}
			}
		};
	}

	
}