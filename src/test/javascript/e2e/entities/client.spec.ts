import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Client e2e test', () => {

    let navBarPage: NavBarPage;
    let clientDialogPage: ClientDialogPage;
    let clientComponentsPage: ClientComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Clients', () => {
        navBarPage.goToEntity('client');
        clientComponentsPage = new ClientComponentsPage();
        expect(clientComponentsPage.getTitle())
            .toMatch(/chatApp.client.home.title/);

    });

    it('should load create Client dialog', () => {
        clientComponentsPage.clickOnCreateButton();
        clientDialogPage = new ClientDialogPage();
        expect(clientDialogPage.getModalTitle())
            .toMatch(/chatApp.client.home.createOrEditLabel/);
        clientDialogPage.close();
    });

    it('should create and save Clients', () => {
        clientComponentsPage.clickOnCreateButton();
        clientDialogPage.setFirstnameInput('firstname');
        expect(clientDialogPage.getFirstnameInput()).toMatch('firstname');
        clientDialogPage.setLastnameInput('lastname');
        expect(clientDialogPage.getLastnameInput()).toMatch('lastname');
        clientDialogPage.setEmailInput('email');
        expect(clientDialogPage.getEmailInput()).toMatch('email');
        clientDialogPage.extraSelectLastOption();
        clientDialogPage.save();
        expect(clientDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ClientComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-client div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ClientDialogPage {
    modalTitle = element(by.css('h4#myClientLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    firstnameInput = element(by.css('input#field_firstname'));
    lastnameInput = element(by.css('input#field_lastname'));
    emailInput = element(by.css('input#field_email'));
    extraSelect = element(by.css('select#field_extra'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setFirstnameInput = function(firstname) {
        this.firstnameInput.sendKeys(firstname);
    };

    getFirstnameInput = function() {
        return this.firstnameInput.getAttribute('value');
    };

    setLastnameInput = function(lastname) {
        this.lastnameInput.sendKeys(lastname);
    };

    getLastnameInput = function() {
        return this.lastnameInput.getAttribute('value');
    };

    setEmailInput = function(email) {
        this.emailInput.sendKeys(email);
    };

    getEmailInput = function() {
        return this.emailInput.getAttribute('value');
    };

    extraSelectLastOption = function() {
        this.extraSelect.all(by.tagName('option')).last().click();
    };

    extraSelectOption = function(option) {
        this.extraSelect.sendKeys(option);
    };

    getExtraSelect = function() {
        return this.extraSelect;
    };

    getExtraSelectedOption = function() {
        return this.extraSelect.element(by.css('option:checked')).getText();
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
