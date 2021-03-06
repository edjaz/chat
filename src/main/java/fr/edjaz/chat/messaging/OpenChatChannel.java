package fr.edjaz.chat.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OpenChatChannel {

    @Input
    SubscribableChannel hasFreeChat();

    @Input
    SubscribableChannel waitClientSubscribe();

    @Input
    SubscribableChannel waitForFreeChat();

    @Output
    MessageChannel openChat();

    @Output
    MessageChannel conseillerFreeForChat();

}
