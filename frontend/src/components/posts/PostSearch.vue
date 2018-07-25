<template>

  <b-input-group id="postSearchGroup">
    <b-form-input type="text" v-model="searchStr" placeholder="Search Posts"></b-form-input>
    <b-input-group-append>
      <b-btn variant="primary" type="submit" @click="emitSearchStr">Search</b-btn>
    </b-input-group-append>
  </b-input-group>

</template>

<script>
  export default {
    name: 'PostSearch',
    props: ['value'],
    data () {
      return {
        searchStr: ''
      }
    },
    methods: {
      emitSearchStr () {
        console.log('post search val:', this.searchStr)
        this.$emit('input', this.searchStr)
      }
    }
  }
</script>

<style scoped>

</style>
