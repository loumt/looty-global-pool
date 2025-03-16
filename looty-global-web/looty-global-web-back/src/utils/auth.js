import cookie from 'js-cookie'
const key = 'looty-web-token'
export function getToken(){
    cookie.get(key)
}
export function setToken(token){
    cookie.set(key, token)
}
export function removeToken(){
    cookie.remove(key)
}