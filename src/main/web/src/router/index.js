import Vue from 'vue'
import Router from 'vue-router'

import CmsAdminView from '@/components/admin/CmsAdminView.vue'
import SettingsAdminView from '@/components/admin/SettingsAdminView.vue'
import StreamDetailView from '@/components/admin/StreamDetailView.vue'
import StreamView from  '@/components/kiosk/StreamView.vue'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/admin/cms',
      name: 'cms-admin-view',
      component: CmsAdminView
    },
    {
      path: '/admin/settings',
      name: 'settings-admin-view',
      component: SettingsAdminView
    },
    {
      path: '/admin/streams/:id',
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
      path: '*', redirect: '/admin/cms'
    }
  ]
})
