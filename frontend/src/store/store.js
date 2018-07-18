import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    AUTH_TOKEN: ''
  },
  getters: {
    getAuthToken: state => {
      return state.AUTH_TOKEN
    },
    isAuthenticated: state => {
      return state.AUTH_TOKEN.length > 0
    }
  },
  mutations: {
    'SET_AUTH_TOKEN' (state, token) {
      state.AUTH_TOKEN = token
    }
  }
})
