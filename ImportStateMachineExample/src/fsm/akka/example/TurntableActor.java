package fsm.akka.example;

import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import fsm.example.config.Signals;
import fsm.example.config.States;

public class TurntableActor extends AbstractActor {

	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	

	private InternalState state;
	
	
	public static Props props(InternalState stateMachine, MachineEventBus bus) {
		return Props.create(TurntableActor.class, stateMachine, bus);
	}
	
	public TurntableActor(InternalState stateMachine, MachineEventBus bus) {
		this.state = stateMachine;
		state.getStateMachine().addStateListener(listener(bus));
		state.getStateMachine().start();
	}
	
	@Override
	public Receive createReceive() {
		return receiveBuilder()
				.match(
		        	BaseTurntableMessage.class, msg -> {
		              state.getStateMachine().sendEvent(msg.convertToInternalMessage());
		            })
		        .matchAny(o -> log.info("received unknown message"))
		        .build();
	}
	


	public static class BaseTurntableMessage {
		protected Signals type;

		public Signals getType() {
			return type;
		}
		
		protected Message<String> convertToInternalMessage() {
			return MessageBuilder
			.withPayload(type.toString())
			.build();
		}

		public void setType(Signals type) {
			this.type = type;
		}

		public BaseTurntableMessage(Signals type) {
			super();
			this.type = type;
		}
		
	}
	
	public static class TurnRequest extends BaseTurntableMessage {
		private Integer position;
		
		public TurnRequest(int position) {
			super(Signals.TurnToSignal);
			this.position = position;
		}
		
		@Override
		protected Message<String> convertToInternalMessage() {
			return MessageBuilder
			.withPayload(type.toString())
			.setHeader("orientation", position)
			.build();
		}

		public Integer getPosition() {
			return position;
		}
	}
	
	public static class TurntableStateChangeEvent {
		private String turntableId;
		private States state;
		public String getTurntableId() {
			return turntableId;
		}
		public States getState() {
			return state;
		}
		public TurntableStateChangeEvent(String turntableId, States state) {
			super();
			this.turntableId = turntableId;
			this.state = state;
		}
	}
	

    private StateMachineListener<String, String> listener(MachineEventBus bus) {
        return new StateMachineListenerAdapter<String, String>() {
        	
        	private MachineEventBus bus;
        	
        	public StateMachineListenerAdapter<String, String> setEventBus(MachineEventBus bus) {
        		this.bus = bus;
        		return this;
        	};
        	
            @Override
            public void stateChanged(State<String, String> from, State<String, String> to) {
                log.info("State change to " + to.getId());
                States state = States.valueOf(to.getId());
                bus.publish(new TurntableStateChangeEvent("TT1", state));
            }
        }.setEventBus(bus);
    }
	

    
}
