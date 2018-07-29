import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/home/Home'
import Login from '../components/login/Login'
import UserProfile from '../components/UserProfile'
import Posts from '../components/posts/Posts'
import Categories from '../components/categories/Categories'
import Users from '../components/users/Users'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/login', name: 'login', component: Login, props: true },
    { path: '/users', name: 'user', component: Users, props: true },
    { path: '/signup', name: 'userProfile', component: UserProfile },
    { path: '/posts', name: 'posts', component: Posts },
    { path: '/categories', name: 'categories', component: Categories },
    // catch all route that handles any URL not explicitly handled above it
    { path: '*', redirect: '/' }
  ],
  mode: 'history'
})
