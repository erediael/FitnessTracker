import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Constants } from 'src/app/internals/Constants';
import { ErrorConstants } from 'src/app/internals/ErrorConstants';
import { UserRegisterRequest } from 'src/app/models/requests/userRegisterRequest.model';
import { IdentityService } from 'src/app/services/identity.service';

@Component({
  selector: 'app-register-view',
  templateUrl: './register-view.component.html',
  styleUrls: ['./register-view.component.scss']
})
export class RegisterViewComponent implements OnInit {
  public registerForm: FormGroup;
  public loading: boolean = false;
  public errorMessage: string = null;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private identityService: IdentityService,
    private toastrService: ToastrService) {}

    ngOnInit() {
      this.registerForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.pattern(Constants.EMAIL_REGEX)]],
        username: ['', [Validators.required, Validators.pattern(Constants.USERNAME_REGEX)]],
        password: ['', [Validators.required, Validators.pattern(Constants.PASSWORD_REGEX)]]
      });
    }

    public get email(): AbstractControl { 
      return this.registerForm.get('email'); 
    }

    public get username(): AbstractControl { 
      return this.registerForm.get('username'); 
    }
  
    public get password(): AbstractControl { 
      return this.registerForm.get('password');
    }

    onSubmit(): void {
        this.errorMessage = null;

        if (this.registerForm.pristine) {
          this.errorMessage = ErrorConstants.FILL_ALL_FIELDS;
          return;
        }

        if (this.registerForm.invalid) {
          this.errorMessage = ErrorConstants.FIX_ALL_ERRORS;
          return;
        }

        this.loading = true;

        this.identityService.register(this.createRequestModel()).subscribe(() => {
          this.errorMessage = null;
          this.loading = false;
          this.toastrService.success("Successful registration!");
          this.router.navigate(["/login"]);
        },
        httpErrorResponse => {
          this.errorMessage = httpErrorResponse.error.error || httpErrorResponse.statusText;
          this.loading = false;
        });
    }

  private createRequestModel(): UserRegisterRequest {
    let request = new UserRegisterRequest();
    request.email = this.email.value;
    request.username = this.username.value;
    request.password = this.password.value;
    return request;
  }
}
