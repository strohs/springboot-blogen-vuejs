import router from '../router/index'

function handleAxiosError (error) {
  if (error.response) {
    // console.error('Error Data:', error.response.data)
    // console.error('Error Status', error.response.status)
    // console.error('Error Headers', error.response.headers)
    switch (error.response.status) {
      case 401:
        // this.$store.dispatch('doLogout')
        router.push({ name: 'login', params: { message: 'Your credentials are invalid/expired please log back in' } })
        break
      case 403:
        router.push({ name: 'login', params: { message: 'That resource is forbidden, please log back in' } })
        break
      case 500:
        router.push({ name: 'login', params: { message: 'all servers are busy, please try again later' } })
        break
    }
  } else if (error.request) {
    console.error('Request made but no response received. request=', error.request)
  } else {
    console.error('Some other error occurred:', error.message)
  }
}

const getGlobalErrors = (errorObj) => {
  return errorObj
}

export { handleAxiosError, getGlobalErrors }
