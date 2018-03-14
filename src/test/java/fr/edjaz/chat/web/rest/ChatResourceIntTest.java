package fr.edjaz.chat.web.rest;

import fr.edjaz.chat.ChatApp;

import fr.edjaz.chat.domain.Chat;
import fr.edjaz.chat.messaging.ChatMessageChannel;
import fr.edjaz.chat.messaging.OpenChatChannel;
import fr.edjaz.chat.repository.ChatRepository;
import fr.edjaz.chat.service.ChatService;
import fr.edjaz.chat.repository.search.ChatSearchRepository;
import fr.edjaz.chat.service.ClientService;
import fr.edjaz.chat.service.ConseillerService;
import fr.edjaz.chat.service.UserService;
import fr.edjaz.chat.service.dto.ChatDTO;
import fr.edjaz.chat.service.mapper.ChatMapper;
import fr.edjaz.chat.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static fr.edjaz.chat.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import fr.edjaz.chat.domain.enumeration.ChatStatus;
/**
 * Test class for the ChatResource REST controller.
 *
 * @see ChatResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ChatApp.class)
public class ChatResourceIntTest {

    private static final ChatStatus DEFAULT_STATUS = ChatStatus.WAITTING;
    private static final ChatStatus UPDATED_STATUS = ChatStatus.OPENED;

    private static final Instant DEFAULT_CREATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_OPENED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_OPENED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CLOSED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CLOSED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatMapper chatMapper;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatSearchRepository chatSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private PublishSubscribeChannel hasFreeChat;

    private MockMvc restChatMockMvc;

    private Chat chat;


    @Autowired
    private ChatMessageChannel chatMessageChannel;

    @Autowired
    private UserService userService;

    @Autowired
    private ConseillerService conseillerService;

    @Autowired
    private OpenChatChannel openChatChannel;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PublishSubscribeChannel broadCastHasFreeChat;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChatResource chatResource = new ChatResource(chatService, conseillerService, openChatChannel, clientService, broadCastHasFreeChat, chatMessageChannel, messageService, broadCastChatMessageChannel);
        this.restChatMockMvc = MockMvcBuilders.standaloneSetup(chatResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Chat createEntity(EntityManager em) {
        Chat chat = new Chat()
            .status(DEFAULT_STATUS)
            .created(DEFAULT_CREATED)
            .opened(DEFAULT_OPENED)
            .closed(DEFAULT_CLOSED);
        return chat;
    }

    @Before
    public void initTest() {
        chatSearchRepository.deleteAll();
        chat = createEntity(em);
    }

    @Test
    @Transactional
    public void createChat() throws Exception {
        int databaseSizeBeforeCreate = chatRepository.findAll().size();

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);
        restChatMockMvc.perform(post("/api/chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isCreated());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeCreate + 1);
        Chat testChat = chatList.get(chatList.size() - 1);
        assertThat(testChat.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testChat.getCreated()).isEqualTo(DEFAULT_CREATED);
        assertThat(testChat.getOpened()).isEqualTo(DEFAULT_OPENED);
        assertThat(testChat.getClosed()).isEqualTo(DEFAULT_CLOSED);

        // Validate the Chat in Elasticsearch
        Chat chatEs = chatSearchRepository.findById(testChat.getId()).get();
        assertThat(chatEs).isEqualToIgnoringGivenFields(testChat);
    }

    @Test
    @Transactional
    public void createChatWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = chatRepository.findAll().size();

        // Create the Chat with an existing ID
        chat.setId(1L);
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChatMockMvc.perform(post("/api/chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllChats() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        // Get all the chatList
        restChatMockMvc.perform(get("/api/chats?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chat.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].opened").value(hasItem(DEFAULT_OPENED.toString())))
            .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.toString())));
    }

    @Test
    @Transactional
    public void getChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);

        // Get the chat
        restChatMockMvc.perform(get("/api/chats/{id}", chat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(chat.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.created").value(DEFAULT_CREATED.toString()))
            .andExpect(jsonPath("$.opened").value(DEFAULT_OPENED.toString()))
            .andExpect(jsonPath("$.closed").value(DEFAULT_CLOSED.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingChat() throws Exception {
        // Get the chat
        restChatMockMvc.perform(get("/api/chats/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);
        chatSearchRepository.save(chat);
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();

        // Update the chat
        Chat updatedChat = chatRepository.findById(chat.getId()).get();
        // Disconnect from session so that the updates on updatedChat are not directly saved in db
        em.detach(updatedChat);
        updatedChat
            .status(UPDATED_STATUS)
            .created(UPDATED_CREATED)
            .opened(UPDATED_OPENED)
            .closed(UPDATED_CLOSED);
        ChatDTO chatDTO = chatMapper.toDto(updatedChat);

        restChatMockMvc.perform(put("/api/chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isOk());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate);
        Chat testChat = chatList.get(chatList.size() - 1);
        assertThat(testChat.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testChat.getCreated()).isEqualTo(UPDATED_CREATED);
        assertThat(testChat.getOpened()).isEqualTo(UPDATED_OPENED);
        assertThat(testChat.getClosed()).isEqualTo(UPDATED_CLOSED);

        // Validate the Chat in Elasticsearch
        Chat chatEs = chatSearchRepository.findById(testChat.getId()).get();
        assertThat(chatEs).isEqualToIgnoringGivenFields(testChat);
    }

    @Test
    @Transactional
    public void updateNonExistingChat() throws Exception {
        int databaseSizeBeforeUpdate = chatRepository.findAll().size();

        // Create the Chat
        ChatDTO chatDTO = chatMapper.toDto(chat);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restChatMockMvc.perform(put("/api/chats")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(chatDTO)))
            .andExpect(status().isCreated());

        // Validate the Chat in the database
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);
        chatSearchRepository.save(chat);
        int databaseSizeBeforeDelete = chatRepository.findAll().size();

        // Get the chat
        restChatMockMvc.perform(delete("/api/chats/{id}", chat.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean chatExistsInEs = chatSearchRepository.existsById(chat.getId());
        assertThat(chatExistsInEs).isFalse();

        // Validate the database is empty
        List<Chat> chatList = chatRepository.findAll();
        assertThat(chatList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchChat() throws Exception {
        // Initialize the database
        chatRepository.saveAndFlush(chat);
        chatSearchRepository.save(chat);

        // Search the chat
        restChatMockMvc.perform(get("/api/_search/chats?query=id:" + chat.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(chat.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].created").value(hasItem(DEFAULT_CREATED.toString())))
            .andExpect(jsonPath("$.[*].opened").value(hasItem(DEFAULT_OPENED.toString())))
            .andExpect(jsonPath("$.[*].closed").value(hasItem(DEFAULT_CLOSED.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Chat.class);
        Chat chat1 = new Chat();
        chat1.setId(1L);
        Chat chat2 = new Chat();
        chat2.setId(chat1.getId());
        assertThat(chat1).isEqualTo(chat2);
        chat2.setId(2L);
        assertThat(chat1).isNotEqualTo(chat2);
        chat1.setId(null);
        assertThat(chat1).isNotEqualTo(chat2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChatDTO.class);
        ChatDTO chatDTO1 = new ChatDTO();
        chatDTO1.setId(1L);
        ChatDTO chatDTO2 = new ChatDTO();
        assertThat(chatDTO1).isNotEqualTo(chatDTO2);
        chatDTO2.setId(chatDTO1.getId());
        assertThat(chatDTO1).isEqualTo(chatDTO2);
        chatDTO2.setId(2L);
        assertThat(chatDTO1).isNotEqualTo(chatDTO2);
        chatDTO1.setId(null);
        assertThat(chatDTO1).isNotEqualTo(chatDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(chatMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(chatMapper.fromId(null)).isNull();
    }
}
