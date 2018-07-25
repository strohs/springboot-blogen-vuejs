export {dateLongFormat}

/**
 * format a Date as a long format date ex: Saturday, September 17, 2016
 * @param date
 * @returns {string}
 */
function dateLongFormat (dateStr) {
  const options = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' }
  return new Date(dateStr).toLocaleDateString('en-US', options)
}
