export interface ConversationInfoModel {
  userId: string;
  talkbotId: string;
  conversationId: string;
  sentAt: SentAtType;
}

export enum SentAtType {
  USER = 'USER',
  BOT = 'BOT'
}
