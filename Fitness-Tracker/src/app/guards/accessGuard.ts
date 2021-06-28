import { Injectable } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { IdentityService } from '../services/identity.service';

@Injectable({
    providedIn: 'root'
})
export class AccessGuard implements CanActivate {
    constructor(private router: Router, private identityService: IdentityService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        let path = route.url[0].path;
        if (!this.identityService.isLogged() && (path === "login" || path === "register" || path === "forgotPassword")) {
            return true;
        }

        this.router.navigate(['']);
        return true;
    }
}