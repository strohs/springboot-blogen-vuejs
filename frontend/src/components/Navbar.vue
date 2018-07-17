<template>

  <b-navbar toggleable="sm" type="dark" variant="dark">

    <b-navbar-toggle target="nav_collapse"></b-navbar-toggle>

    <b-navbar-brand to="/">Blogen</b-navbar-brand>

    <b-collapse is-nav id="nav_collapse">
      <b-navbar-nav>
        <!--TODO only show if authenticated -->
        <b-nav-item to="/posts" active-class>Posts</b-nav-item>
        <!--TODO only if authenticated, show posts authenitcated user made -->
        <b-nav-item to="/users/USERNAME" active-class>My Posts</b-nav-item>
        <!--TODO show categories if authenticated as Admin -->
        <b-nav-item to="/categories">Categories</b-nav-item>
      </b-navbar-nav>

      <b-navbar-nav class="ml-auto">
        <b-nav-item-dropdown :text="userName" right>
          <!--TODO navigate to user profile page -->
          <b-dropdown-item to="/profile">
            <icon name="user-cog"></icon>
            Profile
          </b-dropdown-item>
        </b-nav-item-dropdown>

        <!--TODO don't show if authenticated -->
        <b-nav-item to="/login">
          <icon name="user-circle"></icon>
          Login
        </b-nav-item>
        <!--TODO show if authenticated -->
        <b-nav-item to="/logout">Logout</b-nav-item>
      </b-navbar-nav>
    </b-collapse>

  </b-navbar>


</template>

<script>
  export default {
    name: 'Navbar',
    data () {
      return {
        userName: 'REPLACEME'
      }
    }
  }
</script>

<style scoped>

</style>
