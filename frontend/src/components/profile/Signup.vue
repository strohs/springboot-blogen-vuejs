<template>
  <div id="userProfile">
    <header id="signup-header" class="py-2 bg-info text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1><icon name="user-plus" class="mr-2" scale="2"></icon>Blogen Sign-Up</h1>
          </div>
        </div>
      </div>
    </header>
    <div class="container">
      <div class="row">

        <div class="col-md-6 mx-auto my-4">
          <b-card no-body border-variant="info">
            <b-card-header class="text-center">
              <h4>User Info</h4>
            </b-card-header>
            <b-card-body>


              <app-user-profile-form v-bind="user" @submit="doSignUp"></app-user-profile-form>

            </b-card-body>
          </b-card>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import axios from '../../axios-auth'
  import UserProfileForm from './UserProfileForm'
  import { handleAxiosError } from '../../common'

  export default {
    name: 'UserProfile',
    components: {
      appUserProfileForm: UserProfileForm
    },
    data () {
      return {
        user: {
          firstName: '',
          lastName: '',
          email: '',
          userName: '',
          password: ''
        }
      }
    },
    methods: {
      doSignUp (user) {
        console.log('submitted user info:', user)
        axios.post('/api/v1/auth/signup', user)
          .then(res => {
            console.log('sign-up OK with response:', res.data)
            this.$store.commit('SET_USER', res.data)
            this.$router.push({
              name: 'login',
              params: {
                message: `Welcome to Blogen ${user.firstName}. Login with your username and password`
              }
            })
          })
          .catch(error => {
            // TODO possibly set app wide status message if signup failed
            handleAxiosError(error)
          })
      }
    }
  }
</script>

<style scoped>

</style>
