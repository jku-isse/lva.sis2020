package fsm.akka.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import fsm.example.config.Signals;

@SpringBootApplication
public class AkkaSpringApplication //implements CommandLineRunner 
{
	
    @Autowired
    private ApplicationContext applicationContext;
	
    @Autowired
    private ActorSystem system;
	
	public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AkkaSpringApplication.class, args);    
        ActorSystem system = (ActorSystem) context.getBean("actorSystem");
        ActorRef listener = system.actorOf(ListenerActor.props());
        MachineEventBus bus = (MachineEventBus) context.getBean("machineEventBus");
        bus.subscribe(listener, "*");
        ActorRef ttActor = ((ActorProducer) context.getBean("turntableActorProducer")).produce("TestActor1");
        ttActor.tell(new TurntableActor.BaseTurntableMessage(Signals.ResetSignal), ActorRef.noSender());
    }
	
//	@Override
//	public void run(String... args) throws Exception {
//	//	ActorRef turntable = produce("turntableActorProducer", "TT1");
//	//	turntable.tell(new TurntableActor.BaseTurntableMessage(Signals.ResetSignal), ActorRef.noSender());
//	}
	
	public ActorRef produce(String actorProducerBeanName, String actorInstanceName) {
		return ((ActorProducer) applicationContext.getBean(actorProducerBeanName)).produce(actorInstanceName);
	}
	
	public ActorSystem getSystem() {
		return system;
	}
}
