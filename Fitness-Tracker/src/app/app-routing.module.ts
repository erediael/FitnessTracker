import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CalendarViewComponent } from './components/calendar-view/calendar-view.component';
import { AuthGuard } from './guards/authGuard';
import { HomeComponent } from './components/home/home.component';
import { LoginViewComponent } from './components/login-view/login-view.component';
import { RegisterViewComponent } from './components/register-view/register-view.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { ChangePasswordComponent } from './components/change-password/change-password.component';
import { FriendsViewComponent } from './components/friends-view/friends-view.component';
import { FriendsAddComponent } from './components/friends-add/friends-add.component';
import { AccessGuard } from './guards/accessGuard';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginViewComponent, canActivate: [AccessGuard]},
  {path: 'register', component: RegisterViewComponent, canActivate: [AccessGuard]},
  {path: 'forgotPassword', component: ForgotPasswordComponent, canActivate: [AccessGuard]},
  {path: 'changePassword', component: ChangePasswordComponent, canActivate: [AuthGuard]},
  {path: 'friends/:id', component: FriendsAddComponent, canActivate: [AuthGuard]},
  {path: 'friends', component: FriendsViewComponent, canActivate: [AuthGuard]},
  {path: 'calendar/:id', component: CalendarViewComponent, canActivate: [AuthGuard]},
  {path: '**', redirectTo: ''}, // not found routes
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
