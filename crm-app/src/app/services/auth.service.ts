import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) {}

  login(credentials: {email: string, password: string}): Observable<any> {
    return this.http.post(this.apiUrl, credentials).pipe(
      tap((res: any) => {
        if (res.token) {
          // GUARDAMOS LAS LLAVES
          localStorage.setItem('crm_token', res.token);
          localStorage.setItem('crm_email', res.email);
          localStorage.setItem('crm_isAdmin', String(res.isAdmin));
          
          // ESTA ES LA LLAVE IMPORTANTE PARA EL USUARIO:
          localStorage.setItem('crm_user_id', res.customerId);
        }
      })
    );
  }

  logout() {
    localStorage.clear();
  }

  get isAdmin(): boolean {
    return localStorage.getItem('crm_isAdmin') === 'true';
  }

  get correo(): string {
    return localStorage.getItem('crm_email') || '';
  }
}