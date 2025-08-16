import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { SignUpComponent } from "../../components/sign-up/sign-up.component";

@Component({
  selector: 'app-user-management',
  standalone: true,
  imports: [SignUpComponent],
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.scss']
})
export class UserManagementComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }
}
