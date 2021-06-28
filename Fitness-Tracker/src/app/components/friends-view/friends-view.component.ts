import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { GetUserFriendResponse } from 'src/app/models/responses/getUserFriendsResponse.model';
import { FriendsService } from 'src/app/services/friends.service';
import { IdentityService } from 'src/app/services/identity.service';

@Component({
  selector: 'app-friends-view',
  templateUrl: './friends-view.component.html',
  styleUrls: ['./friends-view.component.scss']
})
export class FriendsViewComponent implements OnInit {
  public friends: Array<GetUserFriendResponse> = [];
  public inviteLink: string = "";
  constructor(private identitySerivce: IdentityService, private friendsSerivce: FriendsService, private toastrService: ToastrService) {
    this.inviteLink = `${location.origin}/friends/${this.identitySerivce.getLoggedUserId()}`
  }

  ngOnInit(): void {
    this.friendsSerivce.getUserFriends().subscribe(data => {
      this.friends = data;
    },
    _ => this.toastrService.error("Failed to fetch friends!"))
  }
}
