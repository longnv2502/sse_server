import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {ChatService} from "./chat.service";
import {ConversationInfoModel, SentAtType} from "./conversation-info.model";
import {switchMap} from "rxjs";
import {MessageDataModel, MessageDtoModel, MessageModel} from "./message.model";
import {Stack} from "../utils/Stack";

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})
export class ChatComponent implements OnInit {
  @ViewChild('input') input: ElementRef | undefined;
  textMessage: string = '';
  talkbotId: string = '';
  userId: string = '';
  conversationInfo: ConversationInfoModel | undefined;
  conversationId: string = '';
  index = 0;

  conversations:Stack<MessageDtoModel> = new Stack();

  constructor(private route: ActivatedRoute,
              private chatService: ChatService) {
  }

  ngOnInit(): void {
    this.talkbotId = this.route.snapshot.params['talkbotId'];
    this.userId = this.route.snapshot.params['userId'];

    this.chatService.startConversation(this.talkbotId, this.userId).pipe(switchMap((resp) => {
      this.conversationId = resp.body.conversationId;
      this.conversationInfo = { talkbotId: this.talkbotId, userId: this.userId, conversationId: this.conversationId, sentAt: SentAtType.USER};
      return this.chatService.getMessage(this.talkbotId, this.userId, this.conversationId)
    })).subscribe((resp) => {
      const messageData: MessageDataModel = JSON.parse(resp.data);
      const replies: {type:string, message:string}[] = JSON.parse(messageData.text);
      replies.forEach(reply => this.add2Conversation(reply.message, SentAtType.BOT));
    });
    }

  sent() {
    this.add2Conversation(this.textMessage, SentAtType.USER);
    const message: MessageModel = {conversationInfo: this.conversationInfo , data: {text: this.textMessage, index: this.index++}}
    this.chatService.sendMessage(message).subscribe(() => {
      this.textMessage = '';
      this.input?.nativeElement.focus();
    })
  }

  add2Conversation(message: string, sentAt: SentAtType) {
    if (this.conversations.peek()?.sentAt === sentAt) {
      // @ts-ignore
      const diffTime = Math.abs(this.conversations.peek()?.time.getTime() - new Date().getTime());
      // diffTime < 15s
      if (diffTime < 15 * 1000) {
        this.conversations.peek()?.messages?.push(message);
        return;
      }
    }
    this.conversations.push({messages: [message], sentAt, time: new Date()})
  }

}
