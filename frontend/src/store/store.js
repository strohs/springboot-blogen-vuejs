import Vue from 'vue'
import Vuex from 'vuex'
import axios from '../axios-auth'
import {logAxiosError} from '../common'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    AUTH_TOKEN: '',
    // user holds details of the currently logged in user
    user: {
      id: 0,
      firstName: '',
      lastName: '',
      userName: '',
      email: '',
      roles: []
    },
    // categories holds a list of the current blogen categories, they are used across multiple components
    categories: [
      {id: 0, name: '', categoryUrl: ''}
    ]
  },
  getters: {
    getAuthToken: state => {
      return state.AUTH_TOKEN
    },
    getUserRoles: state => {
      return state.user.roles
    },
    getUser: state => {
      return state.user
    },
    getCategories: state => {
      return state.categories
    },
    isAuthenticated: state => {
      return state.AUTH_TOKEN.length > 0
    },
    isAdmin: state => {
      return state.user.roles.includes('ADMIN')
    }
  },
  mutations: {
    'SET_AUTH_TOKEN' (state, token) {
      state.AUTH_TOKEN = token
    },
    'SET_USER' (state, userObj) {
      state.user = userObj
    },
    'LOGOUT' (state) {
      // very basic logout that simply clears the AUTH_TOKEN
      if (state.AUTH_TOKEN.length > 0) {
        state.AUTH_TOKEN = ''
      }
    },
    'RESET_USER' (state) {
      const u = { id: 0, firstName: '', lastName: '', userName: '', email: '', roles: [] }
      state.user = u
    },
    'SET_CATEGORIES' (state, categoriesArr) {
      state.categories = categoriesArr
    },
    'ADD_CATEGORY' (state, category) {
      state.categories.push(category)
    }
  },
  actions: {
    saveUserProfile: ({commit}) => {
      // TODO save profile might be done in the component
      // save user profile changes to db
      // axios.put...
      // commit('SET_USER', savedUser );
    },
    doLogout: ({commit}) => {
      commit('LOGOUT')
      commit('RESET_USER')
    },
    fetchCategories: ({commit}) => {
      axios.get('/api/v1/categories')
        .then(res => {
          console.log('fetchCategories response:', res.data)
          commit('SET_CATEGORIES', res.data.categories)
        })
        .catch(error => {
          logAxiosError(error)
        })
    }
  }
})
