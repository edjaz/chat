/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { Observable } from 'rxjs/Observable';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ChatTestModule } from '../../../test.module';
import { ChatComponent } from '../../../../../../main/webapp/app/entities/chat/chat.component';
import { ChatService } from '../../../../../../main/webapp/app/entities/chat/chat.service';
import { Chat } from '../../../../../../main/webapp/app/entities/chat/chat.model';

describe('Component Tests', () => {

    describe('Chat Management Component', () => {
        let comp: ChatComponent;
        let fixture: ComponentFixture<ChatComponent>;
        let service: ChatService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ChatTestModule],
                declarations: [ChatComponent],
                providers: [
                    ChatService
                ]
            })
            .overrideTemplate(ChatComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(ChatComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ChatService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN
                const headers = new HttpHeaders().append('link', 'link;link');
                spyOn(service, 'query').and.returnValue(Observable.of(new HttpResponse({
                    body: [new Chat(123)],
                    headers
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.query).toHaveBeenCalled();
                expect(comp.chats[0]).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
