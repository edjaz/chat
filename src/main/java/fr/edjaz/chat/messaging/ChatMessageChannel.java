package fr.edjaz.chat.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface ChatMessageChannel {
    @Input
    SubscribableChannel waitClient();

    @Input
    SubscribableChannel waitConseiller();

    @Output
    MessageChannel sendToClient();

    @Output
    MessageChannel sendToConseiller();
}
