package web.jms;

import javax.jms.Message;
import javax.jms.ObjectMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import web.daos.SwitchRepository;
import web.models.Switch;

@Component
public class Listener {

	@Autowired
	SwitchRepository swrepo;

	@JmsListener(destination = "cm.create")
	public void create_switch(Message objMessage) {
		if (objMessage instanceof ObjectMessage) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) objMessage;
				Switch sw = (Switch) objectMessage.getObject();
				if (!sw.IsNullOrEmpty()) {
					System.out.println(sw);
					sw.setId(null);
					String err = sw.checkData();
					if (err.equals("")) {
						System.out.println("ok");
						if (swrepo.insert(sw) != null) {
							System.out.println("Insert success!");
						}
					} else {
						System.out.println(err);
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@JmsListener(destination = "cm.update")
	private void updateSwitch(Message objMessage) {
		if (objMessage instanceof ObjectMessage) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) objMessage;
				Switch body = (Switch) objectMessage.getObject();
				String err = body.checkData();
				if (err.equals("")) {
					String mac = body.getMAC();
					if (!StringUtils.isEmpty(mac)) {
						mac = normalizeMAC(mac);
						Switch sw = swrepo.findByMAC(mac);
						if (sw != null) {
							System.out.println("Before update: " + sw.toString());
							if (!StringUtils.isEmpty(body.getName())) {
								sw.setName(body.getName());
							}
							if (!StringUtils.isEmpty(body.getType())) {
								sw.setType(body.getType());
							}
							if (!StringUtils.isEmpty(body.getAddress())) {
								sw.setAddress(body.getAddress());
							}
							if (!StringUtils.isEmpty(body.getVersion())) {
								sw.setVersion(body.getVersion());
							}
							try {
								if (swrepo.save(sw) != null) {
									System.out.println("New: " + sw.toString());
								} else {
									System.out.println("Update failed.");
								}
							} catch (Exception e) {
								System.out.println(e.getMessage());
							}
						} else {
							System.out.println("This switch doesn't exist!");
						}
					}
				} else {
					System.out.println(err);
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@JmsListener(destination = "cm.delete")
	private void deleteSwitch(Message objMessage) {
		if (objMessage instanceof ObjectMessage) {
			try {
				ObjectMessage objectMessage = (ObjectMessage) objMessage;
				Switch body = (Switch) objectMessage.getObject();
				String mac = body.getMAC();
				if (!StringUtils.isEmpty(mac)) {
					mac = normalizeMAC(mac);
					Switch sw = swrepo.findByMAC(mac);
					try {
						if (sw != null) {
							System.out.println("Delete " + sw.toString());
							swrepo.delete(sw);
						}
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	private String normalizeMAC(String mac) {
		return mac.replaceAll("\\-", ":");
	}

}
