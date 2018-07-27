<template>
  <!-- LOGIN -->
  <section id="login">
    <div class="container">
      <div class="row my-4">

        <div class="col-md-8 mx-auto">
          <transition name="flip" mode="out-in">
            <b-card border-variant="primary" no-body v-if="!showHelp" key="login">
              <b-card-header>
                <div class="row">
                  <h4>
                    <icon class="mx-2" name="user-circle" scale="2"></icon>
                    Account Login
                  </h4>
                  <b-link class="ml-auto mx-2" @click="showHelp = !showHelp">
                    <icon name="question-circle" scale="1.5"></icon>
                  </b-link>
                </div>
              </b-card-header>
              <b-card-body>
                <b-alert variant="danger" :show="loginError">
                  Invalid username or password
                </b-alert>
                <b-alert variant="success" dismissible :show="isStatusMessage" @dismissed="statusMessage = ''">
                  {{ statusMessage }}
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
            <!-- Login help -->
            <b-card no-body v-if="showHelp" key="loginHelp">
              <b-card-header header-text-variant="white" header-bg-variant="secondary">
                <div class="row">
                  <h4>
                    <icon class="mx-2" name="user-circle" scale="2"></icon>
                    Blogen Login Help
                  </h4>

                </div>
              </b-card-header>
              <b-card-body>
                <b-table striped hover :items="blogenUsers"></b-table>
                <b-button @click="showHelp = !showHelp" class="ml-auto" variant="primary" size="sm">Back to Login
                </b-button>
              </b-card-body>
            </b-card>
          </transition>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
  import axios from '../../axios-auth'
  import {logAxiosError} from '../../common'
  import {defaultUsers} from '../../common/defaultUsers'

  export default {
    name: 'Login',
    props: {
      logout: Boolean,
      message: ''
    },
    data () {
      return {
        login: {
          username: '',
          password: ''
        },
        blogenUsers: defaultUsers,
        statusMessage: this.message,
        loginError: false,
        showHelp: false
      }
    },
    methods: {
      doLogin (event) {
        event.preventDefault()
        this.statusMessage = ''
        axios.post('/api/v1/auth/login', this.login)
          .then(res => {
            // if we get an access token, save in in the store and set the authorization header in axios
            if (res.data.accessToken) {
              console.log('access token:', res.data.accessToken)
              console.log('user:', res.data.user)
              this.$store.commit('SET_AUTH_TOKEN', res.data.accessToken)
              this.$store.commit('SET_USER', res.data.user)
              axios.defaults.headers.common['Authorization'] = 'Bearer ' + res.data.accessToken
              // get a list of categories and store in vuex
              this.$store.dispatch('fetchCategories')
                .then(() => this.$router.push({name: 'posts'}))
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
        // this.message = 'You have been logged out'
      }
    },
    computed: {
      isStatusMessage () {
        return (this.statusMessage != null && this.statusMessage.length > 0)
      }
    },
    mounted () {
      this.login.username = this.$store.state.user.userName
      console.log('LOGIN PAGE PROPS.message:', this.message)
      if (this.logout === true) {
        this.doLogout()
        this.statusMessage = 'You have been logged out'
      }
    }
  }
</script>

<style scoped>
  .flip-enter-active, .flip-leave-active {
    transition: 0.3s ease-out;
  }

  .flip-enter, .flip-leave-to {
    transform: scaleX(0);
  }
</style>
