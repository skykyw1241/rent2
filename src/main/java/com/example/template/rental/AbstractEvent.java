package com.example.template.rental;

import com.example.template.Application;
import com.example.template.book.KafkaProcessorBook;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class AbstractEvent {
    protected String eventType;
    protected long id;
    String timestamp;

    public String getEventType() { return eventType; }
    public String getTimestamp() { return timestamp; }

    public void publish() throws Exception{
        publishToMessageChannel(this.getMessageChannel(), this.toJson());
    }

    public void publishToMessageChannel(MessageChannel output, String contents){
        if(contents == null){
            throw new RuntimeException("contents is null");
        }
        if(output == null){
            throw new RuntimeException("Target MessageChannel is null");
        }

        output.send(MessageBuilder
                .withPayload(contents)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());
    }

    public String toJson() throws Exception{
        ObjectMapper om = new ObjectMapper();
        String json = null;

        try{
            json = om.writeValueAsString(this);
            System.out.println(json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("JSON format exception", e);
        }
        return json;
    }

    private MessageChannel getMessageChannel(){
        MessageChannel output = null;
        this.eventType = this.getClass().getSimpleName();
        SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
        this.timestamp = defaultSimpleDateFormat.format(new Date());

        //KafkaProcessorRental processor = (KafkaProcessorRental) Application.getBeanForProcessor();
        KafkaProcessorRental processor = Application.applicationContext.getBean(KafkaProcessorRental.class);
        Class cls = processor.getClass();
        Method[] methods = cls.getDeclaredMethods();
        try{
            Method method = cls.getMethod("outbound"+this.getClass().getSimpleName());
            output = (MessageChannel) method.invoke(processor);
        }catch(Exception e) {
            e.printStackTrace();
            output = null;
        }
        return output;
    }
}
