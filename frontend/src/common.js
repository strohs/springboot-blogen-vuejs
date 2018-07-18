const logAxiosError = error => {
  if (error.response) {
    console.log('Error Data:', error.response.data)
    console.log('Error Status', error.response.status)
    console.log('Error Headers', error.response.headers)
  } else if (error.request) {
    console.log('Request made but not response received->', error.request)
  } else {
    console.log('Some other error:', error.message)
  }
}

export {logAxiosError}
