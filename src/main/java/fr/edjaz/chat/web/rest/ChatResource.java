package fr.edjaz.chat.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.edjaz.chat.domain.enumeration.ChatStatus;
import fr.edjaz.chat.domain.enumeration.MessageStatus;
import fr.edjaz.chat.messaging.ChatMessageChannel;
import fr.edjaz.chat.messaging.OpenChatChannel;
import fr.edjaz.chat.security.AuthoritiesConstants;
import fr.edjaz.chat.security.SecurityUtils;
import fr.edjaz.chat.service.ChatService;
import fr.edjaz.chat.service.ClientService;
import fr.edjaz.chat.service.ConseillerService;
import fr.edjaz.chat.service.MessageService;
import fr.edjaz.chat.service.dto.ChatDTO;
import fr.edjaz.chat.service.dto.ClientDTO;
import fr.edjaz.chat.service.dto.ConseillerDTO;
import fr.edjaz.chat.service.dto.MessageDTO;
import fr.edjaz.chat.web.rest.errors.BadRequestAlertException;
import fr.edjaz.chat.web.rest.util.HeaderUtil;
import fr.edjaz.chat.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Chat.
 */
@RestController
@RequestMapping("/api")
public class ChatResource {

    private final Logger log = LoggerFactory.getLogger(ChatResource.class);

    private static final String ENTITY_NAME = "chat";

    private final ChatService chatService;

    private final ConseillerService conseillerService;

    private final OpenChatChannel openChatChannel;

    private final ClientService clientService;

    private final PublishSubscribeChannel broadCastHasFreeChat;


    public ChatResource(ChatService chatService, ConseillerService conseillerService, OpenChatChannel openChatChannel, ClientService clientService, PublishSubscribeChannel broadCastHasFreeChat) {
        this.chatService = chatService;
        this.conseillerService = conseillerService;
        this.openChatChannel = openChatChannel;
        this.clientService = clientService;
        this.broadCastHasFreeChat = broadCastHasFreeChat;
    }


    @GetMapping(value = "/chats/client/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent> subscribeClient() {
        return Flux.create(sink -> {

            if (chatService.hasFreeChat()) {
                sink.next(ServerSentEvent.builder().event("free").build());
            } else {
                sink.next(ServerSentEvent.builder().event("notAvailable").build());
                broadCastHasFreeChat.subscribe(message -> sink.next(ServerSentEvent.builder().event("free").build()));
            }

        });
    }


    @GetMapping(value = "/chats/client/open", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatDTO> openClient() {

        return Flux.create(sink -> {
            Optional<ChatDTO> freeChat = chatService.findFreeChat();

            ClientDTO client = new ClientDTO();
            client = clientService.save(client);

            SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("anonymous", client, Collections.singletonList(new SimpleGrantedAuthority(AuthoritiesConstants.ANONYMOUS))));

            if (freeChat.isPresent()) {
                ChatDTO chatDTO = freeChat.get();
                chatDTO.setClientId(client.getId());
                chatService.save(chatDTO);
                openChatChannel.openChat().send(MessageBuilder.withPayload(chatDTO).build());
                sink.next(chatDTO);
                sink.complete();
            } else {

                // TODO :
/*                ClientDTO finalClient = client;
                openChatChannel.waitForFreeChat().subscribe(message -> {
                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.findAndRegisterModules();

                    ChatDTO chatDTO = null;
                    try {
                        chatDTO = objectMapper.readValue((byte[]) message.getPayload(), ChatDTO.class);
                        chatDTO.setClientId(finalClient.getId());
                        chatDTO = chatService.save(chatDTO);
                        sink.next(chatDTO);
                        sink.complete();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });*/
            }
        });
    }





    @GetMapping(value = "/chats/conseiller/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatDTO> subscribeConseiller() {
        return Flux.create(sink -> {
            // 1. create new chat

            String loginConseiller = SecurityUtils.getCurrentUserLogin().get();

            ChatDTO chatDTO = new ChatDTO();
            chatDTO.setCreated(Instant.now());
            chatDTO.setConseillers(Collections.singleton(conseillerService.findByLogin(loginConseiller)));
            chatDTO.setStatus(ChatStatus.WAITTING);
            chatDTO = chatService.save(chatDTO);

            openChatChannel.conseillerFreeForChat().send(MessageBuilder.withPayload(chatDTO).build());
            // 2.wait un Client
            openChatChannel.waitClientSubscribe().subscribe(message -> {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.findAndRegisterModules();
                try {
                    ChatDTO chat = objectMapper.readValue((byte[]) message.getPayload(), ChatDTO.class);
                    sink.next(chat);
                    sink.complete();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            });
        });
    }







    /**
     * POST  /chats : Create a new chat.
     *
     * @param chatDTO the chatDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new chatDTO, or with status 400 (Bad Request) if the chat has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/chats")
    @Timed
    public ResponseEntity<ChatDTO> createChat(@RequestBody ChatDTO chatDTO) throws URISyntaxException {
        log.debug("REST request to save Chat : {}", chatDTO);
        if (chatDTO.getId() != null) {
            throw new BadRequestAlertException("A new chat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChatDTO result = chatService.save(chatDTO);
        return ResponseEntity.created(new URI("/api/chats/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /chats : Updates an existing chat.
     *
     * @param chatDTO the chatDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated chatDTO,
     * or with status 400 (Bad Request) if the chatDTO is not valid,
     * or with status 500 (Internal Server Error) if the chatDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/chats")
    @Timed
    public ResponseEntity<ChatDTO> updateChat(@RequestBody ChatDTO chatDTO) throws URISyntaxException {
        log.debug("REST request to update Chat : {}", chatDTO);
        if (chatDTO.getId() == null) {
            return createChat(chatDTO);
        }
        ChatDTO result = chatService.save(chatDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, chatDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /chats : get all the chats.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of chats in body
     */
    @GetMapping("/chats")
    @Timed
    public ResponseEntity<List<ChatDTO>> getAllChats(Pageable pageable) {
        log.debug("REST request to get a page of Chats");
        Page<ChatDTO> page = chatService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/chats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /chats/:id : get the "id" chat.
     *
     * @param id the id of the chatDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the chatDTO, or with status 404 (Not Found)
     */
    @GetMapping("/chats/{id}")
    @Timed
    public ResponseEntity<ChatDTO> getChat(@PathVariable Long id) {
        log.debug("REST request to get Chat : {}", id);
        ChatDTO chatDTO = chatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(chatDTO));
    }

    /**
     * DELETE  /chats/:id : delete the "id" chat.
     *
     * @param id the id of the chatDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/chats/{id}")
    @Timed
    public ResponseEntity<Void> deleteChat(@PathVariable Long id) {
        log.debug("REST request to delete Chat : {}", id);
        chatService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/chats?query=:query : search for the chat corresponding
     * to the query.
     *
     * @param query    the query of the chat search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/chats")
    @Timed
    public ResponseEntity<List<ChatDTO>> searchChats(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Chats for query {}", query);
        Page<ChatDTO> page = chatService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/chats");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}

