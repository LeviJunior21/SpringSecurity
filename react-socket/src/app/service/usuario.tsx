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
