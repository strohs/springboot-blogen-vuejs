// functions to validate that something was selected in a form-select input
export default validate

// const alphaNumericRegEx = /[A-Za-z0-9]+/

function validate (value) {
  return {
    state: state(value),
    invalidFeedback: invalidFeedback(value),
    validFeedback: validFeedback(value)
  }
}

function state (value) {
  return value !== null
}

function invalidFeedback (value) {
  if (state(value)) {
    return ''
  } else {
    return 'Please select an option'
  }
}

function validFeedback (value) {
  return 'Looks Good'
}
