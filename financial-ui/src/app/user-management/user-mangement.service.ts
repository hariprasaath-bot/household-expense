import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class UserMangementService {
  
  baseUrl =  "http://localhost:8090/";
  contextPath = ""

  // http://localhost:8090/auth/signup

  constructor(private httpService:HttpClient) { }

  createUser(user:any){
      let url = this.baseUrl + this.contextPath + "auth/" + "signup";
      return this.httpService.post(url, user);
  }
}
