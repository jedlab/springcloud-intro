package com.omidp.queuer;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.artemis.jms.client.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ArtemisConsumer implements SessionAwareMessageListener<Message>
{

    @Autowired
    DiscoveryClient dc;

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    public ResponseEntity<String> receive(ApiValueHolder avo)
    {
        List<ServiceInstance> instances = dc.getInstances(avo.getServiceName());
        if (instances != null && instances.size() > 0)
        {
            log.info("found {} many instance(s) for {}", instances.size(), avo.getServiceName());
            ServiceInstance serviceInstance = instances.iterator().next();
            String serviceId = serviceInstance.getServiceId();
            String url = String.format("http://%s%s", serviceId, avo.getPath());
            log.info("send {} to {} with payload {} ", avo.getMethod().name(), url, avo.getPayload());

            restTemplate.setErrorHandler(new NoOpResponseErrorHandler());
            HttpMethod method = avo.getMethod() == null ? HttpMethod.POST : avo.getMethod();
            HttpEntity<?> requestEntity = new HttpEntity<>(avo.getPayload(), getHeaders());

            switch (method)
            {
            case POST:
                ResponseEntity<String> post = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
                return post;
            case GET:
                ResponseEntity<String> get = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
                return get;
            case PUT:
                ResponseEntity<String> put = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, String.class);
                return put;
            case DELETE:
                ResponseEntity<String> del = restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, String.class);
                return del;
            default:
                break;
            }

        }
        return null;
    }

    private MultiValueMap<String, String> getHeaders()
    {
        MultiValueMap<String, String> m = new LinkedMultiValueMap<>();
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        m.setAll(map);
        return m;
    }

    public static class NoOpResponseErrorHandler extends DefaultResponseErrorHandler
    {

        @Override
        public void handleError(ClientHttpResponse response) throws IOException
        {
            log.info("ERROR {}", response.getStatusText());
        }

    }

    @Override
    @JmsListener(destination = "${jms.queue.destination}")
    public void onMessage(Message message, Session session) throws JMSException
    {
        ActiveMQObjectMessage obj = ((ActiveMQObjectMessage) message);
        ApiValueHolder avo = (ApiValueHolder) obj.getObject();
        System.out.println(avo.toString());
        // done handling the request, now create a response message
        ResponseEntity<String> receive = receive(avo);
        ApiResponseVO resp = new ApiResponseVO(404, "Not found");
        if(receive != null)
            resp = new ApiResponseVO(receive.getStatusCodeValue(), receive.getBody());
        ObjectMessage responseMessage = session.createObjectMessage(resp);
        responseMessage.setJMSCorrelationID(message.getJMSCorrelationID());

        // Message sent back to the replyTo address of the income message.
        final MessageProducer producer = session.createProducer(message.getJMSReplyTo());
        producer.send(responseMessage);
    }

}