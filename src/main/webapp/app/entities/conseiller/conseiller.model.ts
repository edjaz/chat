import { BaseEntity } from 'app/core/model/base-entity';

export class Conseiller implements BaseEntity {
    constructor(public id?: number, public userId?: number) {}
}
