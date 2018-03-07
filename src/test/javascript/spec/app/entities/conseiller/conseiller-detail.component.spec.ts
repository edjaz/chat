/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ChatTestModule } from '../../../test.module';
import { ConseillerDetailComponent } from '../../../../../../main/webapp/app/entities/conseiller/conseiller-detail.component';
import { ConseillerService } from '../../../../../../main/webapp/app/entities/conseiller/conseiller.service';
import { Conseiller } from '../../../../../../main/webapp/app/entities/conseiller/conseiller.model';

describe('Component Tests', () => {

    describe('Conseiller Management Detail Component', () => {
        let comp: ConseillerDetailComponent;
        let fixture: ComponentFixture<ConseillerDetailComponent>;
        let service: ConseillerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ConseillerDetailComponent],
                providers: [
                    ConseillerService
                ]
            })
            .overrideTemplate(ConseillerDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConseillerDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConseillerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Conseiller(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.conseiller).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
