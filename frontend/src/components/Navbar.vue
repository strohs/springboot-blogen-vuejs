<template>

  <b-navbar toggleable="sm" type="dark" variant="dark">

    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

    <b-navbar-brand to="/">Blogen</b-navbar-brand>

    <b-collapse is-nav id="nav_collapse">
      <b-navbar-nav>
        <b-nav-item to="/posts" class="mx-2" active-class v-if="isAuthenticated">Posts</b-nav-item>
        <b-nav-item to="/users" class="mx-2" active-class v-if="isAuthenticated">My Posts</b-nav-item>
        <b-nav-item to="/categories" class="mx-2" active-class v-if="isAuthenticated && isAdmin">Categories</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-item-dropdown :text="'Welcome ' + getUser.userName" left v-if="isAuthenticated">
          <b-dropdown-item to="/profile">
            <icon name="user-cog"></icon>
            Profile
          </b-dropdown-item>
        </b-nav-item-dropdown>

        <b-nav-item to="/signup" class="mx-2" v-if="!isAuthenticated">
          <icon name="user-plus"></icon>
          Sign-Up
        </b-nav-item>

        <b-nav-item to="/login" class="mx-2" v-if="!isAuthenticated">
          <icon name="user-circle"></icon>
          Login
        </b-nav-item>

        <b-nav-item :to="{ name: 'login', params: { logout: true } }" class="mx-2" v-if="isAuthenticated">
          <icon name="user-times"></icon>
          Logout
        </b-nav-item>


      </b-navbar-nav>
    </b-collapse>

  </b-navbar>


</template>

<script>
  import { mapGetters } from 'vuex'

  export default {
    name: 'Navbar',
    computed: {
      ...mapGetters([
        'isAuthenticated',
        'isAdmin',
        'getUser'
      ])
    }
  }
</script>

<style scoped>

</style>
