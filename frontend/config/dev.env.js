var merge = require('webpack-merge')
var prodEnv = require('./prod.env')

module.exports = merge(prodEnv, {
  NODE_ENV: '"development"',
  // strohs: api server url for development
  API_SERVER_URL: '"http://localhost:8088"'
})
