// import the configured axios instance
import axios from '../axios-auth'
import ApiError from './ApiError'

// These are all functions that call the Blogen Rest API.
// The API expects to receive a Bearer token (JWT) in the Authorization
// Header. This header will be set by Vuex via the SET_AUTH_TOKEN mutation
// function.
// Any errors received from the REST Api will be mapped into an ApiError
// object and then re-thrown to the caller

/**
 * calls the REST API to fetch a page of categories
 * @param pageNumber{Number} - the page number to fetch
 * @param pageLimit(Number - the maximum number of items to fetch per page
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getCategories (pageNumber = 0, pageLimit = 20) {
  return axios.get('/api/v1/categories', {
    params: {
      page: pageNumber,
      limit: pageLimit
    }
  })
}

/**
 * calls the REST API to log in a blogen user using their username and password.
 * @param username{String}
 * @param password{String}
 * @returns {Promise<String>} the users jwt if the login was successful, else
 * an ApiError is thrown with the shape:
 * { code, message } where code is the error status code from the api, and message
 * is a human-readable description of the message
 */
export async function loginByUsername (username, password) {
  return axios.post('/login/form', { username: username, password: password })
    .then(res => {
      const token = res.headers.authorization.split(' ')[1]
      console.log('received access token:', token)
      return token
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * calls the REST API to retrieve authenticated user information.
 * The user's JWT must be set in the VUEX store.
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getAuthenticatedUserInfo () {
  return axios.get('/api/v1/users/authenticate')
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * calls the REST API to fetch an end user's information using their blogen id
 * @param id
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getUserInfoById (id) {
  return axios.get(`/api/v1/users/${id}`)
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * call the REST API to fetch a list of avatar filenames currently in use by end users.
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getAvatarFileNames () {
  return axios.get('/api/v1/userPrefs/avatars')
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * call the REST API to fetch a page of posts for the specified user id
 * @param id - the user's id
 * @param pageNum - the page number of posts to return
 * @param pageLimit - the number of posts to retrieve per page
 * @param categoryId - the category id of the posts
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getPostsByUserId (id, pageNum, pageLimit, categoryId) {
  return axios.get(`/api/v1/users/${id}/posts`, {
    params: {
      page: pageNum,
      limit: pageLimit,
      category: categoryId
    }
  })
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * call the REST API to delete a post by its primary id
 * @param id - the primary id of the post
 */
export async function deletePostById (id) {
  return axios.delete(`/api/v1/posts/${id}`)
    .then(res => res)
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * update an existing post with data from the passed in post
 * @param id - the id of the post to be updated
 * @param updatedPost - the post containing properties to be updated
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function putPostById (id, updatedPost) {
  return axios.put(`/api/v1/posts/${id}`, updatedPost)
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * create a new post thread (i.e. a "parent post"), or a new "child" post
 * @param id - if id is not null, then we are creating a child post, and the id is the id of the parent post,
 * if id is undefined or null, then we are creating a parent post
 * @param newPost - object containing the properties of the post to be created
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function createPost (id, newPost) {
  const url = (id) ? `/api/v1/posts/${id}` : '/api/v1/posts'
  return axios.post(url, newPost)
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * fetches a page worth of blogen posts for a specific category
 * @param pageNum - the page number to fetch
 * @param pageLimit - the number of posts to fetch per page
 * @param categoryId - the categoryId of the posts, if this is equal to -1, then all posts across all
 * categories will be fetched
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getPostsByCategory (pageNum, pageLimit, categoryId) {
  return axios.get('/api/v1/posts', {
    params: {
      page: pageNum,
      limit: pageLimit,
      category: categoryId
    }
  })
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * update an existing category with data from the updatedCategory object
 * @param id - the id of the category to be updated
 * @param updatedCategory - category object containing the properties to update
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function putCategoryById (id, updatedCategory) {
  return axios.put(`/api/v1/categories/${id}`, updatedCategory)
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * create a new category using the properties of the newCategory
 * @param newCategory - a category object to be created
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function createCategory (newCategory) {
  return axios.post('/api/v1/categories', newCategory)
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

export async function getAllCategoriesByPage (pageNum, pageLimit) {
  return axios.get('/api/v1/categories', {
    params: {
      page: pageNum,
      limit: pageLimit
    }
  })
    .then(res => {
      return res
    })
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * call the REST API to clear a users session with the backend
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function logout () {
  return axios.get('/logout')
    .then(res => res)
    .catch(error => {
      console.error(error)
      throw mapAxiosErrorToApiError(error)
    })
}

/**
 * map a Blogen REST Api error JSON object to an ApiError object:
 *
 * @param error - the API error JSON
 * @returns {ApiError}
 */
function mapAxiosErrorToApiError (error) {
  return new ApiError(error.response.status, error.response.data.globalError[0].message)
}
