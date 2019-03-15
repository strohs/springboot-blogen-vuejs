// Component for editing a users profile (i.e. their sign-up information
//
//

<template>
  <div id="userProfile">
    <header id="user-profile-header" class="py-2 bg-info text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1>
              <font-awesome-icon icon="user-cog" class="mr-2" scale="2"></font-awesome-icon>
              User Profile
            </h1>
          </div>
        </div>
      </div>
    </header>

    <div class="container">
      <!-- alert message box -->
      <div class="row my-4 justify-content-center">
        <app-status-alert v-bind="status" @dismissed="dismissStatusAlert"></app-status-alert>
      </div>
      <b-card class="my-4" no-body border-variant="info">
        <b-card-header class="text-left">
          <div class="row">
            <h4>Profile</h4>
            <app-edit-password class="ml-auto" password="" @submit="doChangePassword"></app-edit-password>
          </div>
        </b-card-header>
        <b-card-body>
          <div class="row">

            <div class="col">
              <app-user-profile-form v-bind="user" @submit="doChangeProfile"></app-user-profile-form>
            </div>
            <div class="col">
            </div>
          </div>

        </b-card-body>
      </b-card>
    </div>
  </div>
</template>

<script>
import axios from '../../axios-auth'
import UserProfileForm from './UserProfileForm'
import StatusAlert from '../common/StatusAlert'
import { handleAxiosError } from '../../common/errorHandlers'
import EditPassword from './EditPassword'
// import { mapState } from 'vuex'

export default {
  name: 'UserProfile',
  components: {
    appUserProfileForm: UserProfileForm,
    appStatusAlert: StatusAlert,
    appEditPassword: EditPassword
  },
  data () {
    return {
      user: {
        id: -1,
        firstName: '',
        lastName: '',
        email: '',
        userName: '',
        avatarImage: '',
        password: ''
      },
      status: {
        code: 200,
        message: '',
        show: false
      }
    }
  },
  methods: {
    doChangeProfile (user) {
      console.log('change profile user info:', user)
      axios.put(`/api/v1/users/${this.user.id}`, user)
        .then(res => {
          console.log('user profile change response:', res.data)
          this.$store.commit('SET_USER', res.data)
          this.status.code = 200
          this.status.message = 'Your profile was successfully changed'
          this.displayStatusAlert()
        })
        .catch(error => {
          handleAxiosError(error)
          this.status.code = error.response.status
          this.status.message = error.response.data.globalError[0].message
          this.displayStatusAlert()
        })
    },
    doChangePassword (newPassword) {
      console.log('change password:', newPassword)
      axios.put(`api/v1/users/${this.user.id}/password`, { password: newPassword })
        .then(res => {
          console.log('change password success')
          this.status.code = 200
          this.status.message = 'Your password was successfully changed'
          this.displayStatusAlert()
        })
        .catch(error => {
          handleAxiosError(error)
          this.status.code = error.response.status
          this.status.message = error.response.data.globalError[0].message
          this.displayStatusAlert()
        })
    },
    displayStatusAlert () {
      // display the status code from CRUD request
      this.status.show = true
    },
    dismissStatusAlert () {
      this.status.show = false
    }
  },
  created () {
    this.user = this.$store.getters.getAuthUser
  }
}
</script>

<style scoped>

</style>
