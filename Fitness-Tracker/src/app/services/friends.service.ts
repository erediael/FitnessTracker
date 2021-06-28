import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AddFriendRequest } from '../models/requests/addFriendRequest.model';
import { ErrorResponse } from '../models/responses/errorResponse.model';
import { GetUserFriendResponse } from '../models/responses/getUserFriendsResponse.model';
import { IdentityService } from './identity.service';

@Injectable({
  providedIn: 'root'
})
export class FriendsService {  
  constructor(private httpClient: HttpClient) { }
  
  public getUserFriends(): Observable<Array<GetUserFriendResponse>> {
    return this.httpClient.get<Array<GetUserFriendResponse>>(`${environment.apiUrl}/friends`);
  }

  public addFriend(data: AddFriendRequest): Observable<ErrorResponse> {
    return this.httpClient.post<ErrorResponse>(`${environment.apiUrl}/friends`, data);
  }
}
