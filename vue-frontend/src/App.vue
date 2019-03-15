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
    if (this.$route.query.login) {
      console.log('found login')
    }
    console.log('HEADERS:', window.config)
  }
}
</script>

<style>

</style>
