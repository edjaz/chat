import { BaseEntity } from 'app/core/model/base-entity';

export class Client implements BaseEntity {
    constructor(public id?: number, public firstname?: string, public lastname?: string, public email?: string, public extraId?: number) {}
}
