<template>

      <img :src="blogenLogo" :width="width" :height="height" class="d-inline-block align-top" alt="Blogen Logo">

</template>

<script>
  import constants from '../../common/constants'

  export default {
    name: 'BlogenLogo',
    props: ['height', 'width'],
    computed: {
      blogenLogo () {
        return constants.API_SERVER_URL + '/images' + '/blogen-logo.png'
      }
    }
  }
</script>

<style scoped>

</style>
