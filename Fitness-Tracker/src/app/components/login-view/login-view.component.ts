import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router, RouterStateSnapshot } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Constants } from 'src/app/internals/Constants';
import { ErrorConstants } from 'src/app/internals/ErrorConstants';
import { UserLoginRequest } from 'src/app/models/requests/userLoginRequest.model';
import { IdentityService } from 'src/app/services/identity.service';

@Component({
  selector: 'app-login-view',
  templateUrl: './login-view.component.html',
  styleUrls: ['./login-view.component.scss']
})
export class LoginViewComponent implements OnInit {
  public loginForm: FormGroup;
  public loading: boolean = false;
  public errorMessage: string = null;

  constructor(
      private formBuilder: FormBuilder,
      private router: Router,
      private identityService: IdentityService,
      private toastrService: ToastrService) {}

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.pattern(Constants.EMAIL_REGEX)]],
      password: ['', Validators.required]
    });
  }

  public get email(): AbstractControl { 
    return this.loginForm.get('email'); 
  }

  public get password(): AbstractControl { 
    return this.loginForm.get('password'); 
  }

  onSubmit(): void {
    this.errorMessage = null;

    if (this.loginForm.pristine) {
      this.errorMessage = ErrorConstants.FILL_ALL_FIELDS;
      return;
    }

    if (this.loginForm.invalid) {
      this.errorMessage = ErrorConstants.FIX_ALL_ERRORS;
      return;
    }

    this.loading = true;

    this.identityService.login(this.createRequestModel()).subscribe(() => {
      this.errorMessage = null;
      this.loading = false;
      this.toastrService.success("Successful login!");
      this.router.navigate([`/calendar/${this.identityService.getLoggedUserId()}`]);
    },
    httpErrorResponse => {
      this.errorMessage = httpErrorResponse.error.error || httpErrorResponse.statusText;
      this.loading = false;
    });
  }

  private createRequestModel(): UserLoginRequest {
    let request = new UserLoginRequest();
    request.email = this.email.value;
    request.password = this.password.value;
    return request;
  }
}
