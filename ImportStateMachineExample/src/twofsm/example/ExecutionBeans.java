package twofsm.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Configuration
public class ExecutionBeans {
	
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
