/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.util.Set;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.VoidHandler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.net.NetSocket;
import org.vertx.java.platform.Verticle;

public class Server extends Verticle {

	public void start() {

		final Set<Customer> customersConnections = vertx.sharedData().getSet(
				"customers");

		container.deployWorkerVerticle(ComputationalTask.class.getName()
				+ ".java", null, 1, true, new Handler<AsyncResult<String>>() {
			@Override
			public void handle(AsyncResult<String> asyncResult) {
				System.out.println("Deployed ComputionalTask worker vertical "
						+ asyncResult.succeeded());
				// TODO Auto-generated method stub
				if (asyncResult.succeeded()) {
					System.out
							.println("The verticle has been deployed, deployment ID is "
									+ asyncResult.result());
				} else {
					asyncResult.cause().printStackTrace();
				}

			}
		});

		/*
		 * Register task
		 */
		vertx.createNetServer().connectHandler(socketHandler).listen(1234);

		vertx.setPeriodic(50000, new Handler<Long>() {
			@Override
			public void handle(Long time) {
				if (!customersConnections.isEmpty()) {
					vertx.eventBus().publish("locupdate", "hello");
				}

			}
		});

	}

	Handler<NetSocket> socketHandler = new Handler<NetSocket>() {

		@Override
		public void handle(final NetSocket socket) {
			// TODO Auto-generated method stub
			System.out.println(socket.writeHandlerID() + " COnnection open ");
			System.out.println("Sending message : Please tell me who are you?");
			socket.write("Welcome Here?");
			/*
			 * Message Received Hander
			 */
			final Set<Customer> customersConnections = vertx.sharedData()
					.getSet("customers");
			/* Just for the testing purpose */
			Customer obj = new Customer();
			obj.first_name = "Mubbashar";
			obj.last_name = "Husain";
			obj.socketid = socket.writeHandlerID();

			customersConnections.add(obj);
			socket.dataHandler(new SocketMessageHandler(socket, vertx));
			socket.closeHandler(disconnectHandlerFunction(vertx));

		}
	};

	public Handler<Buffer> messageReceivedHandlerFunction(final NetSocket socket) {
		Handler<Buffer> onDutyHandler = new Handler<Buffer>() {
			public void handle(Buffer message) {
				// TODO Auto-generated method stub
				socket.write(message.toString());
			}
		};
		return onDutyHandler;
	}

	public Handler<Void> disconnectHandlerFunction(Vertx vertx) {
		Handler<Void> onDisconnectHandler = new VoidHandler() {
			@Override
			protected void handle() {

			}
		};
		return onDisconnectHandler;
	}

}
