package fr.edjaz.chat.config;

import java.text.SimpleDateFormat;
import java.util.Date;

import fr.edjaz.chat.messaging.ChatMessageChannel;
import fr.edjaz.chat.messaging.OpenChatChannel;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageBuilder;

/**
 * Configures Spring Cloud Stream support.
 *
 * This works out-of-the-box if you use the Docker Compose configuration at "src/main/docker/kafka.yml".
 *
 * See http://docs.spring.io/spring-cloud-stream/docs/current/reference/htmlsingle/
 * for the official Spring Cloud Stream documentation.
 */
@EnableBinding(value = { Source.class , ChatMessageChannel.class, OpenChatChannel.class})
public class MessagingConfiguration {


    /**
     * This sends a test message at regular intervals.
     *
     * In order to see the test messages, you can use the Kafka command-line client:
     * "./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic topic-jhipster --from-beginning".
     */
    @Bean
    @InboundChannelAdapter(value = Source.OUTPUT)
    public MessageSource<String> timerMessageSource() {
        return () -> new GenericMessage<>("Test message from JHipster sent at " +
            new SimpleDateFormat().format(new Date()));
    }


    @Bean
    public PublishSubscribeChannel broadCastHasFreeChat(){
        return MessageChannels.publishSubscribe().get();
    }

    @Bean
    public PublishSubscribeChannel broadCastChatMessageChannel(){
        return MessageChannels.publishSubscribe().get();
    }


    @StreamListener("hasFreeChat")
    public void hasFreeChat(Message message) {
        broadCastHasFreeChat().send(MessageBuilder.withPayload(message).build());
    }

    @StreamListener("readMessage")
    public void chatMessageChannel(Message message) {
        broadCastChatMessageChannel().send(MessageBuilder.withPayload(message).build());
    }

}
