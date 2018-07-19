<template>
  <div id="userProfile">
    <header id="signup-header" class="py-2 bg-success text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1><icon name="user-plus" scale="2"></icon>Blogen Sign-Up</h1>
          </div>
        </div>
      </div>
    </header>
    <div class="container">
      <div class="row">

        <div class="col-md-6 mx-auto my-4">
          <b-card no-body border-variant="success">
            <b-card-header class="text-center">
              <h4>User Info</h4>
            </b-card-header>
            <b-card-body>
              <b-alert variant="danger" :show="formError">
                Invalid username or password
              </b-alert>

              <b-form @submit="doSignUp">
                <b-form-group id="firstNameGroup2" label="First Name" label-for="firstName2">
                  <b-form-input id="firstName2" type="text" v-model="user.firstName" required placeholder="First Name">
                  </b-form-input>
                </b-form-group>

                <b-form-group id="lastNameGroup2" label="Last Name" label-for="lastName2">
                  <b-form-input id="lastName2" type="text" v-model="user.lastName" required placeholder="Last Name">
                  </b-form-input>
                </b-form-group>

                <b-form-group id="emailGroup2" label="Email" label-for="emailName2">
                  <b-form-input id="emailName2" type="email" v-model="user.email" required placeholder="Email">
                  </b-form-input>
                </b-form-group>

                <b-form-group id="userNameGroup2" label="User Name" label-for="userName2">
                  <b-form-input id="userName2" type="text" v-model="user.userName" required placeholder="username">
                  </b-form-input>
                </b-form-group>

                <b-form-group id="passwordGroup2" label="Password" label-for="password2">
                  <b-form-input id="password2" type="password" v-model="user.password" required placeholder="password">
                  </b-form-input>
                </b-form-group>

                <b-button :to="{ name: 'home'}" type="submit" variant="secondary">Cancel</b-button>
                <b-button  type="submit" variant="success">Submit</b-button>

              </b-form>
            </b-card-body>
          </b-card>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import axios from '../axios-auth'
  import { logAxiosError } from '../common'

  export default {
    name: 'UserProfile',
    data () {
      return {
        user: {
          firstName: '',
          lastName: '',
          email: '',
          userName: '',
          password: '',
          avatarImage: ''
        },
        formError: false
      }
    },
    methods: {
      doSignUp (event) {
        event.preventDefault()
        console.log('USER:', this.user)
        axios.post('/auth/signup', this.user)
          .then(res => {
            console.log('/sign-up OK user:', res.data)
            this.$store.commit('SET_USER', res.data)
            this.$router.push({
              name: 'login',
              params: {
                message: 'Sign-Up successful. Login with your username & password'
              }
            })
          })
          .catch(error => {
            logAxiosError(error)
          })
      }
    }
  }
</script>

<style scoped>

</style>
