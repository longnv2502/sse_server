import {ConversationInfoModel, SentAtType} from "./conversation-info.model";

export interface MessageModel {
  conversationInfo?: ConversationInfoModel;
  data?: MessageDataModel;
}

export interface MessageDataModel {
  index: number;
  text: string;
}

export interface MessageDtoModel {
  messages?: String[];
  sentAt: SentAtType;
  time:Date;
}
