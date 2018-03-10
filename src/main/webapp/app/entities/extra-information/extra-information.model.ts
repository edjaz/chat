import { BaseEntity } from 'app/core/model/base-entity';

export class ExtraInformation implements BaseEntity {
    constructor(public id?: number, public extras?: string) {}
}
