<template>
  <!-- LOGIN -->
  <section id="login">
    <div class="container">
      <div class="row my-4">

        <div class="col-md-8 mx-auto">
          <b-card border-variant="primary" no-body>
            <b-card-header>
              <h4><icon name="user-circle" scale="2"></icon> Account Login</h4>
            </b-card-header>
            <b-card-body>
              <b-alert variant="danger" :show="loginError">
                Invalid username or password
              </b-alert>
              <b-alert variant="success" dismissible :show="showStatusMessage" @dismissed="message = ''">
                {{ message }}
              </b-alert>

              <b-form @submit="doLogin">
                <b-form-group id="userNameGroup" label="User Name:" label-for="userName">
                  <b-form-input id="userName" type="text" v-model="login.username" required placeholder="username">
                  </b-form-input>
                </b-form-group>

                <b-form-group id="passwordGroup" label="Password:" label-for="password">
                  <b-form-input id="password" type="password" v-model="login.password" required>
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
    props: {
      message: String,
      logout: Boolean
    },
    data () {
      return {
        login: {
          username: '',
          password: ''
        },
        loginError: false
      }
    },
    methods: {
      doLogin (event) {
        event.preventDefault()
        this.message = ''
        axios.post('/auth/login', this.login)
          .then(res => {
            // if we get an access token, save in in the store and set the authorization header in axios
            if (res.data.accessToken) {
              console.log('access token:', res.data.accessToken)
              console.log('user:', res.data.user)
              this.$store.commit('SET_AUTH_TOKEN', res.data.accessToken)
              this.$store.commit('SET_USER', res.data.user)
              axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.accessToken
              this.$router.push({ name: 'posts' })
            } else {
              console.log('expected an access token but none was sent')
            }
            this.loginError = false
          })
          .catch(error => {
            logAxiosError(error)
            this.loginError = true
          })
      },
      doLogout () {
        console.log('DO LOGOUT')
        this.$store.commit('LOGOUT')
        this.message = 'You have been logged out'
      }
    },
    computed: {
      showStatusMessage () {
        return (this.message != null && this.message.length > 0)
      },
      showErrorMessage () {
        return (this.message != null && this.message.length > 0)
      }
    },
    mounted () {
      this.login.username = this.$store.state.user.userName
      if (this.logout === true) {
        this.doLogout()
      }
    }
  }
</script>

<style scoped>

</style>
