import Vue from 'vue'
import Vuex from 'vuex'
import axios from '../axios-auth'
import { handleAxiosError } from '../common/errorHandlers'
import constants from '../common/constants'

Vue.use(Vuex)

// the prefix of the Blogen API we are using
const apiPrefix = process.env.VUE_APP_API_PREFIX

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
      password: '',
      avatarImage: '',
      roles: []
    },
    // categories holds a list of the current blogen categories, they are used across multiple components
    categories: [
      { id: 0, name: '', categoryUrl: '' }
    ],
    // holds one page worth of posts
    posts: [{
      id: 0,
      text: '',
      title: '',
      created: '',
      imageUrl: '',
      user: {},
      category: {},
      postUrl: '',
      parentPostUrl: null,
      children: []
    }],
    pageInfo: { // pageInfo is returned by the API on each page request
      totalElements: 0, // total threads available using the current filter criteria
      totalPages: 0, // total pages available using the current filter criteria
      pageNumber: 0, // the page number of data that was requested (0 based indices)
      pageSize: 0 // shows the number of threads that will be displayed on a page
    },
    // avatar file names used within this application
    avatars: []
  },
  getters: {
    getAuthToken: state => {
      return state.AUTH_TOKEN
    },
    getAuthUserRoles: state => {
      return state.user.roles
    },
    getAuthUser: state => {
      return state.user
    },
    getAuthUserId: state => {
      return state.user.id
    },
    getCategories: state => {
      return state.categories
    },
    getAvatarUrlByFileName: (state) => (fileName) => {
      return constants.DEFAULT_AVATAR_URL + '/' + fileName
    },
    getPostById: (state) => (id) => {
      for (const post of state.posts) {
        if (post.id === id) {
          return post
        } else {
          // search child posts
          const child = post.children.find(c => c.id === id)
          if (child) {
            return child
          }
        }
      }
    },
    getPostIndicesById: (state) => (id) => {
      // find a post by id in this.posts as well as in this.posts.children
      let indices = { pi: -1, ci: -1 }
      for (let pi = 0; pi < state.posts.length; pi++) {
        if (state.posts[pi].id === id) {
          indices.pi = pi
          break
        } else {
          for (let ci = 0; ci < state.posts[pi].children.length; ci++) {
            if (state.posts[pi].children[ci].id === id) {
              indices.pi = pi
              indices.ci = ci
              break
            }
          }
        }
      }
      return indices
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
      state.user = { id: 0, firstName: '', lastName: '', userName: '', email: '', roles: [] }
    },
    'SET_CATEGORIES' (state, categoriesArr) {
      state.categories = categoriesArr
    },
    'ADD_CATEGORY' (state, category) {
      state.categories.push(category)
    },
    'UPDATE_CATEGORY' (state, updatedCategory) {
      // find existing category id and update it with the new category
      const index = state.categories.findIndex(c => c.id === updatedCategory.id)
      state.categories.splice(index, 1, updatedCategory)
    },
    'SET_POSTS' (state, postsArr) {
      state.posts = postsArr
    },
    'PREPEND_POST' (state, post) {
      state.posts.splice(0, 0, post)
    },
    'ADD_CHILD_POST' (state, { pi, newPost }) {
      // add a child post to a parent post, pi is the parent post index
      state.posts[pi].children.splice(0, 0, newPost)
    },
    'UPDATE_POST' (state, { pi, ci, newPost }) {
      // pi is index of parent post to update and ci is index of child post to update
      // console.log(`pi:${pi} ci:${ci} postData`, newPost)
      if (ci >= 0) {
        state.posts[pi].children.splice(ci, 1, newPost)
      } else {
        state.posts.splice(pi, 1, newPost)
      }
    },
    'DELETE_POST' (state, { pi, ci }) {
      console.log(`DELETE_POST pi:${pi} ci:${ci}`)
      if (ci >= 0) {
        // a child index was provided so delete the child post
        state.posts[pi].children.splice(ci, 1)
      } else {
        // a parent index should always be provided, so delete it
        state.posts.splice(pi, 1)
      }
    },
    'SET_PAGE_INFO' (state, pageInfo) {
      state.pageInfo = pageInfo
    },
    'SET_CURRENT_PAGE_NUM' (state, pageNum) {
      state.pageInfo.pageNumber = pageNum
    },
    'SET_AVATARS' (state, avatarsArr) {
      state.avatars = avatarsArr
    }
  },
  actions: {
    doLogout: ({ commit }) => {
      commit('LOGOUT')
      commit('RESET_USER')
    },
    updatePostData: ({ commit, getters }, { id, newPost }) => {
      let updateData = getters.getPostIndicesById(id)
      updateData.newPost = newPost // { ci, pi, newPost }
      commit('UPDATE_POST', updateData)
    },
    fetchAndStoreCategories: ({ commit }, { pageNum = 0, pageLimit = 20 }) => {
      axios.get(`${apiPrefix}/categories`, {
        params: {
          page: pageNum,
          limit: pageLimit
        }
      })
        .then(res => {
          console.log(`fetchAndStoreCategories response:`, res.data)
          commit('SET_CATEGORIES', res.data.categories)
        })
        .catch(error => {
          handleAxiosError(error)
        })
    },
    fetchCategories: ({ commit }, { pageNum = 0, pageLimit = 20 }) => {
      return axios.get(`${apiPrefix}/categories`, {
        params: {
          page: pageNum,
          limit: pageLimit
        }
      })
        .then(res => {
          console.log(`fetchCategories response:`, res.data)
          return res.data
        })
        .catch(error => {
          handleAxiosError(error)
        })
    },
    createCategory: ({ commit }, categoryObj) => {
      console.log('create category with object:', categoryObj)
      return axios.post(`${apiPrefix}/categories`, categoryObj)
        .then(res => {
          console.log('createCategory response:', res.data)
          commit('ADD_CATEGORY', res.data)
          return res.data
        })
        .catch(error => {
          handleAxiosError(error)
          throw error
        })
    },
    updateCategory: ({ commit }, categoryObj) => {
      console.log('update category with object:', categoryObj)
      delete categoryObj.categoryUrl
      return axios.put(`${apiPrefix}/categories/${categoryObj.id}`, categoryObj)
        .then(res => {
          console.log('updateCategory response:', res.data)
          commit('UPDATE_CATEGORY', res.data)
          return res.data
        })
        .catch(error => {
          handleAxiosError(error)
          throw error
        })
    },
    fetchPosts: ({ commit, dispatch }, { pageNum, pageLimit, categoryId }) => {
      axios.get(`${apiPrefix}/posts`, {
        params: {
          page: pageNum,
          limit: pageLimit,
          category: categoryId
        }
      })
        .then(res => {
          console.log('fetchPosts response:', res.data)
          commit('SET_POSTS', res.data.posts)
          commit('SET_PAGE_INFO', res.data.pageInfo)
        })
        .catch(error => {
          handleAxiosError(error)
        })
    },
    createPost: (context, { id, post }) => {
      // if id is passed, then we are creating a child post, if id is undefined then we are creating a parent post
      const url = (id) ? `${apiPrefix}/posts/${id}` : `${apiPrefix}/posts`
      axios.post(url, post)
        .then(res => {
          console.log(`createPost url:${url} response:`, res.data)
          if (!id) {
            context.commit('PREPEND_POST', res.data)
          } else {
            let indices = context.getters.getPostIndicesById(id)
            indices.newPost = res.data
            context.commit('UPDATE_POST', indices)
          }
        })
        .catch(error => {
          handleAxiosError(error)
        })
    },
    updatePost: (context, { id, post }) => {
      // id and post are mandatory parameters
      const url = `${apiPrefix}/posts/${id}`
      axios.put(url, post)
        .then(res => {
          console.log(`updatePost url:${url} response:`, res.data)
          context.dispatch('updatePostData', { id: id, newPost: res.data })
        })
        .catch(error => {
          handleAxiosError(error)
        })
    },
    deletePost: (context, id) => {
      const url = `${apiPrefix}/posts/${id}`
      axios.delete(url)
        .then(res => {
          console.log(`deletePost url:${url} response:`, res.data)
          const indices = context.getters.getPostIndicesById(id)
          context.commit('DELETE_POST', indices)
        })
        .catch(error => {
          handleAxiosError(error)
        })
    },
    fetchPostsByUser: (context, { id, pageNum, pageLimit, categoryId }) => {
      const url = `${apiPrefix}/users/${id}/posts`
      return axios.get(url, {
        params: {
          page: pageNum,
          limit: pageLimit,
          category: categoryId
        }
      })
        .then(res => {
          console.log(`get posts for user:${id} response:`, res)
          context.commit('SET_POSTS', res.data.posts)
          context.commit('SET_PAGE_INFO', res.data.pageInfo)
          return res.data
        })
        .catch(error => {
          handleAxiosError(error)
          throw error
        })
    },
    fetchAvatarFileNames: ({ commit }) => {
      axios.get(`${apiPrefix}/userPrefs/avatars`)
        .then(res => {
          console.log('fetch avatar file name response:', res)
          commit('SET_AVATARS', res.data.avatars)
        })
        .catch(error => {
          handleAxiosError(error)
          throw (error)
        })
    },
    fetchUserInfo: ({ commit }, id) => {
      return axios.get(`${apiPrefix}/users/${id}`)
        .then(res => {
          console.log('fetch user info response:', res)
          return res.data
        })
        .catch(error => {
          handleAxiosError(error)
          throw (error)
        })
    },
    fetchAuthenticatedUserInfo: ({ commit }) => {
      return axios.get(`${apiPrefix}/login/userinfo`)
        .then(res => {
          console.log('authenticated userinfo:', res.data)
          commit('SET_USER', res.data)
        })
        .catch(error => {
          handleAxiosError(error)
          throw (error)
        })
    },
    loginWithUsername: ({ commit, dispatch }, { username, password }) => {
      return axios.post(`/api/v1/login/form`, { username, password })
        .then(res => {
          // blogen access token is passed as Bearer token in Authorization header
          const token = res.headers.authorization.split(' ')[1]
          console.log('received access token:', token)
          commit('SET_AUTH_TOKEN', token)
          // set the JWT as a bearer token for each request made with axios
          axios.defaults.headers.common['Authorization'] = 'Bearer ' + token
          return token
        })
        .then(() => {
          // get authenticated user information and store it in Vuex
          return dispatch('fetchAuthenticatedUserInfo')
        })
        .catch(error => {
          commit('LOGOUT')
          handleAxiosError(error)
          throw (error)
        })
    },
    //
    // login to blogen using the JWT
    loginWithToken: ({ commit, dispatch }, token) => {
      commit('SET_AUTH_TOKEN', token)
      // set the JWT as a bearer token for all requests made with axios
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + token
      return dispatch('fetchAuthenticatedUserInfo')
    }
  }
})
