// functions to check if a UserName Exists
import axios from '../axios-auth'

export default validate

function validate (value) {
  return checkUserNameExists(value)
}

function checkUserNameExists (value) {
  return axios.get(`/api/v1/auth/username/${value}`)
    .then(res => {
      // user name exists
      if (res.data === true) {
        console.log(`user name is taken: ${value}`, res.status)
        return {
          state: false,
          invalidFeedback: 'User Name is taken',
          validFeedback: ''
        }
      } else if (res.data === false) {
        return {
          state: true,
          invalidFeedback: '',
          validFeedback: 'User Name is available'
        }
      } else {
        throw new Error('unknown response returned from api: '``, res.data)
      }
    })
    .catch(error => {
      console.error(error)
    })
}
