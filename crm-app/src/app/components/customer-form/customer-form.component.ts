import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './customer-form.component.html'
})
export class CustomerFormComponent implements OnInit {

  id: string | null = null;

  customer = {
    nombre: "",
    correo: "",
    password: "",
    antiguedad: 0,
    deudas: 0,
    isAdmin: false
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private cs: CustomerService
  ) {}

  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    if (this.id) {
      this.cs.getById(this.id).subscribe((c: any) => this.customer = c);
    }
  }

  save() {
    if (this.id) {
      this.cs.update(this.id, this.customer).subscribe(() => {
        this.router.navigate(['/crm/customers']);
      });
    } else {
      this.cs.create(this.customer).subscribe(() => {
        this.router.navigate(['/crm/customers']);
      });
    }
  }

  cancel() {
    this.router.navigate(['/crm/customers']);
  }
}
