import { Injectable, PipeTransform } from '@angular/core';

import { BehaviorSubject, Observable, of, Subject } from 'rxjs';
import { GERMPLASM } from './germplasm';
import { DecimalPipe } from '@angular/common';
import { debounceTime, delay, switchMap, tap } from 'rxjs/operators';
import { SortDirection } from './sortable.directive';
import { BrapiGermplasm } from '../models/brapi.model';

interface SearchResult {
    germplasm: BrapiGermplasm[];
    total: number;
}

interface State {
    page: number;
    pageSize: number;
    searchTerm: string;
    sortColumn: string;
    sortDirection: SortDirection;
}

function compare(v1, v2) {
    return v1 < v2 ? -1 : v1 > v2 ? 1 : 0;
}

function sort(germplasm: BrapiGermplasm[], column: string, direction: string): BrapiGermplasm[] {

    if (direction === '') {
        return germplasm;
    } else {
        return [...germplasm].sort((a, b) => {
            const res = compare(a[column], b[column]);
            return direction === 'asc' ? res : -res;
        });
    }
}

function matches(germplasm: BrapiGermplasm, term: string, pipe: PipeTransform) {
    return germplasm.germplasmDbId.toLowerCase().includes(term.toLowerCase())
        || germplasm.accessionNumber.toLowerCase().includes(term.toLowerCase())
        || germplasm.instituteName.toLowerCase().includes(term.toLowerCase())
        || germplasm.commonCropName.toLowerCase().includes(term.toLowerCase());
}

@Injectable({providedIn: 'root'})
export class GermplasmService {
    private _loading$ = new BehaviorSubject<boolean>(true);
    private _search$ = new Subject<void>();
    private _data$ = new BehaviorSubject<any[]>([]);
    private _total$ = new BehaviorSubject<number>(0);

    private _state: State = {
        page: 1,
        pageSize: 5,
        searchTerm: '',
        sortColumn: '',
        sortDirection: ''
    };

    constructor(private pipe: DecimalPipe) {
        this._search$.pipe(
            tap(() => this._loading$.next(true)),
            debounceTime(200),
            switchMap(() => this._search()),
            delay(200),
            tap(() => this._loading$.next(false))
        ).subscribe(result => {
            this._data$.next(result['germplasm']);
            this._total$.next(result.total);
        });

        this._search$.next();
    }

    get data$() { return this._data$.asObservable(); }
    get total$() { return this._total$.asObservable(); }
    get loading$() { return this._loading$.asObservable(); }
    get page() { return this._state.page; }
    get pageSize() { return this._state.pageSize; }
    get searchTerm() { return this._state.searchTerm; }

    set page(page: number) { this._set({page}); }
    set pageSize(pageSize: number) { this._set({pageSize}); }
    set searchTerm(searchTerm: string) { this._set({searchTerm}); }
    set sortColumn(sortColumn: string) { this._set({sortColumn}); }
    set sortDirection(sortDirection: SortDirection) { this._set({sortDirection}); }

    private _set(patch: Partial<State>) {
        Object.assign(this._state, patch);
        this._search$.next();
    }

    private _search(): Observable<any> {
        const {sortColumn, sortDirection, pageSize, page, searchTerm} = this._state;

        // 1. sort
        let data = sort(GERMPLASM, sortColumn, sortDirection);

        // 2. filter
        data = data.filter(data => matches(data, searchTerm, this.pipe));
        const total = data.length;

        // 3. paginate
        data = data.slice((page - 1) * pageSize, (page - 1) * pageSize + pageSize);
        return of({germplasm: data, total});
    }
}

