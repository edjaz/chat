import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Message e2e test', () => {

    let navBarPage: NavBarPage;
    let messageDialogPage: MessageDialogPage;
    let messageComponentsPage: MessageComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Messages', () => {
        navBarPage.goToEntity('message');
        messageComponentsPage = new MessageComponentsPage();
        expect(messageComponentsPage.getTitle())
            .toMatch(/chatApp.message.home.title/);

    });

    it('should load create Message dialog', () => {
        messageComponentsPage.clickOnCreateButton();
        messageDialogPage = new MessageDialogPage();
        expect(messageDialogPage.getModalTitle())
            .toMatch(/chatApp.message.home.createOrEditLabel/);
        messageDialogPage.close();
    });

    it('should create and save Messages', () => {
        messageComponentsPage.clickOnCreateButton();
        messageDialogPage.setTextInput('text');
        expect(messageDialogPage.getTextInput()).toMatch('text');
        messageDialogPage.statusSelectLastOption();
        messageDialogPage.setCreatedInput(12310020012301);
        expect(messageDialogPage.getCreatedInput()).toMatch('2001-12-31T02:30');
        messageDialogPage.setUpdatedInput(12310020012301);
        expect(messageDialogPage.getUpdatedInput()).toMatch('2001-12-31T02:30');
        messageDialogPage.setSentInput(12310020012301);
        expect(messageDialogPage.getSentInput()).toMatch('2001-12-31T02:30');
        messageDialogPage.chatSelectLastOption();
        messageDialogPage.writeByClientSelectLastOption();
        messageDialogPage.writeByConseillerSelectLastOption();
        messageDialogPage.save();
        expect(messageDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class MessageComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-message div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class MessageDialogPage {
    modalTitle = element(by.css('h4#myMessageLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    textInput = element(by.css('input#field_text'));
    statusSelect = element(by.css('select#field_status'));
    createdInput = element(by.css('input#field_created'));
    updatedInput = element(by.css('input#field_updated'));
    sentInput = element(by.css('input#field_sent'));
    chatSelect = element(by.css('select#field_chat'));
    writeByClientSelect = element(by.css('select#field_writeByClient'));
    writeByConseillerSelect = element(by.css('select#field_writeByConseiller'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setTextInput = function(text) {
        this.textInput.sendKeys(text);
    };

    getTextInput = function() {
        return this.textInput.getAttribute('value');
    };

    setStatusSelect = function(status) {
        this.statusSelect.sendKeys(status);
    };

    getStatusSelect = function() {
        return this.statusSelect.element(by.css('option:checked')).getText();
    };

    statusSelectLastOption = function() {
        this.statusSelect.all(by.tagName('option')).last().click();
    };
    setCreatedInput = function(created) {
        this.createdInput.sendKeys(created);
    };

    getCreatedInput = function() {
        return this.createdInput.getAttribute('value');
    };

    setUpdatedInput = function(updated) {
        this.updatedInput.sendKeys(updated);
    };

    getUpdatedInput = function() {
        return this.updatedInput.getAttribute('value');
    };

    setSentInput = function(sent) {
        this.sentInput.sendKeys(sent);
    };

    getSentInput = function() {
        return this.sentInput.getAttribute('value');
    };

    chatSelectLastOption = function() {
        this.chatSelect.all(by.tagName('option')).last().click();
    };

    chatSelectOption = function(option) {
        this.chatSelect.sendKeys(option);
    };

    getChatSelect = function() {
        return this.chatSelect;
    };

    getChatSelectedOption = function() {
        return this.chatSelect.element(by.css('option:checked')).getText();
    };

    writeByClientSelectLastOption = function() {
        this.writeByClientSelect.all(by.tagName('option')).last().click();
    };

    writeByClientSelectOption = function(option) {
        this.writeByClientSelect.sendKeys(option);
    };

    getWriteByClientSelect = function() {
        return this.writeByClientSelect;
    };

    getWriteByClientSelectedOption = function() {
        return this.writeByClientSelect.element(by.css('option:checked')).getText();
    };

    writeByConseillerSelectLastOption = function() {
        this.writeByConseillerSelect.all(by.tagName('option')).last().click();
    };

    writeByConseillerSelectOption = function(option) {
        this.writeByConseillerSelect.sendKeys(option);
    };

    getWriteByConseillerSelect = function() {
        return this.writeByConseillerSelect;
    };

    getWriteByConseillerSelectedOption = function() {
        return this.writeByConseillerSelect.element(by.css('option:checked')).getText();
    };

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
