import { Component } from '@angular/core';
import { IdentityService } from '../../../services/identity.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent {
  constructor(private identityService: IdentityService) { }

  isLogged(): boolean {
    return this.identityService.isLogged();
  }

  getLoggedUserId(): string {
    return this.identityService.getLoggedUserId();
  }

  getLoggedUserName(): string {
    return this.identityService.getLoggedUsername();
  }

  logout(): void {
    this.identityService.logout();
  }
}
