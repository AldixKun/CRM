import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../services/customer.service';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './customer-form.component.html',
  styleUrls: ['./customer-form.component.css']
})
export class CustomerFormComponent implements OnInit {

  customerForm: FormGroup;
  isEditMode = false;
  customerId: string | null = null;

  constructor(
    private fb: FormBuilder,
    private customerService: CustomerService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.customerForm = this.fb.group({
      nombre: ['', [Validators.required]],
      correo: ['', [Validators.required, Validators.email]],
      antiguedad: [0, [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.customerId = this.route.snapshot.paramMap.get('id');

    if (this.customerId) {
      this.isEditMode = true;
      this.loadCustomerData(this.customerId);
    }
  }

  loadCustomerData(id: string) {
    console.log("Cargando datos para editar ID:", id);
    
    this.customerService.getById(id).subscribe({
      next: (customer: any) => {
        console.log("Datos recibidos del servidor:", customer);
        
        // RELLENAR LOS CAMPOS SOLOS
        this.customerForm.patchValue({
          nombre: customer.nombre,
          correo: customer.correo,
          antiguedad: customer.antiguedad
        });
      },
      error: (err: any) => console.error('Error cargando cliente:', err)
    });
  }

  onSubmit() {
    if (this.customerForm.invalid) return;
    const formValues = this.customerForm.value;

    if (this.isEditMode && this.customerId) {
      // EDITAR
      this.customerService.update(this.customerId, formValues).subscribe({
        next: () => {
          alert('Guardado correctamente');
          this.router.navigate(['/product-admin']);
        },
        error: (err: any) => alert('Error al guardar')
      });
    } else {
      // CREAR
      const newCustomer = {
        ...formValues,
        password: '1234',
        isAdmin: false,
        deudas: 0,
        facturas: [],
        facturasCount: 0
      };
      this.customerService.create(newCustomer).subscribe({
        next: () => {
          alert('Creado correctamente');
          this.router.navigate(['/product-admin']);
        },
        error: (err: any) => alert('Error al crear')
      });
    }
  }
}