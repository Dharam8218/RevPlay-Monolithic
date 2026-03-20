import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private baseUrl = '/api/revplay';

  constructor(private http: HttpClient) {}

  login(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/login`, data);
  }

  register(data: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, data);
  }

  saveAuthData(response: any) {
    localStorage.setItem('token', response.token);
    localStorage.setItem('username', response.username);
    localStorage.setItem('email', response.email);
    localStorage.setItem('roles', JSON.stringify(response.roles));
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getRoles(): string[] {
    const roles = localStorage.getItem('roles');
    return roles ? JSON.parse(roles) : [];
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    localStorage.removeItem('email');
    localStorage.removeItem('roles');
  }

  isLoggedIn(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp * 1000 > Date.now();
    } catch {
      return false;
    }
  }

  registerUser(formData: FormData, role: string) {
    const endpoint =
      role === 'ARTIST' ? `${this.baseUrl}/register/artist` : `${this.baseUrl}/register/user`;

    return this.http.post(endpoint, formData);
  }

  getProfile() {
    return this.http.get<any>(`${this.baseUrl}/get-profile`);
  }
  updateProfile(formData: FormData) {
    return this.http.put(`${this.baseUrl}/update-profile`, formData);
  }

  getArtistProfile(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/artist/profile`);
  }

  updateArtistProfile(formData: FormData): Observable<any> {
    return this.http.put(`${this.baseUrl}/artist/profile`, formData);
  }
}
