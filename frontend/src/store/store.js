import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    AUTH_TOKEN: '',
    user: {
      id: 0,
      firstName: '',
      lastName: '',
      userName: '',
      email: '',
      roles: []
    }
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
    }
  }
})
