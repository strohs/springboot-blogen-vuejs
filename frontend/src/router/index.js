import Vue from 'vue'
import Router from 'vue-router'
import Home from '../components/home/Home'
import Login from '../components/login/Login'
import Signup from '../components/signup/Signup'
import UserProfile from '../components/profile/UserProfile'
import Posts from '../components/posts/Posts'
import Categories from '../components/categories/Categories'
import Users from '../components/users/Users'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', name: 'home', component: Home },
    { path: '/login', name: 'login', component: Login, props: true },
    { path: '/users', name: 'users', component: Users, props: true },
    { path: '/userProfile', name: 'userProfile', component: UserProfile },
    { path: '/signup', name: 'signup', component: Signup },
    { path: '/posts', name: 'posts', component: Posts },
    { path: '/categories', name: 'categories', component: Categories },
    // catch all route that handles any URL not explicitly handled above it
    { path: '*', redirect: '/' }
  ],
  mode: 'history'
})
