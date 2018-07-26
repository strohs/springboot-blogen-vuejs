import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/home/Home'
import Login from '../components/Login'
import UserProfile from '../components/UserProfile'
import Posts from '../components/posts/Posts'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/login', name: 'login', component: Login, props: true },
    { path: '/signup', name: 'userProfile', component: UserProfile },
    { path: '/posts', name: 'posts', component: Posts },
    // catch all route that handles any URL not explicitly handled above it
    { path: '*', redirect: '/' }
  ],
  mode: 'history'
})
