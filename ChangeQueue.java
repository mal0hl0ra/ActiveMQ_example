package kkonev_ActiveMQ_java;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class ChangeQueue {

    // URL нашего ActiveMQ сервера.
    private static String server_url = "failover://tcp://localhost:61616";

    // queue_1_name = задаем имя для первой очереди.
    // queue_2_name = задаем имя для второй очереди.
    private static String queue_1_name = "queue_test_1";
    private static String queue_2_name = "queue_test_55";

    public static void main(String[] args) throws JMSException {

        // Узнаем 'DEFAULT_BROKER_URL'/.
        System.out.println("'DEFAULT_BROKER_URL' = " + ActiveMQConnection.DEFAULT_BROKER_URL);

        // Getting JMS connection from the server and starting it.
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(server_url);
        Connection connection = connectionFactory.createConnection();
        connection.start();

        //Creating a non transactional session to send/receive JMS message.
        Session session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);

        //Destination represents here our queue 'queue_test_1' on the JMS server.
        //The queue will be created automatically on the server.
        Destination destination = session.createQueue(queue_1_name);

        // MessageConsumer is used for receiving (consuming) messages.
        MessageConsumer consumer = session.createConsumer(destination);

        // Here we receive the message.
        Message message = consumer.receive();

        // Выводим в консоль, что из очереди (указывается очередь) было получено сообщение с текстом (указывается текст).
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Из очереди '" + queue_1_name + "' было получено cообщение с текстом '" + textMessage.getText() + "'");
        }

        //Destination represents here our queue 'queue_test_55' on the JMS server.
        //The queue will be created automatically on the server.
        Destination destination_2 = session.createQueue(queue_2_name);

        // MessageProducer is used for sending messages to the queue.
        MessageProducer producer = session.createProducer(destination_2);

        // Here we are sending our message!
        producer.send(message);

        // Выводим в консоль, что сообщение с текстом (указывается текст) было отправлено в очередь (указывается очередь).
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            System.out.println("Сообщение с текстом '" + textMessage.getText() + "' было отправлено в очередь '" + queue_2_name + "'");

        }
        connection.close();
    }
}