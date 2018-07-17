<template>
    <!-- a Bootstrap card containing a single user post -->
  <b-card no-body>
    <b-card-header>{{ category }}</b-card-header>
    <b-card-img :src="image" alt="Image" top></b-card-img>
    <b-card-body :title="title">
      <p class="card-text">{{ text }}</p>
      <small>
        Posted by <cite title="Source Title">{{ userName }}</cite>
      </small>
    </b-card-body>
  </b-card>

</template>

<script>
  export default {
    name: 'PostCard',
    props: [ 'id', 'title', 'userName', 'image', 'category', 'text' ]
  }
</script>

<style scoped>

</style>
