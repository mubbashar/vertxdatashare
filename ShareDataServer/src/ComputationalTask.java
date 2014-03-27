import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

public class ComputationalTask extends Verticle implements
		Handler<Message<JsonObject>> {

	public void start() {
		vertx.eventBus().registerHandler("com.tech.computation", this);
		System.out.println(vertx.isEventLoop() + "  is event loop");

	}

	public void stop() {
		vertx.eventBus().send("com.tech.computation" + ".unregister",
				new JsonObject() {
					{
						putString("status", "ok");
					}
				});
		vertx.eventBus().unregisterHandler("com.tech.computation", this);
	}

	@Override
	public void handle(final Message<JsonObject> message) {

		// TODO Auto-generated method stub
		String action = message.body().getString("messagetype");
		// if (logger.isDebugEnabled()) {
		// logger.debug("\n ** HANDLE ** " + this.toString()
		// + " (main handler) RECEIVED CALL " + action);
		// }
		if (action == null) {
			// sendError(message, "action must be specified");
		}
		switch (action) {
		case "userwanttohire":
			find(message);
			break;

		}
	}

	/****************************************************************************
	 ** 
	 ** Find
	 ** 
	 ****************************************************************************/

	private void find(Message<JsonObject> message) {
		// JsonObject receivedMessage = message.body();
		final Set<Customer> customerConnections = vertx.sharedData().getSet(
				"customers");
		Iterator<Customer> driverIte = customerConnections.iterator();
		while (driverIte.hasNext()) {
			System.out.println(driverIte.next().first_name + " yeyyeye ");
		}
		System.out.println(" Customers Map " + customerConnections + " \n");

		for (Customer temp : customerConnections) {

			System.out.println(" object type ==> <" + temp.getClass().getName()
					+ ">");
			if (temp instanceof Customer) {
				System.out.println(" \n\n Yes it is the instance of Customer");
			}

		}

		message.reply("\n\n Nothing Found ");

	}

}