package fr.edjaz.chat.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import com.codahale.metrics.annotation.Timed;
import fr.edjaz.chat.domain.enumeration.MessageStatus;
import fr.edjaz.chat.messaging.ChatMessageChannel;
import fr.edjaz.chat.security.SecurityUtils;
import fr.edjaz.chat.service.ConseillerService;
import fr.edjaz.chat.service.MessageService;
import fr.edjaz.chat.service.dto.ConseillerDTO;
import fr.edjaz.chat.service.dto.MessageDTO;
import fr.edjaz.chat.web.rest.errors.BadRequestAlertException;
import fr.edjaz.chat.web.rest.util.HeaderUtil;
import fr.edjaz.chat.web.rest.util.PaginationUtil;
import fr.edjaz.chat.web.rest.vm.MessageClientVM;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * REST controller for managing Message.
 */
@RestController
@RequestMapping("/api")
public class MessageResource {

    private final Logger log = LoggerFactory.getLogger(MessageResource.class);

    private static final String ENTITY_NAME = "message";

    private final MessageService messageService;

    private final ChatMessageChannel chatMessageChannel;

    private final ConseillerService conseillerService;

    public MessageResource(MessageService messageService, ChatMessageChannel chatMessageChannel, ConseillerService conseillerService) {
        this.messageService = messageService;
        this.chatMessageChannel = chatMessageChannel;
        this.conseillerService = conseillerService;
    }


    @PostMapping("/chats/{idChat}/client/{idClient}/messages")
    @Timed
    public void sendClientMessage(@PathVariable Long idChat, @PathVariable Long idClient, @RequestBody MessageClientVM message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);


        MessageDTO messageDTO = new MessageDTO();

        if (message.getId() == null) {
            messageDTO.setId(message.getId());
        }

        messageDTO.setChatId(idChat);
        messageDTO.setCreated(Instant.now());
        if (message.isValidated()) {
            messageDTO.setSent(Instant.now());
            messageDTO.setStatus(MessageStatus.DONE);
        } else {
            messageDTO.setStatus(MessageStatus.IN_PROGRESS);
        }
        messageDTO.setWriteByClientId(idClient);

        messageDTO = messageService.save(messageDTO);

        chatMessageChannel.sendToConseiller().send(MessageBuilder.withPayload(messageDTO).build());
    }


    @PostMapping("/chats/{idChat}/conseiller/messages")
    @Timed
    public void sendConseillerMessage(@PathVariable Long idChat, @RequestBody MessageClientVM message) throws URISyntaxException {
        log.debug("REST request to save Message : {}", message);

        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        ConseillerDTO conseiller = conseillerService.findByLogin(currentUserLogin.get());

        MessageDTO messageDTO = new MessageDTO();

        messageDTO.setChatId(idChat);
        messageDTO.setCreated(Instant.now());
        messageDTO.setSent(Instant.now());
        messageDTO.setStatus(MessageStatus.DONE);
        messageDTO.setWriteByConseillerId(conseiller.getId());

        messageService.save(messageDTO);
        chatMessageChannel.sendToClient().send(MessageBuilder.withPayload(messageDTO).build());

    }


    @GetMapping("/chats/{idChat}/client/{idClient}/messages")
    @Timed
    public Flux<ServerSentEvent<MessageDTO>> clientReadMessage(@PathVariable Long idChat) {
        return Flux.create(sink -> chatMessageChannel.waitConseiller().subscribe(message -> {
            MessageDTO msg = (MessageDTO) message.getPayload();
            if (msg.getChatId().equals(idChat)) {
                sink.next(ServerSentEvent.builder(msg).build());
            }
        }));
    }


    @GetMapping("/chats/{idChat}/conseiller/messages")
    @Timed
    public Flux<ServerSentEvent<MessageDTO>> conseillerReadMessage(@PathVariable Long idChat) {
        return Flux.create(sink -> chatMessageChannel.waitClient().subscribe(message -> {
            MessageDTO msg = (MessageDTO) message.getPayload();
            if (msg.getChatId().equals(idChat)) {
                sink.next(ServerSentEvent.builder(msg).build());
            }
        }));
    }


    /**
     * POST  /messages : Create a new message.
     *
     * @param messageDTO the messageDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new messageDTO, or with status 400 (Bad Request) if the message has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/messages")
    @Timed
    public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to save Message : {}", messageDTO);
        if (messageDTO.getId() != null) {
            throw new BadRequestAlertException("A new message cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MessageDTO result = messageService.save(messageDTO);
        return ResponseEntity.created(new URI("/api/messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /messages : Updates an existing message.
     *
     * @param messageDTO the messageDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated messageDTO,
     * or with status 400 (Bad Request) if the messageDTO is not valid,
     * or with status 500 (Internal Server Error) if the messageDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/messages")
    @Timed
    public ResponseEntity<MessageDTO> updateMessage(@RequestBody MessageDTO messageDTO) throws URISyntaxException {
        log.debug("REST request to update Message : {}", messageDTO);
        if (messageDTO.getId() == null) {
            return createMessage(messageDTO);
        }
        MessageDTO result = messageService.save(messageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, messageDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /messages : get all the messages.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of messages in body
     */
    @GetMapping("/messages")
    @Timed
    public ResponseEntity<List<MessageDTO>> getAllMessages(Pageable pageable) {
        log.debug("REST request to get a page of Messages");
        Page<MessageDTO> page = messageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /messages/:id : get the "id" message.
     *
     * @param id the id of the messageDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the messageDTO, or with status 404 (Not Found)
     */
    @GetMapping("/messages/{id}")
    @Timed
    public ResponseEntity<MessageDTO> getMessage(@PathVariable Long id) {
        log.debug("REST request to get Message : {}", id);
        MessageDTO messageDTO = messageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(messageDTO));
    }

    /**
     * DELETE  /messages/:id : delete the "id" message.
     *
     * @param id the id of the messageDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/messages/{id}")
    @Timed
    public ResponseEntity<Void> deleteMessage(@PathVariable Long id) {
        log.debug("REST request to delete Message : {}", id);
        messageService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/messages?query=:query : search for the message corresponding
     * to the query.
     *
     * @param query    the query of the message search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/messages")
    @Timed
    public ResponseEntity<List<MessageDTO>> searchMessages(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Messages for query {}", query);
        Page<MessageDTO> page = messageService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/messages");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
