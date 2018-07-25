<template>
    <!-- a Bootstrap card containing a single user post -->
  <b-card no-body>
    <b-card-header><h3>{{ categoryName }}</h3></b-card-header>
    <b-card-img :src="imageUrl" alt="Image" top></b-card-img>
    <b-card-body :title="title">
      <p class="card-text">{{ text }}</p>
      <small>
        Posted by
        <cite title="Source Title">{{ userName }}</cite>
         on <cite title="Source Title">{{ new Date(created).toLocaleString() }}</cite>
      </small>
    </b-card-body>
  </b-card>

</template>

<script>
  export default {
    name: 'PostCard',
    props: [ 'id', 'title', 'userName', 'imageUrl', 'categoryName', 'text', 'created' ]
  }
</script>

<style scoped>

</style>
