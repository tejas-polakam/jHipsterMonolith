import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';
import { IBook, Book } from 'app/shared/model/book.model';
import { BookService } from './book.service';
import { IAuthor } from 'app/shared/model/author.model';
import { AuthorService } from 'app/entities/author';

@Component({
  selector: 'jhi-book-update',
  templateUrl: './book-update.component.html'
})
export class BookUpdateComponent implements OnInit {
  isSaving: boolean;

  authors: IAuthor[];
  publicationDateDp: any;

  editForm = this.fb.group({
    id: [],
    title: [],
    description: [],
    publicationDate: [],
    price: [],
    manytooneId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected bookService: BookService,
    protected authorService: AuthorService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ book }) => {
      this.updateForm(book);
    });
    this.authorService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IAuthor[]>) => mayBeOk.ok),
        map((response: HttpResponse<IAuthor[]>) => response.body)
      )
      .subscribe((res: IAuthor[]) => (this.authors = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(book: IBook) {
    this.editForm.patchValue({
      id: book.id,
      title: book.title,
      description: book.description,
      publicationDate: book.publicationDate,
      price: book.price,
      manytooneId: book.manytooneId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const book = this.createFromForm();
    if (book.id !== undefined) {
      this.subscribeToSaveResponse(this.bookService.update(book));
    } else {
      this.subscribeToSaveResponse(this.bookService.create(book));
    }
  }

  private createFromForm(): IBook {
    const entity = {
      ...new Book(),
      id: this.editForm.get(['id']).value,
      title: this.editForm.get(['title']).value,
      description: this.editForm.get(['description']).value,
      publicationDate: this.editForm.get(['publicationDate']).value,
      price: this.editForm.get(['price']).value,
      manytooneId: this.editForm.get(['manytooneId']).value
    };
    return entity;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBook>>) {
    result.subscribe((res: HttpResponse<IBook>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackAuthorById(index: number, item: IAuthor) {
    return item.id;
  }
}
