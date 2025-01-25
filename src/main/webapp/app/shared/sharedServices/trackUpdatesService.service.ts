import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TrackUpdatesService {
  trackUpdateSaldo$!: Observable<boolean>;
  private updateSaldo = new BehaviorSubject<boolean>(false);

  constructor() {
    this.trackUpdateSaldo$ = this.updateSaldo.asObservable();
  }

  emitSaldoUpdate(): void {
    this.updateSaldo.next(true);
  }
}
