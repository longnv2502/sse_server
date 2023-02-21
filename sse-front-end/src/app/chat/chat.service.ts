import {Injectable, NgZone} from "@angular/core";
import {Observable} from "rxjs";
import {HttpClient, HttpResponse} from "@angular/common/http";
import {MessageModel} from "./message.model";
import {SentAtType} from "./conversation-info.model";

@Injectable({
  providedIn: "root"
})
export class ChatService {
  private eventSource: EventSource | undefined;
  private END_POINT_PATH = '/sse-server'
  constructor(private http: HttpClient,
              private zone: NgZone) {
  }

  subscribe(talkbotId: String, userId: String, conversationId: String):EventSource {
    if (this.eventSource) {
      console.log('EventSource closed.');
      this.eventSource.close();
    }
    this.eventSource = new EventSource(this.END_POINT_PATH + `/${talkbotId}/subscribe/${userId}/${conversationId}?sentAt=${SentAtType.USER}`);
    return this.eventSource;
  }

  getMessage(talkbotId: String, userId: String, conversationId: String): Observable<MessageEvent> {
    return new Observable(observer => {
      const eventSource = this.subscribe(talkbotId, userId, conversationId);
      eventSource.onopen = (ev) => {
        console.log('Connection to server opened.', ev);
      };
      eventSource.onerror = (ev) => {
        console.log('EventSource failed.', ev);
      };
      eventSource.addEventListener('latest', event => {
        this.zone.run(() => {
          observer.next(event);
        });
      });
    });
  }

  sendMessage(message: MessageModel): Observable<HttpResponse<any>> {
    return this.http.post(this.END_POINT_PATH + `/send`, message, { observe: 'response' });
  }

  startConversation(talkbotId: String, userId: String): Observable<HttpResponse<any>> {
    return this.http.get(this.END_POINT_PATH + `/${talkbotId}/startConversation/${userId}`, { observe: 'response' });
  }

}
