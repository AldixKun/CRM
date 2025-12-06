import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs'; 

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private baseUrl = 'http://localhost:8080/api/customers'; 

  constructor(private http: HttpClient) {}

  private getHeaders() {
    const token = localStorage.getItem('crm_token'); 
    return {
      headers: new HttpHeaders().set('Authorization', `Bearer ${token}`)
    };
  }

  // Admin usa este
  getAll(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl, this.getHeaders());
  }

  getCustomers() { return this.getAll(); }

  // Busca el usuario filtrando la lista completa en el frontend
  getMyInfoByEmail(email: string): Observable<any> {
    return this.getAll().pipe(
      map(allCustomers => {
        const found = allCustomers.find(c => c.correo === email);
        if (!found) throw new Error('Usuario no encontrado en la lista');
        return found;
      })
    );
  }

  // Métodos estándar para evitar errores en otros componentes
  getById(id: string): Observable<any> { return this.http.get<any>(`${this.baseUrl}/${id}`, this.getHeaders()); }
  create(customer: any): Observable<any> { return this.http.post<any>(this.baseUrl, customer, this.getHeaders()); }
  update(id: string, customer: any): Observable<any> { return this.http.put<any>(`${this.baseUrl}/${id}`, customer, this.getHeaders()); }
  delete(id: string): Observable<any> { return this.http.delete<any>(`${this.baseUrl}/${id}`, this.getHeaders()); }
  
  // Dejamos este por compatibilidad
  getMyInfo(id: string): Observable<any> { return this.getById(id); }
  getInvoices(customerId: string): Observable<any[]> {
    return new Observable(observer => { observer.next([]); observer.complete(); });
  }
}