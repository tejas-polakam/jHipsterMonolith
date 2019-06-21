import { Moment } from 'moment';

export interface IBook {
  id?: number;
  title?: string;
  description?: string;
  publicationDate?: Moment;
  price?: number;
  manytooneName?: string;
  manytooneId?: number;
}

export class Book implements IBook {
  constructor(
    public id?: number,
    public title?: string,
    public description?: string,
    public publicationDate?: Moment,
    public price?: number,
    public manytooneName?: string,
    public manytooneId?: number
  ) {}
}
