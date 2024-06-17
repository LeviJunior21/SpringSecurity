import React, { MutableRefObject, useEffect, useLayoutEffect, useRef, useState } from 'react';
import './App.css';
import SockJS from "sockjs-client";
import Stomp, { Client } from "stompjs";
import { WebSocketApp } from './app/service/websocket';

const App: React.FC = () => {
    const webSocket: MutableRefObject<Client | null> = useRef<Client | null>(null);
    const [connected, setConnected] = useState<boolean>(false);
    const URL: string = `http://localhost:8080/chat?userId=${encodeURIComponent(1152)}`;
    const URIToSend: string = "/app/private-message";
    const [meuId, setMeuID] = useState<number>(1000);
    const [outroId, setOutro] = useState<number>(1002);

    useLayoutEffect(() => {
        const connect = () => {
            const sockJS = new SockJS(URL);
            const stompClient = Stomp.over(sockJS);
            webSocket.current = stompClient;
    
            const onConnect = () => {
                if (webSocket.current && webSocket.current.connected) {
                    setConnected(true);
                    
                    webSocket.current.subscribe("/user/" + meuId + "/private", (data) => {
                        const dados = JSON.parse(data.body);
                        console.log("Dados recebidos:")
                        console.log(dados);
                    }, {});
                }
            };
    
            const onError = (error: any) => {
                console.error("Erro ao conectar ao WebSocket", error);
                setConnected(false);
                setTimeout(connect, 5000);
            };
    
            webSocket.current.connect({ command: 'CONNECT',
                header: 
                { token: "",
                  'accept-version': '1.1,1.0',
                  'heart-beat': '10000,10000' },
                body: 'oiiiiiiiiiiiiiiii'}, onConnect, onError);
        };
    
        connect();
    
        return () => {
            if (webSocket.current && webSocket.current.connected) {
                webSocket.current.disconnect(() => {
                    console.log("Desconectado do WebSocket");
                });
            }
        };
    }, [URL, meuId, setMeuID, outroId, setOutro]);

    const sendMessage = () => {
        const mensagemEnviar = {
            mensagem: "Oiiiiiiiiiii".replace(/^\s+|\s+$/g, ''),
            timestamp: new Date(),
            remetente: meuId,
            receptor: outroId
        };
    
        if (webSocket.current) {
            webSocket.current.send(URIToSend, {}, JSON.stringify(mensagemEnviar));
        }
    }

    useEffect(() => {
        const id: number = 1152;
        const data = {
            mensagem: "Boa noite!", 
            receptor: 1002, 
            remetente: 1000, 
            timestamp: new Date()
        }
        const webSocketApp = WebSocketApp.builder()
            .withUrl(URL)
            .withID(id)
            .withSubscribe(`/user/${id}/private`)
            .build();
        //webSocketApp.connect();
        //webSocketApp.send("/app/private-message", {}, data);

        return () => {
            // webSocket.disconnect();
        };
    }, []);

    return (
        <div className='container'>
            <div className='cont'>
                <input placeholder='MeuID' className='inputs' onChange={(data) => setMeuID(parseInt(data.target.value))}/>
                <input placeholder='OutroID' className='inputs' onChange={(data) => setOutro(parseInt(data.target.value))}/>
            </div>
            <button className='botao' onClick={() => sendMessage()}>Enviar</button>

        </div>
    );
};

export default App;
