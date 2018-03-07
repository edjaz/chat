import { browser, element, by } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';

describe('ExtraInformation e2e test', () => {

    let navBarPage: NavBarPage;
    let extraInformationDialogPage: ExtraInformationDialogPage;
    let extraInformationComponentsPage: ExtraInformationComponentsPage;

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load ExtraInformations', () => {
        navBarPage.goToEntity('extra-information');
        extraInformationComponentsPage = new ExtraInformationComponentsPage();
        expect(extraInformationComponentsPage.getTitle())
            .toMatch(/chatApp.extraInformation.home.title/);

    });

    it('should load create ExtraInformation dialog', () => {
        extraInformationComponentsPage.clickOnCreateButton();
        extraInformationDialogPage = new ExtraInformationDialogPage();
        expect(extraInformationDialogPage.getModalTitle())
            .toMatch(/chatApp.extraInformation.home.createOrEditLabel/);
        extraInformationDialogPage.close();
    });

    it('should create and save ExtraInformations', () => {
        extraInformationComponentsPage.clickOnCreateButton();
        extraInformationDialogPage.setExtrasInput('extras');
        expect(extraInformationDialogPage.getExtrasInput()).toMatch('extras');
        extraInformationDialogPage.save();
        expect(extraInformationDialogPage.getSaveButton().isPresent()).toBeFalsy();
    });

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class ExtraInformationComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-extra-information div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class ExtraInformationDialogPage {
    modalTitle = element(by.css('h4#myExtraInformationLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    extrasInput = element(by.css('input#field_extras'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setExtrasInput = function(extras) {
        this.extrasInput.sendKeys(extras);
    };

    getExtrasInput = function() {
        return this.extrasInput.getAttribute('value');
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
