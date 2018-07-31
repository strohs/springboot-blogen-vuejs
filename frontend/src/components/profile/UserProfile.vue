<template>
  <div id="userProfile">
    <header id="user-profile-header" class="py-2 bg-info text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1><icon name="user-cog" class="mr-2" scale="2"></icon>User Profile</h1>
          </div>
        </div>
      </div>
    </header>
    <div class="container">
      <div class="row my-4 justify-content-center">
        <app-status-alert v-bind="status" @dismissed="dismissStatusAlert"></app-status-alert>
      </div>
      <div class="row my-4">

        <div class="col">
          <b-card no-body border-variant="info">
            <b-card-header class="text-center">
              <h4>User Info</h4>
            </b-card-header>
            <b-card-body>
              <div class="row">
                <div class="col-md-6">
                  <app-user-profile-form v-bind="user" @submit="doChangeProfile"></app-user-profile-form>
                </div>

                <div class="col-md-6">
                  <div class="row">
                    <div class="col-md">
                      <b-form-group id="avatarGroup" label="Select Your Avatar" label-for="avatarSelect">
                        <b-form-select id="avatarSelect" v-model="user.avatarImage" :options="avatars"></b-form-select>
                      </b-form-group>
                    </div>
                    <div class="col-md">
                      <b-img thumbnail fluid width="100" :src="avatarUrl" alt="avatar image"></b-img>
                    </div>
                  </div>
                  <div class="row mt-4 justify-content-center">
                    <app-edit-password password="" @submit="doChangePassword"></app-edit-password>
                  </div>
                </div>
              </div>
            </b-card-body>
          </b-card>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import axios from '../../axios-auth'
  import constants from '../../common/constants'
  import UserProfileForm from './UserProfileForm'
  import EditPassword from './EditPassword'
  import StatusAlert from '../common/StatusAlert'
  import { handleAxiosError } from '../../common'
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
        avatars: [],
        status: {
          code: 200,
          message: '',
          show: false
        }
      }
    },
    computed: {
      avatarUrl () {
        return constants.DEFAULT_AVATAR_URL + '/' + this.user.avatarImage
      }
    },
    methods: {
      doChangeProfile (user) {
        // todo move avatar image select into form
        user.avatarImage = this.user.avatarImage
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
        axios.put(`api/v1/users/${this.user.id}/password`, {password: newPassword})
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
      fetchAvatarFileNames () {
        this.$store.dispatch('fetchAvatarFileNames')
          .then(data => {
            this.avatars = data.avatars
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
      this.fetchAvatarFileNames()
    }
  }
</script>

<style scoped>

</style>
