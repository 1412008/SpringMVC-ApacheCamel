package web.camelRoute;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:cm.create").to("activemq:queue:cm.create");
		from("direct:cm.update").to("activemq:queue:cm.update");
		from("direct:cm.delete").to("activemq:queue:cm.delete");
	}

}
