import Vue from 'vue'
import Router from 'vue-router'
import SplashPage from '@/components/SplashPage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'SplashPage',
      component: SplashPage
    }
  ],
  mode: 'history'
})
