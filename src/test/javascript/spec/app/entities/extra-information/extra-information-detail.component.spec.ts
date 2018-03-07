/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ChatTestModule } from '../../../test.module';
import { ExtraInformationDetailComponent } from '../../../../../../main/webapp/app/entities/extra-information/extra-information-detail.component';
import { ExtraInformationService } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.service';
import { ExtraInformation } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.model';

describe('Component Tests', () => {

    describe('ExtraInformation Management Detail Component', () => {
        let comp: ExtraInformationDetailComponent;
        let fixture: ComponentFixture<ExtraInformationDetailComponent>;
        let service: ExtraInformationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ExtraInformationDetailComponent],
                providers: [
                    ExtraInformationService
                ]
            })
            .overrideTemplate(ExtraInformationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExtraInformationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExtraInformationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new ExtraInformation(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.extraInformation).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
