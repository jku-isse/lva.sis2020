package twofsm.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.model.DefaultStateMachineComponentResolver;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.uml.UmlStateMachineModelFactory;

@Configuration
@EnableStateMachine(name="stateMachineRegular")
public class TurntableStatemachineConfig extends StateMachineConfigurerAdapter<String,String> {

private ApplicationContext beanFactory;
	
	@Autowired
	TurntableStatemachineConfig(ApplicationContext beanFactory) {
		this.beanFactory = beanFactory;
	}
	
    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration().autoStartup(false).machineId("TypeStateMachineRegular");
    }
	
	@Override
	public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
		model
        .withModel()
            .factory(modelFactoryReg());
	}

	public StateMachineModelFactory<String, String> modelFactoryReg() {
		String remote = "file:..\\StateMachineExample\\StateMachineExample.uml";
		UmlStateMachineModelFactory factory = new UmlStateMachineModelFactory(remote);
		factory.setStateMachineComponentResolver(new DefaultStateMachineComponentResolver<String, String>(beanFactory, null, null));
		return factory;
    }
	

}