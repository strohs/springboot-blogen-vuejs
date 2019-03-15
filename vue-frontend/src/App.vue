<template>
  <div id="app">
    <app-navbar></app-navbar>
    <router-view></router-view>
    <app-footer></app-footer>

  </div>
</template>

<script>
import Navbar from './components/Navbar'
import Footer from './components/Footer'

export default {
  name: 'app',
  components: {
    appNavbar: Navbar,
    appFooter: Footer
  },
  mounted () {
    if (this.$route.query.login && this.$cookies.get('token')) {
      // user is logging in after OAuth2 redirect
      console.log('COOKIES:', this.$cookies.get('token'))
      this.$store.dispatch('loginWithToken', this.$cookies.get('token'))
        .then(() => {
          // if login successful, go to posts page
          this.$router.push({ name: 'posts' })
        })
        .catch(err => {
          console.log('in app catch error:', err)
        })
    }
  }
}
</script>

<style>

</style>
