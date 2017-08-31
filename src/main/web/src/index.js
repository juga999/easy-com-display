import Vue from 'vue'
import VueRouter from 'vue-router'
import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import 'font-awesome/css/font-awesome.css'

Vue.use(VueRouter)
Vue.use(BootstrapVue)

import App from './components/App.vue'

const AdminView = () => import(/* webpackChunkName: "AdminView" */ './components/admin/AdminView.vue')
const StreamDetail = () => import(/* webpackChunkName: "StreamDetail" */ './components/StreamDetail.vue')
const StreamView = () => import(/* webpackChunkName: "StreamView" */ './components/StreamView.vue')

window.document.title= 'Easy Com Display';

const routes = [
    {
        path: '/admin',
        component: AdminView
    },
    {
        path: '/admin/stream/:id',
        name: 'stream-detail',
        component: StreamDetail
    },
    {
        path: '/show-stream/:id',
        name: 'stream-view',
        component: StreamView
    },
    {
        path: '/show-current-stream',
        name: 'current-stream-view',
        component: StreamView
    },
    // catch all redirect
    {
        path: '*', redirect: '/admin'
    }
]

const router = new VueRouter({
    routes
})

const app = new Vue({
    router,
    render: h => h(App),
}).$mount('#app')
