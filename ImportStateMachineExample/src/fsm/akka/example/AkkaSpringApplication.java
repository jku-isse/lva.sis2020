package fsm.akka.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import akka.actor.ActorRef;
import fsm.example.config.Signals;

@SpringBootApplication
public class AkkaSpringApplication implements CommandLineRunner {
	
    @Autowired
    private ApplicationContext applicationContext;
	
	
	public static void main(String[] args) {
        SpringApplication.run(AkkaSpringApplication.class, args);
    }
	
	@Override
	public void run(String... args) throws Exception {
		ActorRef turntable = produce("turntableActorProducer", "TT1");
		turntable.tell(new TurntableActor.BaseTurntableMessage(Signals.ResetSignal), ActorRef.noSender());
	}
	
	public ActorRef produce(String actorProducerBeanName, String actorInstanceName) {
		return ((ActorProducer) applicationContext.getBean(actorProducerBeanName)).produce(actorInstanceName);
	}
}
