import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.core.net.NetSocket;

public class SocketMessageHandler implements
		org.vertx.java.core.Handler<Buffer> {

	NetSocket socket;
	Vertx vertx;
	String socketMessage;

	public SocketMessageHandler(NetSocket socket, Vertx vertx) {
		this.socket = socket;
		this.vertx = vertx;
	}

	@Override
	public void handle(Buffer buffer) {
		System.out.println("Message Received : " + buffer.toString());
		socketMessage = buffer.toString();
		JsonObject receivedMessage = null;
		try {
			receivedMessage = new JsonObject(buffer.toString());
			/*
			 * Check type of the message
			 */
			String userType = receivedMessage.getString("usertype");
			if (userType.equalsIgnoreCase("customer")) {

				// String email = receivedMessage.getString("email");
				// String password = receivedMessage.getField("password");
				/*
				 * vertx.eventBus().send( "com.bloidonia.jdbcpersistor",
				 * MetroDbQuries.getSelectJsonFromQuery(MetroDbQuries
				 * .getDriverInfo(email, password)), driverDbDataHandler);
				 */
				vertx.eventBus().send("com.tech.computation", receivedMessage,
						hireDataHandler);

			} else {
				System.out
						.println("A Unknown User came : " + buffer.toString());
			}

		} catch (Exception e) {
			System.out.println("Unknown Message Received : "
					+ buffer.toString());
		}
	}

	/*
	 * hire
	 */
	Handler<Message<JsonObject>> hireDataHandler = new Handler<Message<JsonObject>>() {

		@Override
		public void handle(Message<JsonObject> message) {
			System.out.println("\n\n Back from Hire -- " + message.body()
					+ "\n\n");
		}

	};

}
