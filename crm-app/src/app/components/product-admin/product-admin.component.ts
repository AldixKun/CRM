import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { CustomerService } from '../../services/customer.service';

@Component({
  selector: 'app-product-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product-admin.component.html',
  styleUrls: ['./product-admin.component.css']
})
export class ProductAdminComponent implements OnInit {

  isAdmin = false;
  customers: any[] = [];
  currentUser: any = null;
  selectedCustomer: any = null;
  errorMessage = '';

  constructor(
    public auth: AuthService,
    private customerService: CustomerService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.auth.isAdmin;
    this.loadData();
  }

  loadData() {
    if (this.isAdmin) {
      // MODO ADMIN: Carga lista completa
      this.customerService.getAll().subscribe((data: any) => {
        this.customers = data;
      });
    } else {
      // MODO USUARIO: Usamos el EMAIL
      const myEmail = localStorage.getItem('crm_email');
      console.log("Buscando perfil para:", myEmail);

      if (myEmail) {
        this.customerService.getMyInfoByEmail(myEmail).subscribe({
          next: (data: any) => {
            this.currentUser = data;
            console.log("¡Usuario encontrado!", data);
          },
          error: (err: any) => {
            console.error(err);
            this.errorMessage = "No se pudo cargar tu perfil. Intenta entrar de nuevo.";
          }
        });
      } else {
        this.logout();
      }
    }
  }

  // --- MÉTODOS VISUALES ---

  viewDetails(customer: any) { 
    this.selectedCustomer = customer; 
  }
  
  clearSelection() { 
    this.selectedCustomer = null; 
  }

  // --- MÉTODOS DE ACCIÓN ---

  createCustomer() {
    this.router.navigate(['/customer-form']);
  }

  editCustomer(id: string) {
    this.router.navigate(['/customer-form', id]);
  }

  // --- ELIMINAR CLIENTE ---
  deleteCustomer(id: string) {
    if (confirm('¿Estás SEGURO de que quieres eliminar a este cliente? Esta acción no se puede deshacer.')) {
      this.customerService.delete(id).subscribe({
        next: () => {
          alert('Cliente eliminado correctamente.');
          this.loadData();
        },
        error: (err: any) => {
          console.error(err);
          alert('Error al eliminar. Revisa la consola.');
        }
      });
    }
  }

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}