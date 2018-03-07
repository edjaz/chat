/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ChatTestModule } from '../../../test.module';
import { ExtraInformationDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/extra-information/extra-information-delete-dialog.component';
import { ExtraInformationService } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.service';

describe('Component Tests', () => {

    describe('ExtraInformation Management Delete Component', () => {
        let comp: ExtraInformationDeleteDialogComponent;
        let fixture: ComponentFixture<ExtraInformationDeleteDialogComponent>;
        let service: ExtraInformationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ExtraInformationDeleteDialogComponent],
                providers: [
                    ExtraInformationService
                ]
            })
            .overrideTemplate(ExtraInformationDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExtraInformationDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExtraInformationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
