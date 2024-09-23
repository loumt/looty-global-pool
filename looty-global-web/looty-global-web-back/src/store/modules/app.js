import {defineStore} from "pinia";

import userDefaultHeader from '@/assets/user-default-header.png'

const useAppStore = defineStore('app',
    {
        state: ()=>({
            sidebar:{
                hide: false
            },
            userInfo:{
                header: userDefaultHeader
            }
        }),
        actions:{
            showAndHideMenu(){
                this.sidebar.hide = !this.sidebar.hide
            }
        }
    }
)

export default useAppStore