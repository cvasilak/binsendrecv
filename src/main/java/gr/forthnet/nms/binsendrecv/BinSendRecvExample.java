package gr.forthnet.nms.binsendrecv;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class BinSendRecvExample {

	private Context ctx;
	private QueueConnectionFactory factory;
	private QueueConnection connection;

	public BinSendRecvExample() throws JMSException, NamingException {
		this.ctx = new InitialContext();

		System.out.print("retrieving /RemoteConnectionFactory...");
		this.factory = (QueueConnectionFactory) ctx.lookup("RemoteConnectionFactory");
		System.out.println("retrieved");
		this.connection = factory.createQueueConnection();
		System.out.println("established connection to server");
	}

	public void test() throws JMSException, NamingException {
		QueueSession session = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);

		Queue queue = (Queue)ctx.lookup("/queue/fetchGRAPH");
		
		connection.start();
		
		System.console().readLine("Press <enter> to send message:");
		
		TextMessage message = session.createTextMessage();
		
		message.setText("Hello World!");
		
		QueueSender sender = session.createSender(queue);
		sender.send(message);
		
		System.out.println("message sent.");
		
		System.console().readLine("Press <enter> to exit");
		
		connection.close();
	}

	public static void main(String[] args) {

		try {
			new BinSendRecvExample().test();
		} catch (JMSException jmse) {
			jmse.printStackTrace();
			System.exit(1);
		} catch (NamingException jne) {
			jne.printStackTrace();
			System.exit(1);
		}

	}

}
