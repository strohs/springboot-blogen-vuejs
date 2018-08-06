import router from '../router/index'

function handleAxiosError (error) {
  if (error.response) {
    console.log('Error Data:', error.response.data)
    console.log('Error Status', error.response.status)
    console.log('Error Headers', error.response.headers)
    switch (error.response.status) {
      case 401:
        router.push({ name: 'login', params: { logout: true, message: 'Your session has expired please log back in' } })
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

export {handleAxiosError, getGlobalErrors}
