import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { IdentityService } from '../services/identity.service';

@Injectable({
    providedIn: 'root'
})
export class AuthGuard implements CanActivate {
    constructor(private router: Router, private identityService: IdentityService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {       
        if (this.identityService.isLogged()) {
            if (!this.identityService.isJwtExpired())
            {
                return true;
            }

            this.identityService.logout();
        }

        return false;
    }
}