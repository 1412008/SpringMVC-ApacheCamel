package web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import web.daos.SwitchRepository;
import web.models.Switch;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
@RequestMapping("/api")
public class SwitchController3 {

	@Autowired
	SwitchRepository swRepo;

	@Autowired
	CamelContext camelContext;

	@RequestMapping(value = { "switches" }, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<Switch> getAll() {
		List<Switch> switches = swRepo.findAll();
		return switches;
	}

	@RequestMapping(value = "switch", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Switch getOne(HttpServletRequest req) {
		String mac = req.getParameter("MAC");
		if (!StringUtils.isEmpty(mac)) {
			mac = normalizeMAC(mac);
			Switch sw = swRepo.findByMAC(mac);
			return sw;
		}
		return null;
	}

	@RequestMapping(value = "switch", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> createSwitch(@RequestBody Switch body, HttpServletRequest req) {
		ProducerTemplate pt = camelContext.createProducerTemplate();
		System.out.println("Creating Switch...");
		String destination = "direct:cm.create";
		System.out.println("Send message to " + destination);
		pt.sendBody(destination, body);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@RequestMapping(value = "switch", method = RequestMethod.PUT)
	public ResponseEntity<HttpStatus> updateSwitch(@RequestBody Switch body) {
		ProducerTemplate pt = camelContext.createProducerTemplate();
		System.out.println("Update Switch...");
		String destination = "direct:cm.update";
		System.out.println("Send message to " + destination);
		pt.sendBody(destination, body);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	@RequestMapping(value = "switch", method = RequestMethod.DELETE)
	public ResponseEntity<HttpStatus> deleteSwitch(@RequestBody Switch body) {
		ProducerTemplate pt = camelContext.createProducerTemplate();
		System.out.println("Delete Switch...");
		String destination = "direct:cm.delete";
		System.out.println("Send message to " + destination);
		pt.sendBody(destination, body);
		return ResponseEntity.ok(HttpStatus.OK);
	}

	private String normalizeMAC(String mac) {
		return mac.replaceAll("\\-", ":");
	}
}
