import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Constants } from 'src/app/internals/Constants';
import { ErrorConstants } from 'src/app/internals/ErrorConstants';
import { ForgotPasswordRequest } from 'src/app/models/requests/forgotPasswordRequest.model';
import { IdentityService } from 'src/app/services/identity.service';

@Component({
  selector: 'app-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit {
  public forgotPasswordForm: FormGroup;
  public loading: boolean = false;
  public errorMessage: string = null;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private identityService: IdentityService,
    private toastrService: ToastrService) {}
    
    ngOnInit() {
      this.forgotPasswordForm = this.formBuilder.group({
        email: ['', [Validators.required, Validators.pattern(Constants.EMAIL_REGEX)]]
      });
    }

    public get email(): AbstractControl { 
      return this.forgotPasswordForm.get('email'); 
    }

    onSubmit(): void {
      this.errorMessage = null;

      if (this.forgotPasswordForm.pristine) {
        this.errorMessage = ErrorConstants.FILL_ALL_FIELDS;
        return;
      }

      if (this.forgotPasswordForm.invalid) {
        
        this.errorMessage = ErrorConstants.FIX_ALL_ERRORS;
        return;
      }

      this.loading = true;

      this.identityService.forgotPassword(this.createRequestModel()).subscribe(() => {
        this.errorMessage = null;
        this.loading = false;
        this.toastrService.success("Check your email!");
        this.router.navigate(["/login"]);
      },
      httpErrorResponse => {        
        this.errorMessage = httpErrorResponse.error.error || httpErrorResponse.statusText;
        this.loading = false;
      });
  }

  private createRequestModel(): ForgotPasswordRequest {
    let request = new ForgotPasswordRequest();
    request.email = this.email.value;
    return request;
  }
}
