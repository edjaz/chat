/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ChatTestModule } from '../../../test.module';
import { ExtraInformationComponent } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.component';
import { ExtraInformationService } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.service';
import { ExtraInformation } from '../../../../../../main/webapp/app/entities/extra-information/extra-information.model';

describe('Component Tests', () => {

    describe('ExtraInformation Management Component', () => {
        let comp: ExtraInformationComponent;
        let fixture: ComponentFixture<ExtraInformationComponent>;
        let service: ExtraInformationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ExtraInformationComponent],
                providers: [
                    ExtraInformationService
                ]
            })
            .overrideTemplate(ExtraInformationComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ExtraInformationComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExtraInformationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new ExtraInformation(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.extraInformations[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
