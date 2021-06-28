import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { UserLoginResponse } from '../models/responses/userLoginResponse.model';
import { UserLoginRequest } from '../models/requests/userLoginRequest.model';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { ChangePasswordRequest } from '../models/requests/changePasswordRequest.model';
import { UserRegisterRequest } from '../models/requests/userRegisterRequest.model';
import { ForgotPasswordRequest } from '../models/requests/forgotPasswordRequest.model';
import { ErrorResponse } from '../models/responses/errorResponse.model';
import { AddFriendRequest } from '../models/requests/addFriendRequest.model';
import { UserResponse } from '../models/responses/userResponse.model';

@Injectable({
  providedIn: 'root'
})
export class IdentityService {
  private userId: string = "";
  private user: string = "";
  private localStorageUser: string = "jwt";
  
  constructor(private router: Router, private httpClient: HttpClient) { }

  public login(data: UserLoginRequest): Observable<void> {
    return this.httpClient.post<UserLoginResponse>(`${environment.apiUrl}/login`, data)
      .pipe(map((userLoginResponse: UserLoginResponse) => 
      {
        localStorage.setItem(this.localStorageUser, userLoginResponse.jwt);
        this.setUserFromLocalStorate();
      }));
  }

  public register(data: UserRegisterRequest): Observable<ErrorResponse> {
    return this.httpClient.post<ErrorResponse>(`${environment.apiUrl}/register`, data);
  }

  public getUserById(userId: string): Observable<UserResponse> {
    return this.httpClient.get<UserResponse>(`${environment.apiUrl}/users/${userId}`);
  }

  public forgotPassword(data: ForgotPasswordRequest): Observable<ErrorResponse> {
    return this.httpClient.post<ErrorResponse>(`${environment.apiUrl}/forgotPassword`, data);
  }

  public changePassword(data: ChangePasswordRequest): Observable<ErrorResponse> {
    return this.httpClient.post<ErrorResponse>(`${environment.apiUrl}/changePassword`, data);
  }

  public logout(): void {
    localStorage.removeItem(this.localStorageUser);
    this.userId = "";
    this.user = "";
    this.router.navigate(["/login"]);
  }
  
  public getLoggedUsername(): string {
    if (this.user === "") {
      this.setUserFromLocalStorate();
    }

    return this.user;
  }

  public getLoggedUserId(): string {
    if (this.userId === "") {
      this.setUserFromLocalStorate();
    }

    return this.userId;
  }
  
  public getJwt(): string {
    return localStorage.getItem(this.localStorageUser);
  }

  public isJwtExpired(): boolean {
    let jwt = JSON.parse(atob(this.getJwt().split('.')[1]));
    return Date.now() >= jwt.exp * 1000
  }

  public isLogged(): boolean {
    return localStorage.getItem(this.localStorageUser) != null;
  }

  private setUserFromLocalStorate() {
    let jwt = this.getJwt();
    if (jwt != null) {
      let payload = JSON.parse(atob(jwt.split('.')[1]));
      this.userId = payload.sub;
      this.user = payload.username;
    }
  }
}
