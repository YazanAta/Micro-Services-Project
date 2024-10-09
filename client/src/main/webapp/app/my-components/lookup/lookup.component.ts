import { CommonModule } from '@angular/common'
import { HttpResponse } from '@angular/common/http'
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core'
import { Observable } from 'rxjs'

@Component({
  selector: 'jhi-lookup',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './lookup.component.html',
  styleUrl: './lookup.component.scss'
})
export class LookupComponent implements OnInit, OnChanges {
  @Input() service: any;
  @Input() labelKey: keyof any;
  @Input() flag: any;
  @Input() id: any;
  @Output() selected = new EventEmitter<any>();

  entities$: Observable<HttpResponse<any[]>> | undefined
  entities: any[] = []
  
  ngOnInit(): void {
    if (this.service) {
      this.loadEntities()
    }
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.loadEntities();
  }

  loadEntities(): void {
    switch (this.flag) {
      case 'governorate': {
        this.entities$ = this.service.query();
        this.entities$?.subscribe(response => {
          this.entities = response.body ?? [];
        });
        break; // Add break here
      }
      case 'brigade': { // Add colon here
        this.entities$ = this.service.findByGovernorateId(this.id);
        this.entities$?.subscribe({
          next: (res) => {
            this.entities = res.body ?? [];
          }
        });
        break; // Add break here
      }
      case 'municipality': {
        console.log("MMM")
        this.entities$ = this.service.findByBrigadeId(this.id);
        this.entities$?.subscribe({
          next: (res) => {
            this.entities = res.body ?? [];
          }
        });
        break; // Add break here
      }
      default: {
        // Optional: handle unexpected values of this.flag
        console.warn(`Unhandled flag value: ${this.flag}`);
      }
    }
    
  }

  onSelect(event: Event): void {
    const target = event.target as HTMLSelectElement; // Cast to HTMLSelectElement
    const selectedValue = target.value; // Now TypeScript knows `target` has a `value` property
    
    const selectedEntity = this.entities.find(entity => entity.name === selectedValue);
    if (selectedEntity) {
      this.selected.emit(selectedEntity); // Emit the selected entity
    }
  }
  
}
