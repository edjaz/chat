import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('Conseiller e2e test', () => {

    let navBarPage: NavBarPage;
    let conseillerDialogPage: ConseillerDialogPage;
    let conseillerComponentsPage: ConseillerComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Conseillers', () => {
        navBarPage.goToEntity('conseiller');
        conseillerComponentsPage = new ConseillerComponentsPage();
        expect(conseillerComponentsPage.getTitle())
            .toMatch(/chatApp.conseiller.home.title/);

    });

    it('should load create Conseiller dialog', () => {
        conseillerComponentsPage.clickOnCreateButton();
        conseillerDialogPage = new ConseillerDialogPage();
        expect(conseillerDialogPage.getModalTitle())
            .toMatch(/chatApp.conseiller.home.createOrEditLabel/);
        conseillerDialogPage.close();
    });

    it('should create and save Conseillers', () => {
        conseillerComponentsPage.clickOnCreateButton();
        conseillerDialogPage.userSelectLastOption();
        conseillerDialogPage.save();
        expect(conseillerDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ConseillerComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-conseiller div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ConseillerDialogPage {
    modalTitle = element(by.css('h4#myConseillerLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    userSelectLastOption = function() {
        this.userSelect.all(by.tagName('option')).last().click();
    };

    userSelectOption = function(option) {
        this.userSelect.sendKeys(option);
    };

    getUserSelect = function() {
        return this.userSelect;
    };

    getUserSelectedOption = function() {
        return this.userSelect.element(by.css('option:checked')).getText();
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
