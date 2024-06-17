import React, { MutableRefObject, useEffect, useLayoutEffect, useRef, useState } from 'react';
import './App.css';
import { WebSocketApp } from './app/service/websocket';
import { Usuario, getAllUsers, getID } from './app/service/usuario';

const App: React.FC = () => {
    const webSocket: MutableRefObject<WebSocketApp | null> = useRef<WebSocketApp | null>(null);
    const [idDestiny, setIdDestiny] = useState<number>(0);
    const [myID, setMyID] = useState<number>(0);
    const [users, setUsers] = useState<Usuario[]>([]);

    useLayoutEffect(() => {
        const loadUsers = async() => {
            getAllUsers().then(response => {
                setUsers(response);
            })
        } 
        
        loadUsers();
    }, []);

    useEffect(() => {
        const connectToSocket = async(id: number) => {
            if (id > 0) {
                const URL: string = `http://localhost:8080/chat?userId=${encodeURIComponent(id)}`;
                const webSocketApp = WebSocketApp.builder()
                    .withUrl(URL)
                    .withID(id)
                    .withSubscribe(`/user/${id}/private`)
                    .withEndPoint("/app/private-message")
                    .build();

                webSocketApp.connect();
                webSocket.current = webSocketApp;
                setMyID(id);
                const user: Usuario | undefined = users.find(usuario => usuario.id === id);
                if (user != undefined) {
                    console.log(`Agora o seu id será: ${user.id} e seu nome será: ${user.nome}`);
                }
            } else {
                console.log("Não há usuários cadastrados no servidor.");
            }
        }
        
        getID().then(response => connectToSocket(response));

        return () => {
            if (webSocket.current) {
                webSocket.current.disconnect();
            }
        };
    }, []);

    const sendMessage = () => {
        if (webSocket.current && idDestiny > 0 && idDestiny != myID) {
            webSocket.current.send(idDestiny);
            console.log("Enviado");
        }
    }

    return (
        <div className='container'>
            <div className='formulario-mensagem'>
                <input placeholder='Mensagem' className='mensagem' onChange={(data) => webSocket.current?.setMessage(data.target.value)}/>
                <select className='id-destino' onChange={(data) => setIdDestiny(parseInt(data.target.value))}>
                    {users.filter(user => user.id != myID).map((user) => (<option key={user.id} value={user.id}>{user.nome}</option>))}        
                </select>
            </div>
            <button className='botao-enviar' onClick={() => sendMessage()}>Enviar</button>
        </div>
    );
};

export default App;
