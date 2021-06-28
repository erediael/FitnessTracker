import { Component, OnInit } from '@angular/core';
import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Constants } from 'src/app/internals/Constants';
import { ErrorConstants } from 'src/app/internals/ErrorConstants';
import { ChangePasswordRequest } from 'src/app/models/requests/changePasswordRequest.model';
import { IdentityService } from 'src/app/services/identity.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.scss']
})
export class ChangePasswordComponent implements OnInit {
  public changePasswordForm: FormGroup;
  public loading: boolean = false;
  public errorMessage: string = null;

  constructor(
    private formBuilder: FormBuilder,
    private identityService: IdentityService) {}

    ngOnInit() {
      this.changePasswordForm = this.formBuilder.group({
        oldPassword: ['', Validators.required],
        newPassword: ['', [Validators.required, Validators.pattern(Constants.PASSWORD_REGEX)]],
        confirmedPassword: ['', Validators.required]
      },{
        validators: this.passwordMatcher.bind(this)
      });
      
    }

    passwordMatcher(formGroup: FormGroup) {
      let newPassword: AbstractControl = formGroup.get('newPassword');
      let confirmedPassword: AbstractControl = formGroup.get('confirmedPassword');

      return newPassword.value === confirmedPassword.value 
        ? confirmedPassword.setErrors(null) 
        : confirmedPassword.setErrors({ notEqual: true });
    }

    public get oldPassword(): AbstractControl { 
      return this.changePasswordForm.get('oldPassword'); 
    }

    public get newPassword(): AbstractControl { 
      return this.changePasswordForm.get('newPassword'); 
    }

    public get confirmedPassword(): AbstractControl {
      return this.changePasswordForm.get('confirmedPassword'); 
    }

    onSubmit(): void {
      this.errorMessage = null;

      if (this.changePasswordForm.pristine) {
        this.errorMessage = ErrorConstants.FILL_ALL_FIELDS;
        return;
      }

      if (this.changePasswordForm.invalid) {        
        this.errorMessage = ErrorConstants.FIX_ALL_ERRORS;
        return;
      }

      this.loading = true;

      this.identityService.changePassword(this.createRequestModel()).subscribe(() => {
        this.errorMessage = null;
        this.identityService.logout();
      },
      httpErrorResponse => {
        this.errorMessage = httpErrorResponse.error.error;
        this.loading = false;
      });
  }

  private createRequestModel(): ChangePasswordRequest {
    let request = new ChangePasswordRequest();
    request.oldPassword = this.oldPassword.value;
    request.newPassword = this.newPassword.value;
    return request;
  }
}