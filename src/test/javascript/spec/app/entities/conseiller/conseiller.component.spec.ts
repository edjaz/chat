/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ChatTestModule } from '../../../test.module';
import { ConseillerComponent } from '../../../../../../main/webapp/app/entities/conseiller/conseiller.component';
import { ConseillerService } from '../../../../../../main/webapp/app/entities/conseiller/conseiller.service';
import { Conseiller } from '../../../../../../main/webapp/app/entities/conseiller/conseiller.model';

describe('Component Tests', () => {

    describe('Conseiller Management Component', () => {
        let comp: ConseillerComponent;
        let fixture: ComponentFixture<ConseillerComponent>;
        let service: ConseillerService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ConseillerComponent],
                providers: [
                    ConseillerService
                ]
            })
            .overrideTemplate(ConseillerComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ConseillerComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ConseillerService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Conseiller(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.conseillers[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
