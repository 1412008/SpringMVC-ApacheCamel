package web.configs;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.spring.javaconfig.CamelConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("web")
public class CamelConfig extends CamelConfiguration {
	@Autowired
	ActiveMQComponent activeMQComponent;
	
	protected void setupCamelContext(org.apache.camel.CamelContext camelContext) {
		camelContext.addComponent("activemq", activeMQComponent);
	}
}
