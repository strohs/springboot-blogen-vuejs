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
      console.log(`user name is taken: ${value}`, res.status)
      return {
        state: false,
        invalidFeedback: 'User Name is taken',
        validFeedback: ''
      }
    })
    .catch(error => {
      // user name does not exist
      console.log(`user name does not exist ${value}:`, error.response.status)
      return {
        state: true,
        invalidFeedback: '',
        validFeedback: 'User Name is available'
      }
    })
}
