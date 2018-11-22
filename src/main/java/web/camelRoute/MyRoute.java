package web.camelRoute;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class MyRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		from("direct:cm.create").process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setHeader("test", "test");
				exchange.getOut().setHeader("JMSCorrelationID", "createc");
				exchange.getOut().setBody(exchange.getIn().getBody());
			}
		}).to("activemq:queue:cm.create");

		from("direct:cm.update").process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setHeader("JMSCorrelationID", "updatec");
				exchange.getOut().setBody(exchange.getIn().getBody());
			}
		}).to("activemq:queue:cm.update");

		from("direct:cm.delete").process(new Processor() {
			public void process(Exchange exchange) throws Exception {
				exchange.getOut().setHeader("JMSCorrelationID", "deletec");
				exchange.getOut().setBody(exchange.getIn().getBody());
			}
		}).to("activemq:queue:cm.delete");
	}

}
