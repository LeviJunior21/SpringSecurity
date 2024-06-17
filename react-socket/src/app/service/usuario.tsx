import axios from "axios"

interface FazerLoginInterface {
    nome: string,
    senha: string
}
export interface Token {
    token: string | null
}
export const fazerLogin = async(data: FazerLoginInterface): Promise<Token> => {
    let result: Token = {
        token: null
    }

    const response = await axios.post("http://localhost:8080/v1/usuarios/login", data);
    if (response.status == 200) {
        const {token} = response.data;;
        result.token = token;
    }

    return result;
}


export enum ROLE {
    ADMIN,
    FUNCIONARIO
}
interface CriarUsuarioInterface {
    nome: string,
    senha: string,
    role: ROLE
}
export const criarUsuario = async(data: CriarUsuarioInterface): Promise<boolean> => {
    let result: boolean = false;
    const response = await axios.post("http://localhost:8080/v1/usuarios/create", data);
    if (response.status == 201) {
        result = true;
    }

    return result;
}

export interface Usuario {
    id: number,
    nome: string
}
export const getID = async(): Promise<number> => {
    let result: Usuario[] = await getAllUsers();

    if (result.length > 0) {
        const randomIndex = Math.floor(Math.random() * result.length);
        return result[randomIndex].id;
    } else {
        return 0;
    }
}

export const getAllUsers = async(): Promise<Usuario[]> => {
    let result: Usuario[] = [];
    const response = await axios.get("http://localhost:8080/v1/usuarios");
    if (response.status == 200) {
        result = response.data;
    }

    return result;
}
