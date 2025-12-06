import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({ providedIn: 'root' })
export class AdminProductService {

  private api = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  list() { return this.http.get<any[]>(`${this.api}/products`); }
  get(id: string) { return this.http.get(`${this.api}/products/${id}`); }
  create(data: any) { return this.http.post(`${this.api}/products`, data); }
  update(id: string, data: any) { return this.http.put(`${this.api}/products/${id}`, data); }
  delete(id: string) { return this.http.delete(`${this.api}/products/${id}`); }
}
