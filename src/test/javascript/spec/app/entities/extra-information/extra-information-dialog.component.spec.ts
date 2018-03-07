/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { ChatTestModule } from '../../../test.module';
import { ExtraInformationDialogComponent } from '../../../../../../main/webapp/app/entities/extra-information/extra-information-dialog.component';
import { ExtraInformationService } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.service';
import { ExtraInformation } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.model';

describe('Component Tests', () => {

    describe('ExtraInformation Management Dialog Component', () => {
        let comp: ExtraInformationDialogComponent;
        let fixture: ComponentFixture<ExtraInformationDialogComponent>;
        let service: ExtraInformationService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ExtraInformationDialogComponent],
                providers: [
                    ExtraInformationService
                ]
            })
            .overrideTemplate(ExtraInformationDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExtraInformationDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExtraInformationService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ExtraInformation(123);
                        spyOn(service, 'update').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.extraInformation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.update).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'extraInformationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );

            it('Should call create service on save for new entity',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        const entity = new ExtraInformation();
                        spyOn(service, 'create').and.returnValue(Observable.of(new HttpResponse({body: entity})));
                        comp.extraInformation = entity;
                        // WHEN
                        comp.save();
                        tick(); // simulate async

                        // THEN
                        expect(service.create).toHaveBeenCalledWith(entity);
                        expect(comp.isSaving).toEqual(false);
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalledWith({ name: 'extraInformationListModification', content: 'OK'});
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
