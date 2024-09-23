import * as ElementPlusIconsVue from '@element-plus/icons-vue'

export default{
    install: (app) => {
        for(let i in ElementPlusIconsVue){
            app.component(i, ElementPlusIconsVue[i])
        }
    }
}