/**
 * ApiError is used to map the error code and 'globalError' message fields
 * of a blogen REST Api error response into a javascript Error object
 * code - is the HTTP status code returned by the API
 * message - is the error description returned by the API
 */
export default class ApiError extends Error {
  constructor (code, message) {
    super(message)
    this.code = code
    this.name = 'ApiError'
  }
}
