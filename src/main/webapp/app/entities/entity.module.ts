import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { ChatChatModule } from './chat/chat.module';
import { ChatClientModule } from './client/client.module';
import { ChatExtraInformationModule } from './extra-information/extra-information.module';
import { ChatConseillerModule } from './conseiller/conseiller.module';
import { ChatMessageModule } from './message/message.module';
import { ChatCoreModule } from 'app/core';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        ChatCoreModule,
        ChatChatModule,
        ChatClientModule,
        ChatExtraInformationModule,
        ChatConseillerModule,
        ChatMessageModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChatEntityModule {}
