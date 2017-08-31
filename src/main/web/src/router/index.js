import Vue from 'vue'
import Router from 'vue-router'

import AdminView from '@/components/admin/AdminView.vue'
import StreamDetailView from '@/components/admin/StreamDetailView.vue'
import StreamView from  '@/components/kiosk/StreamView.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/admin',
      component: AdminView
    },
    {
      path: '/admin/stream/:id',
      name: 'stream-detail',
      component: StreamDetailView
    },
    {
      path: '/kiosk/show-stream/:id',
      name: 'stream-view',
      component: StreamView
    },
    // catch all redirect
    {
      path: '*', redirect: '/admin'
    }
  ]
})
