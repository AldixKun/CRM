import { Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';

export const routes: Routes = [
  { path: 'login', component: LoginComponent }, // El Login se carga rápido al principio
  
  // --- LAZY LOADING ---
  
  // Panel Principal
  { 
    path: 'product-admin', 
    loadComponent: () => import('./components/product-admin/product-admin.component').then(m => m.ProductAdminComponent) 
  },
  
  // Lista de Clientes
  { 
    path: 'customers', 
    loadComponent: () => import('./components/customer-list/customer-list.component').then(m => m.CustomerListComponent) 
  },
  
  // Formulario (Crear)
  { 
    path: 'customer-form', 
    loadComponent: () => import('./components/customer-form/customer-form.component').then(m => m.CustomerFormComponent) 
  },
  
  // Formulario (Editar)
  { 
    path: 'customer-form/:id', 
    loadComponent: () => import('./components/customer-form/customer-form.component').then(m => m.CustomerFormComponent) 
  },
  
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: '**', redirectTo: 'login' }
];