package fr.edjaz.chat.web.rest;

import org.springframework.http.MediaType;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.UUID;


@RestController
@RequestMapping("/api")
public class TestController {


/*
    private final ProducerChannel producerChannel;
    private final PublishSubscribeChannel subscribeChannel;

    public TestController(ProducerChannel producerChannel, PublishSubscribeChannel subscribeChannel) {
        this.producerChannel = producerChannel;
        this.subscribeChannel = subscribeChannel;
    }


    @GetMapping(value = "/chat/{idChat}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Message> flux(@PathVariable Long idChat) {
        return Flux.create(fluxSink -> subscribeChannel.subscribe(message -> {
            Message msg = (Message) message.getPayload();
            if (msg.getIdChat().equals(idChat)) {
                fluxSink.next(msg);
            }
        }));
    }

    @GetMapping("/chat/post/{idChat}")
    public void post(@PathVariable Long idChat) {

        Message msg = new Message();
        msg.setId(0L);
        msg.setIdChat(idChat);
        msg.setText(UUID.randomUUID().toString());

        producerChannel.messageChannel().sendMessage(MessageBuilder.withPayload(msg).build());
    }
*/

}
