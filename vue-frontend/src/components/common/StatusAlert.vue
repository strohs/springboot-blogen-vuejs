// Component for displaying application wide status/info/failure messages in an alert box
<template>

  <b-alert :variant="computeVariant" fade dismissible :show="show" @dismissed="$emit('dismissed')">
    {{ message }}
  </b-alert>

</template>

<script>
// TODO possibly make this an app wide component and move its state into VUEX
export default {
  name: 'StatusAlert',
  props: {
    message: String,
    code: Number,
    show: Boolean(false)
  },
  data () {
    return {
    }
  },
  computed: {
    computeVariant () {
      if (this.code >= 200 && this.code < 300) {
        return 'success'
      } else if (this.code >= 400 && this.code <= 500) {
        return 'danger'
      } else {
        return 'info'
      }
    }
  }
}
</script>

<style scoped>

</style>
