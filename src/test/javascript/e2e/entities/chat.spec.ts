import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Chat e2e test', () => {

    let navBarPage: NavBarPage;
    let chatDialogPage: ChatDialogPage;
    let chatComponentsPage: ChatComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Chats', () => {
        navBarPage.goToEntity('chat');
        chatComponentsPage = new ChatComponentsPage();
        expect(chatComponentsPage.getTitle())
            .toMatch(/chatApp.chat.home.title/);

    });

    it('should load create Chat dialog', () => {
        chatComponentsPage.clickOnCreateButton();
        chatDialogPage = new ChatDialogPage();
        expect(chatDialogPage.getModalTitle())
            .toMatch(/chatApp.chat.home.createOrEditLabel/);
        chatDialogPage.close();
    });

    it('should create and save Chats', () => {
        chatComponentsPage.clickOnCreateButton();
        chatDialogPage.statusSelectLastOption();
        chatDialogPage.setCreatedInput(12310020012301);
        expect(chatDialogPage.getCreatedInput()).toMatch('2001-12-31T02:30');
        chatDialogPage.setOpenedInput(12310020012301);
        expect(chatDialogPage.getOpenedInput()).toMatch('2001-12-31T02:30');
        chatDialogPage.setClosedInput(12310020012301);
        expect(chatDialogPage.getClosedInput()).toMatch('2001-12-31T02:30');
        chatDialogPage.clientSelectLastOption();
        // chatDialogPage.conseillerSelectLastOption();
        chatDialogPage.save();
        expect(chatDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ChatComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-chat div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ChatDialogPage {
    modalTitle = element(by.css('h4#myChatLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    statusSelect = element(by.css('select#field_status'));
    createdInput = element(by.css('input#field_created'));
    openedInput = element(by.css('input#field_opened'));
    closedInput = element(by.css('input#field_closed'));
    clientSelect = element(by.css('select#field_client'));
    conseillerSelect = element(by.css('select#field_conseiller'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

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

    setOpenedInput = function(opened) {
        this.openedInput.sendKeys(opened);
    };

    getOpenedInput = function() {
        return this.openedInput.getAttribute('value');
    };

    setClosedInput = function(closed) {
        this.closedInput.sendKeys(closed);
    };

    getClosedInput = function() {
        return this.closedInput.getAttribute('value');
    };

    clientSelectLastOption = function() {
        this.clientSelect.all(by.tagName('option')).last().click();
    };

    clientSelectOption = function(option) {
        this.clientSelect.sendKeys(option);
    };

    getClientSelect = function() {
        return this.clientSelect;
    };

    getClientSelectedOption = function() {
        return this.clientSelect.element(by.css('option:checked')).getText();
    };

    conseillerSelectLastOption = function() {
        this.conseillerSelect.all(by.tagName('option')).last().click();
    };

    conseillerSelectOption = function(option) {
        this.conseillerSelect.sendKeys(option);
    };

    getConseillerSelect = function() {
        return this.conseillerSelect;
    };

    getConseillerSelectedOption = function() {
        return this.conseillerSelect.element(by.css('option:checked')).getText();
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
