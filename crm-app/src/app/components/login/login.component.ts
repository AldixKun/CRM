import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  correo = '';
  password = '';
  error = ''; 
  showPassword = signal(false);

  constructor(private auth: AuthService, private router: Router) {}

  togglePassword() {
    this.showPassword.update(valorActual => !valorActual);
  }

  login() {
    const credentials = {
      email: this.correo,
      password: this.password
    };

    this.auth.login(credentials).subscribe({
      next: (res: any) => {
        console.log('Login CRM OK');
        this.router.navigate(['/product-admin']); 
      },
      error: (err: any) => {
        console.error(err);
        this.error = 'Credenciales inválidas o error de conexión';
      }
    });
  }
}