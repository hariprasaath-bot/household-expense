import { Component, ElementRef, ViewChild } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faGoogle } from '@fortawesome/free-brands-svg-icons';
import { RippleModule } from 'primeng/ripple';
import { PrimeNGConfig } from 'primeng/api'; 
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sign-up-forms',
  standalone: true,
  imports: [FontAwesomeModule,RippleModule, CommonModule],
  templateUrl: './sign-up-forms.component.html',
  styleUrl: './sign-up-forms.component.scss'
})
export class SignUpFormsComponent {
  google = faGoogle;
  redirect_uri="http://localhost:8090/signup"
  client_id="879827764981-sbq6g552jiha2ucqkbg3gltqcqqcn6gg.apps.googleusercontent.com"
  googleSignUpUri = "https://accounts.google.com/o/oauth2/v2/auth?redirect_uri="+
  this.redirect_uri+"&response_type=code&client_id="+this.client_id+"&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline";

  isBorderActive: boolean = false;

  constructor(private primengConfig: PrimeNGConfig){

  }
  ngOnInit() {
    this.primengConfig.ripple = true; 
  }
}
