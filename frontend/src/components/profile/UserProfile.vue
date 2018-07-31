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
  import { handleAxiosError } from '../../common'
  // import { mapState } from 'vuex'

  export default {
    name: 'UserProfile',
    components: {
      appUserProfileForm: UserProfileForm
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
        password: '',
        message: ''  // todo status message alert box
      }
    },
    computed: {
      avatarUrl () {
        return constants.DEFAULT_AVATAR_URL + '/' + this.user.avatarImage
      }
    },
    methods: {
      doChangeProfile (user) {
        console.log('change profile user info:', user)
        axios.put('/api/v1/users', user)
          .then(res => {
            console.log('sign-up OK with response:', res.data)
            if (this.userNameChanged(user)) {
              // todo test this
              this.$store.dispatch('doLogout')
              this.$router.push({
                name: 'login',
                params: {
                  message: `Please log back in with your new username and password`
                }
              })
            } else {
              this.$store.commit('SET_USER', res.data)
              this.message = 'Your profile has been changed'
            }
          })
          .catch(error => {
            // TODO possibly set app wide status message if signup failed
            handleAxiosError(error)
          })
      },
      fetchAvatarFileNames () {
        this.$store.dispatch('fetchAvatarFileNames')
          .then(data => {
            this.avatars = data.avatars
          })
      },
      userNameChanged (newUser) {
        return (this.user.userName !== newUser.userName)
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
