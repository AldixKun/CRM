import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-customer-list',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './customer-list.component.html',
  styleUrls: ['./customer-list.component.css']
})
export class CustomerListComponent implements OnInit {
  
  customers: any[] = [];
  invoices: any[] = [];
  selectedCustomerId: string | null = null;

  constructor(
    public cs: CustomerService,
    public auth: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadCustomers();
  }

  loadCustomers() {
    this.cs.getAll().subscribe((r: any[]) => {
      if (this.auth.isAdmin) {
        this.customers = r;
      } else {
        this.customers = r.filter((c: any) => c.correo === this.auth.correo);
      }
    });
  }

  viewInvoices(id: string) {
    this.selectedCustomerId = id;
    this.cs.getInvoices(id).subscribe((r: any[]) => {
      this.invoices = r;
    });
  }

  // Botón "Nuevo Cliente"
  create() {
    this.router.navigate(['/customer-form']);
  }

  // Botón "Editar"
  edit(id: string) {
    this.router.navigate(['/customer-form', id]);
  }

  // Botón "Eliminar"
  delete(id: string) {
    if(confirm('¿Estás seguro de eliminar este cliente?')) {
      this.cs.delete(id).subscribe(() => {
        alert('Cliente eliminado');
        this.loadCustomers(); 
      });
    }
  }
}