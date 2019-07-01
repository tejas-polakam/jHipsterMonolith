export interface IStocks {
  id?: number;
  name?: string;
  open?: number;
  high?: number;
  close?: number;
  low?: number;
  volume?: number;
}

export class Stocks implements IStocks {
  constructor(
    public id?: number,
    public name?: string,
    public open?: number,
    public high?: number,
    public close?: number,
    public low?: number,
    public volume?: number
  ) {}
}
