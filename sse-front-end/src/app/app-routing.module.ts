import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ChatComponent} from "./chat/chat.component";
import {JoinComponent} from "./join/join.component";

const routes: Routes = [
  {
    path: 'join',
    component: JoinComponent
  },
  {
    path: ':talkbotId/chat/:userId',
    component: ChatComponent,
  },
  {
    path: '',
    redirectTo: '/join',
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
