<template>
  <b-media>
    <img slot="aside" src="..." alt="Media Aside" />

    <h2>Media Body</h2>
    <p>Some text</p>

    <!-- [Optional: add media children here for nesting] -->
  </b-media>
</template>

<script>
  // {
  // id:
  // text:
  // title:
  // created:
  // category.name
  // user.userName
  // user.avatarImage
  //
  // }
  export default {
    name: "PostMedia",
    props: {
      post: {
        type: Object,
        required: true
      }
    }
  }
</script>

<style scoped>

</style>
