<template>

  <b-navbar toggleable="sm" type="dark" variant="dark">

    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

    <b-navbar-brand to="/">
      <app-blogen-logo :height="30" :width="30"></app-blogen-logo>
      Blogen
    </b-navbar-brand>

    <b-collapse is-nav id="nav_collapse">
      <b-navbar-nav>
        <b-nav-item to="/posts" class="mx-2" active-class v-if="isAuthenticated">Posts</b-nav-item>
        <b-nav-item :to="{ name: 'users', params: { userId: getAuthUserId } }" class="mx-2" active-class v-if="isAuthenticated">User Posts</b-nav-item>
        <b-nav-item to="/categories" class="mx-2" active-class v-if="isAuthenticated && isAdmin">Categories</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-item-dropdown :text="'Welcome ' + getAuthUser.userName" left v-if="isAuthenticated">
          <b-dropdown-item to="/userProfile">
            <font-awesome-icon icon="user-cog"></font-awesome-icon>
            Profile
          </b-dropdown-item>
          <b-dropdown-divider></b-dropdown-divider>
          <b-dropdown-item-button v-b-modal.authTokenModal>
            <font-awesome-icon icon="lock"></font-awesome-icon>
            Authentication Token
          </b-dropdown-item-button>
        </b-nav-item-dropdown>

        <b-nav-item to="/signup" class="mx-2" v-if="!isAuthenticated">
          <font-awesome-icon icon="user-plus"></font-awesome-icon>
          Sign-Up
        </b-nav-item>

        <b-nav-item to="/login" class="mx-2" v-if="!isAuthenticated">
          <font-awesome-icon icon="user-circle"></font-awesome-icon>
          Login
        </b-nav-item>

        <b-nav-item
          @click="logout"
          class="mx-2"
          v-if="isAuthenticated">
          <font-awesome-icon icon="user-times"></font-awesome-icon>
          Logout
        </b-nav-item>

      </b-navbar-nav>
    </b-collapse>

    <!-- Modal Component -->
    <b-modal id="authTokenModal" title="Your Current Authentication Token:">
      <b-form-textarea size="sm" rows="4" plaintext :value="getAuthToken"></b-form-textarea>
    </b-modal>

  </b-navbar>

</template>

<script>
import { mapGetters, mapActions } from 'vuex'
import BlogenLogo from './common/BlogenLogo'

export default {
  name: 'Navbar',
  components: {
    appBlogenLogo: BlogenLogo
  },
  methods: {
    logout () {
      this.$store.dispatch('doLogout')
      this.$router.push({ name: 'login', params: { message: 'you have been logged out' } })
    }
  },
  computed: {
    ...mapGetters([
      'isAuthenticated',
      'isAdmin',
      'getAuthUser',
      'getAuthUserId',
      'getAuthToken'
    ]),
    ...mapActions([
      'doLogout'
    ])
  }
}
</script>

<style scoped>

</style>
