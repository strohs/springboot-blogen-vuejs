<template>
  <!-- LOGIN -->
  <section id="login" class="my-4">
    <div class="container">
      <div class="row">

        <div class="col-md-6 mx-auto">
          <b-card border-variant="primary" no-body>
            <b-card-header>
              <h4><icon name="user-circle" scale="2"></icon> Account Login</h4>
            </b-card-header>
            <b-card-body>
              <b-alert variant="danger" :show="loginError">
                Invalid username or password
              </b-alert>
              <b-alert variant="success" dismissable :show="loggedOut">
                You have been logged out
              </b-alert>

              <b-form @submit="doLogin">
                <b-form-group id="userNameGroup" label="User Name:" label-for="userName">
                  <b-form-input id="userName" type="text" v-model="login.username" required placeholder="username">
                  </b-form-input>
                </b-form-group>

                <b-form-group id="passwordGroup" label="Password:" label-for="password">
                  <b-form-input id="password" type="password" v-model="login.password" required placeholder="password">
                  </b-form-input>
                </b-form-group>

                <b-button block size="lg" type="submit" variant="primary">Login</b-button>

              </b-form>
            </b-card-body>
          </b-card>
        </div>

      </div>
    </div>
  </section>
</template>

<script>
  import axios from '../axios-auth'
  import {logAxiosError} from '../common'

  export default {
    name: 'Login',
    data () {
      return {
        login: {
          username: '',
          password: ''
        },
        loginError: false,
        loggedOut: false  // TODO put this in vuex
      }
    },
    methods: {
      doLogin (event) {
        event.preventDefault()
        axios.post('/auth/login', this.login)
          .then(res => {
            console.log('response data:', res.data)
            // if we get an access token, save in in the store and set the authorization header in axios
            if (res.data.accessToken) {
              console.log('access token:', res.data.accessToken)
              console.log('user:', res.data.user)
              this.$store.commit('SET_AUTH_TOKEN', res.data.accessToken)
              this.$store.commit('SET_USER', res.data.user)
              axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.accessToken
            } else {
              console.log('expected an access token but none was sent')
            }
            this.loginError = false
          })
          .catch(error => {
            logAxiosError(error)
            this.loginError = true
          })
      }
    }
  }
</script>

<style scoped>

</style>
