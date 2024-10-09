import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { LookupComponent } from "../my-components/lookup/lookup.component";
import { MunicipalityService } from 'app/entities/municipality/service/municipality.service';
import { GovernorateService } from 'app/entities/governorate/service/governorate.service';
import { BrigadeService } from 'app/entities/brigade/service/brigade.service';

@Component({
  standalone: true,
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss',
  imports: [SharedModule, RouterModule, LookupComponent],
})
export default class HomeComponent implements OnInit, OnDestroy {
  public municipalityService = inject(MunicipalityService);
  public governorateService = inject(GovernorateService);
  public brigadeService = inject(BrigadeService);

  selectedGovernorate = signal<any>(null);
  selectedBrigade = signal<any>(null);
  selectedMunicipality = signal<any>(null);

  account = signal<Account | null>(null);

  private readonly destroy$ = new Subject<void>();

  private accountService = inject(AccountService);
  private router = inject(Router);

  ngOnInit(): void {
    this.accountService
      .getAuthenticationState()
      .pipe(takeUntil(this.destroy$))
      .subscribe(account => this.account.set(account));
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  onGovernorateSelected(governorate: any) : any {
    this.selectedGovernorate.set(governorate);
    this.selectedBrigade.set(null); // Reset brigade selection when a new governorate is selected
  }

  onBrigadeSelected(brigade: any) : any{
      this.selectedBrigade.set(brigade);
      this.selectedMunicipality.set(null); // Reset brigade selection when a new governorate is selected
  }

  onMunicipalitySelected(municipality: any) : any{
    this.selectedMunicipality.set(municipality);
  }
}
