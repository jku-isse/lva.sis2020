package fsm.akka.example;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import akka.actor.ActorSystem;

@Configuration
@ComponentScan
public class AppConfiguration {

	@Bean
	@Scope("singleton")
	public ActorSystem actorSystem() {
		ActorSystem system = ActorSystem.create("akka-spring-demo");
		return system;
	}

	@Bean 
	@Scope("singleton")
	public MachineEventBus machineEventBus() {
		return new MachineEventBus();
	}
}
