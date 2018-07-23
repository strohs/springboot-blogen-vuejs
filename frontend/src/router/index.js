import Vue from 'vue'
import Router from 'vue-router'
import Home from '@/components/Home'
import Login from '@/components/Login'
import LoginHelp from '@/components/LoginHelp'
import UserProfile from '@/components/UserProfile'
import Posts from '@/components/Posts'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/login', name: 'login', component: Login, props: true },
    { path: '/login/help', name: 'loginHelp', component: LoginHelp },
    { path: '/signup', name: 'userProfile', component: UserProfile },
    { path: '/posts', name: 'posts', component: Posts }
  ],
  mode: 'history'
})
