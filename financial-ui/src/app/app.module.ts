import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomefinanceComponent } from './components/home/homefinance/homefinance.component';
import { UsermanagementComponent } from './components/user/usermanagement/usermanagement.component';
import { PersonalfinanceComponent } from './components/personal/personalfinance/personalfinance.component';
import { ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/user/login/login.component';

@NgModule({
  declarations: [
    AppComponent,
    HomefinanceComponent,
    UsermanagementComponent,
    PersonalfinanceComponent,
    LoginComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
