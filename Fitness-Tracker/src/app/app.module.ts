import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { CalendarViewComponent } from './components/calendar-view/calendar-view.component';
import { NavigationComponent } from './components/layout/navigation/navigation.component';
import { FooterComponent } from './components/layout/footer/footer.component';
import { HomeComponent } from './components/home/home.component';
import { LoginViewComponent } from './components/login-view/login-view.component';
import { RegisterViewComponent } from './components/register-view/register-view.component';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { FriendsViewComponent } from './components/friends-view/friends-view.component';
import { FriendsAddComponent } from './components/friends-add/friends-add.component';
import { JwtInterceptorInterceptor } from './interceptors/jwt-interceptor.interceptor';
import { MainContentComponent } from './components/layout/main-content/main-content.component';


@NgModule({
  declarations: [
    AppComponent,
    CalendarViewComponent,
    NavigationComponent,
    FooterComponent,
    HomeComponent,
    LoginViewComponent,
    RegisterViewComponent,
    ForgotPasswordComponent,
    ChangePasswordComponent,
    FriendsViewComponent,
    FriendsAddComponent,
    MainContentComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    ToastrModule.forRoot(),
    ReactiveFormsModule,
    HttpClientModule
  ],
  providers: [{ provide: HTTP_INTERCEPTORS, useClass: JwtInterceptorInterceptor, multi: true }],
  bootstrap: [AppComponent]
})
export class AppModule { }
