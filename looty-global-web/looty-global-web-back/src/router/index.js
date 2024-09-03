import {createRouter, createWebHistory} from 'vue-router'

export const constantRoutes = [
    {
        path: '/login',
        component: () => import('@/views/login')
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: constantRoutes
})
export default router