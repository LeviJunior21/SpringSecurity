import SockJS from "sockjs-client";
import Stomp, { Client } from "stompjs";
export class WebSocketApp {
    private url: string;
    private webSocket: Client;
    private connected: boolean;
    private myID: number;
    private subscribe: string;
    private endPoint: string;
    private message: string;

    constructor(webSocketAppBuilder: WebSocketAppBuilder) {
        this.url = webSocketAppBuilder.getUrl();
        this.myID = webSocketAppBuilder.getID();
        this.subscribe = webSocketAppBuilder.getSubscribe();
        this.endPoint = webSocketAppBuilder.getEndPoint();

        const sockJS = new SockJS(this.url);
        const stompClient = Stomp.over(sockJS);
        this.webSocket = stompClient;
        this.connected = false;
        this.message = "";
    }

    public onConnect = (): void => {
        if (this.webSocket && this.webSocket.connected) {
            this.connected = true;

            this.webSocket.subscribe(this.subscribe, (data) => {
                const dados = JSON.parse(data.body);
                console.log(dados);
            }, {});
        } else {
            console.log("WebSocket desconectado.");
        }
    };

    public onError = (error: any): void => {
        console.error("Erro ao conectar ao WebSocket", error);
        this.connected = false;
        setTimeout(this.connect, 5000);
    };

    public connect(): void {
        this.webSocket.connect({}, () => this.onConnect(), this.onError);
    };

    public setMessage(message: string): void {
        this.message = message;
    }

    public getConnected(): boolean {
        return this.connected;
    }

    public send(idDestiny: number): void {
        const data = {
            mensagem: this.message,
            receptor: idDestiny,
            remetente: this.myID,
            timestamp: new Date()
        };
        this.webSocket.send(this.endPoint, {}, JSON.stringify(data));
    }

    public disconnect(): void {
        if (this.webSocket && this.webSocket.connected) {
            this.webSocket.disconnect(() => {
                console.log("Desconectado do WebSocket");
            });
        }
    }

    public getWebSocket(): Client {
        return this.webSocket;
    }

    public static builder(): WebSocketAppBuilder {
        return new WebSocketAppBuilder();
    }
}

class WebSocketAppBuilder {
    private url: string;
    private id: number;
    private subscribe: string;
    private endPoint: string;

    constructor() {
        this.url = "";
        this.subscribe = "";
        this.endPoint = "";
        this.id = 1;
    }

    public withUrl(url: string): WebSocketAppBuilder {
        this.url = url;
        return this;
    }

    public getUrl(): string {
        return this.url;
    }

    public withID(id: number): WebSocketAppBuilder {
        this.id = id;
        return this;
    }

    public getID(): number {
        return this.id;
    }

    public withSubscribe(subscribe: string): WebSocketAppBuilder {
        this.subscribe = subscribe;
        return this;
    }

    public getSubscribe(): string {
        return this.subscribe;
    }

    public withEndPoint(endPoint: string): WebSocketAppBuilder {
        this.endPoint = endPoint;
        return this;
    }

    public getEndPoint(): string {
        return this.endPoint;
    }

    public build(): WebSocketApp {
        return new WebSocketApp(this);
    }
}
