import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';

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
      nombre: ['', Validators.required],
      correo: ['', [Validators.required, Validators.email]],
      antiguedad: [0, Validators.required],
      deudas: [0],
      isAdmin: [false],
      password: ['']
    });
  }

  ngOnInit() {
    this.customerId = this.route.snapshot.paramMap.get('id');
    if (this.customerId) {
      this.isEditMode = true;
      this.customerService.getById(this.customerId).subscribe({
        next: (c: any) => {
          // RELLENAMOS TODO MENOS LA CONTRASEÑA
          this.customerForm.patchValue({
            nombre: c.nombre,
            correo: c.correo,
            antiguedad: c.antiguedad,
            deudas: c.deudas,
            isAdmin: c.isAdmin,
            password: ''
          });
          
          this.customerForm.get('password')?.clearValidators();
          this.customerForm.get('password')?.updateValueAndValidity();
        },
        error: (err: any) => console.error(err)
      });
    } else {
      // Si es CREAR, la contraseña es obligatoria por defecto '1234'
      this.customerForm.patchValue({ password: '1234' });
    }
  }

  save() {
    if (this.customerForm.invalid) return;
    const data = this.customerForm.value;

    if (this.isEditMode && this.customerId) {
      // EDITAR
      this.customerService.update(this.customerId, data).subscribe({
        next: () => this.router.navigate(['/product-admin']),
        error: () => alert('Error al actualizar')
      });
    } else {
      // CREAR
      const newCustomer = {
        ...data,
        facturas: [],
        facturasCount: 0
      };
      this.customerService.create(newCustomer).subscribe({
        next: () => {
          alert('Creado con éxito (Pass: 1234)');
          this.router.navigate(['/product-admin']);
        },
        error: () => alert('Error al crear')
      });
    }
  }

  cancel() {
    this.router.navigate(['/product-admin']);
  }
}