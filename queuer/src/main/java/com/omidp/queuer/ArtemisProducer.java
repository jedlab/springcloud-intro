package com.omidp.queuer;

import java.util.UUID;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class ArtemisProducer
{
    @Autowired
    JmsTemplate jmsTemplate;
    
    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;

    @Value("${jms.queue.destination}")
    String destinationQueue;
    
    @Value("${jms.queue.reply}")
    String replyQueue;
    
    

    public ApiResponseVO send(ApiValueHolder avo) throws JMSException
    {        
        jmsTemplate.setReceiveTimeout(3000L);
        jmsMessagingTemplate.setJmsTemplate(jmsTemplate);

        Session session = jmsMessagingTemplate.getConnectionFactory().createConnection()
                .createSession(false, Session.AUTO_ACKNOWLEDGE);

        ObjectMessage objectMessage = session.createObjectMessage(avo);
        objectMessage.setJMSCorrelationID(UUID.randomUUID().toString());
        objectMessage.setJMSReplyTo(new ActiveMQQueue(replyQueue));
        objectMessage.setJMSExpiration(3000L);
        objectMessage.setJMSDeliveryMode(DeliveryMode.NON_PERSISTENT);

        return jmsMessagingTemplate.convertSendAndReceive(new ActiveMQQueue(destinationQueue), objectMessage, ApiResponseVO.class);
    }
}