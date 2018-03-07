package fr.edjaz.chat.messaging;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public interface OpenChatChannel {
    String CHANNEL = "openChat";

    @Input
    SubscribableChannel subscribableChannel();

    @Output
    MessageChannel messageChannel();
}
