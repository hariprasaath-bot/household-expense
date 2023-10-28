import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms'; // Import necessary modules

@Component({
  selector: 'app-usermanagement',
  templateUrl: './usermanagement.component.html',
  styleUrls: ['./usermanagement.component.css']
})
export class UsermanagementComponent implements OnInit {
  public UserForm: FormGroup = new FormGroup({});
  isDarkMode = false;
  public formErrors = {
    lastName: '',
    firstName: '',
    ssnKey: '',
    phone: '',
    email: '',
    ssn: '',
  };

  ngOnInit() {
    this.buildForm();
  }

  constructor(private form: FormBuilder) {} 

  onSubmit() {
    console.log(this.UserForm.value);
    this.buildForm()
  }

  toggleDarkMode() {
    this.isDarkMode = !this.isDarkMode;

    // Update CSS variables based on the mode
    if (this.isDarkMode) {
      document.documentElement.style.setProperty('--background-color', 'var(--background-color-dark)');
      document.documentElement.style.setProperty('--text-color', 'var(--text-color-dark)');
    } else {
      document.documentElement.style.setProperty('--background-color', 'var(--background-color-light)');
      document.documentElement.style.setProperty('--text-color', 'var(--text-color-light)');
    }
  }

  buildForm() {
    this.UserForm = this.form.group({
      name: ['', Validators.required],
      status: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
    });
  }
}
