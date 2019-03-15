import router from '../router/index'

function handleAxiosError (error) {
  console.log('axios error:', error)
  if (error.response) {
    console.log('Error Data:', error.response.data)
    console.log('Error Status', error.response.status)
    console.log('Error Headers', error.response.headers)
    switch (error.response.status) {
      case 401:
        // this.$store.dispatch('doLogout')
        router.push({ name: 'login', params: { message: 'Your credentials are invalid/expired please log back in' } })
        break
    }
  } else if (error.request) {
    console.log('Request made but no response received. request=', error.request)
  } else {
    console.log('Some other error occurred:', error.message)
  }
}

const getGlobalErrors = (errorObj) => {
  return errorObj
}

export { handleAxiosError, getGlobalErrors }
