/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { ChatTestModule } from '../../../test.module';
import { ChatDetailComponent } from '../../../../../../main/webapp/app/entities/chat/chat-detail.component';
import { ChatService } from '../../../../../../main/webapp/app/entities/chat/chat.service';
import { Chat } from '../../../../../../main/webapp/app/entities/chat/chat.model';

describe('Component Tests', () => {

    describe('Chat Management Detail Component', () => {
        let comp: ChatDetailComponent;
        let fixture: ComponentFixture<ChatDetailComponent>;
        let service: ChatService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ChatDetailComponent],
                providers: [
                    ChatService
                ]
            })
            .overrideTemplate(ChatDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChatDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChatService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new Chat(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.chat).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
