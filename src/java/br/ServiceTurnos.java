/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author byron
 */
@WebService(serviceName = "ServiceTurnos")
public class ServiceTurnos {

    @Resource(mappedName = "jms/MyQueue")
    private Queue myQueue;

    @Resource(mappedName = "jms/__defaultConnectionFactory")
    private ConnectionFactory myConnectionFactory;

    /**
     * This is a sample web service operation
     * @param txt
     * @return 
     */

    @WebMethod(operationName = "pedirTurno")
    public String pedirTurno(@WebParam(name = "servicio") String txt) {
        sendJMSMessageToMyQueue(txt);
        return "Turno para " + txt + " !";
    }

    private void sendJMSMessageToMyQueue(String messageData) {

        Connection connection = null;
        Session session = null;
        try {

            connection = myConnectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(myQueue);
            Message message_formated = createJMSMessageForjmsMyQueue(session, messageData);
            messageProducer.send(message_formated);
            if (connection != null) {
                connection.close();
            }

        } catch (JMSException ex) {
            Logger.getLogger(ServiceTurnos.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private Message createJMSMessageForjmsMyQueue(Session session, Object messageData) throws JMSException {
        TextMessage tm = session.createTextMessage();
        tm.setText(messageData.toString());
        return tm;
    }

}
