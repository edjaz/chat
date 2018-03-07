package fr.edjaz.chat.cucumber.stepdefs;

import fr.edjaz.chat.ChatApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ChatApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
