// functions to validate the length of a text string
export default validate

// const alphaNumericRegEx = /[A-Za-z0-9]+/

function validate (value, minLength, maxLength = 999) {
  return {
    state: state(value, minLength, maxLength),
    invalidFeedback: invalidFeedback(value, minLength, maxLength),
    validFeedback: validFeedback(value, minLength, maxLength)
  }
}

function state (value, minLength, maxLength) {
  // const match = value.match(alphaNumericRegEx);
  return value.length >= minLength && value.length <= maxLength
}

function invalidFeedback (value, minLength, maxLength) {
  if (state(value, minLength, maxLength)) {
    return ''
  } else if (value.length < minLength) {
    return `Please enter at least ${minLength} characters`
  } else if (value.length > maxLength) {
    return `Please enter no more than ${maxLength} characters`
  } else {
    return 'Please enter something'
  }
}

function validFeedback (value, minLength, maxLength) {
  return 'Looks Good'
}
