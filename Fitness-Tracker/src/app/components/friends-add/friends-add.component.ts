import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { AddFriendRequest } from 'src/app/models/requests/addFriendRequest.model';
import { UserResponse } from 'src/app/models/responses/userResponse.model';
import { FriendsService } from 'src/app/services/friends.service';
import { IdentityService } from 'src/app/services/identity.service';

@Component({
  selector: 'app-friends-add',
  templateUrl: './friends-add.component.html',
  styleUrls: ['./friends-add.component.scss']
})
export class FriendsAddComponent implements OnInit {
  public loading: boolean = false;
  public errorMessage: string = null;
  private user: UserResponse;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private identityService: IdentityService,
    private friendsService: FriendsService,
    private toastrService: ToastrService) {}

  ngOnInit() {
    this.activatedRoute.params.subscribe((params) => {
      if (params.id === this.identityService.getLoggedUserId()) {
        this.router.navigate(['/friends']);
      }

      this.identityService.getUserById(params.id).subscribe((userResponse) => {
        this.user = userResponse;
      })
    });
  }

  public accept(): void {
    this.loading = true;

    let addFriendRequest = new AddFriendRequest();
    addFriendRequest.userId = this.user.id;

    this.friendsService.addFriend(addFriendRequest).subscribe(() => {
      this.errorMessage = null;
      this.loading = false;
      this.toastrService.success("Successfully added friend!");
      this.router.navigate(['/friends']);
    },
    httpErrorResponse => {
      this.errorMessage = httpErrorResponse.error.error || httpErrorResponse.statusText;
      this.loading = false;
    });
  }

  public cancel(): void {
    this.router.navigate(['/friends']);
  }
}
