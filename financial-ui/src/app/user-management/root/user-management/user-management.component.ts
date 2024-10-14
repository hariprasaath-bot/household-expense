import { Component } from '@angular/core';
import { SignUpComponent } from "../../components/sign-up/sign-up.component";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [SignUpComponent],
  templateUrl: './user-management.component.html',
  styleUrl: './user-management.component.scss'
})
export class UserManagementComponent {

}
