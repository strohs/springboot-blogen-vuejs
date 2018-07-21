// functions to validate email
export default validate

function validate (value) {
  return {
    state: state(value),
    invalidFeedback: invalidFeedback(value),
    validFeedback: validFeedback(value)
  }
}

function state (value) {
  return validateEmail(value)
}

function invalidFeedback (value) {
  if (validateEmail(value)) {
    return ''
  } else {
    return 'Please enter a valid email address'
  }
}

function validFeedback (value) {
  return 'Looks Good'
}

function validateEmail (email) {
  let re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return re.test(String(email).toLowerCase())
}
