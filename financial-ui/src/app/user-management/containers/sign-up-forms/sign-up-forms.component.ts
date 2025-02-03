import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faGoogle } from '@fortawesome/free-brands-svg-icons';
import { RippleModule } from 'primeng/ripple';
import { CommonModule } from '@angular/common';
import { ProgressLightServiceService } from '../../../common/service/progress-light-service.service';
import { ReactiveFormsModule } from '@angular/forms'; // Import ReactiveFormsModule
import { UserMangementService } from '../../user-mangement.service';
import { AuthService } from '../../../auth/service/auth.service';

@Component({
  selector: 'app-sign-up-forms',
  standalone: true,
  imports: [FontAwesomeModule, RippleModule, CommonModule, ReactiveFormsModule], // Add ReactiveFormsModule to imports
  templateUrl: './sign-up-forms.component.html',
  styleUrl: './sign-up-forms.component.scss'
})
export class SignUpFormsComponent implements OnInit {
  google = faGoogle;
  redirect_uri = "http://localhost:8090/signup";
  client_id = "879827764981-sbq6g552jiha2ucqkbg3gltqcqqcn6gg.apps.googleusercontent.com";
  googleSignUpUri = "https://accounts.google.com/o/oauth2/v2/auth?redirect_uri=" +
    this.redirect_uri + "&response_type=code&client_id=" + this.client_id + "&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile+openid&access_type=offline";

  isBorderActive: boolean = false;

  newUserForm!: FormGroup; 
  signInForm !: FormGroup

  constructor( private UserMangementService: UserMangementService, private authSerive: AuthService, private progressService: ProgressLightServiceService, private fb: FormBuilder) { }

  ngOnInit() {
    this.doTheFireThing();
    this.initForm(); // Initialize the form in ngOnInit
  }

  initForm() {
    this.newUserForm = this.fb.group({
      deceasedName: ['', Validators.required],
      deceasedEmail: ['', [Validators.required, Validators.email]],
      deceasedSecret: ['', [Validators.required, Validators.minLength(6)]]
    }); // Add custom validator
    this.signInForm = this.fb.group({
      deceasedEmail: ['', [Validators.required, Validators.email]],
      deceasedSecret: ['', [Validators.required, Validators.minLength(6)]]
    }); // Add custom validator
  }


  doTheFireThing() {
    this.progressService.progress$.subscribe(value => {
      this.isBorderActive = value > 0;
    });
  }

  createTheUser() {
    if (this.newUserForm.valid) {
      let userDetails = this.formnewUserPayload(this.newUserForm.value); // Process the form data
      
      this.UserMangementService.createUser(userDetails).subscribe((resp)=>{
        this.authSerive.login(userDetails.email,userDetails.password).subscribe((resp) =>{
          console.log("login successful")
        }, 
      (err)=>{
        console.log("login failed")
      });
      })
    } else {
      // Mark all controls as touched to display validation errors
      this.markFormGroupTouched(this.newUserForm);
    }
  }

  userSignIn(){
    this.authSerive.login(this.signInForm.value.deceasedEmail,this.signInForm.value.deceasedSecret).subscribe((resp) =>{
      console.log("login successful")
    }, 
  (err)=>{
    console.log("login failed")
  });
  }



  private formnewUserPayload(userDetails: any) {
    return {
      name: userDetails.deceasedName,
      email: userDetails.deceasedEmail,
      password: userDetails.deceasedSecret
    };
  }
  

  private markFormGroupTouched(formGroup: FormGroup) {
    formGroup.markAllAsTouched();
  }

}