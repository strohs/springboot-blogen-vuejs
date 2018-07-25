<template>
  <!-- This the component that renders a single post using a Bootstrap Media Object -->

  <b-media>
    <b-img thumbnail fluid slot="aside" width="100" :src="avatarUrl" alt="avatar"></b-img>

    <h5 class="font-weight-bold">{{ title }}</h5>
    <h6>Posted By:
      <small>{{ user.userName }}</small>
      in
      <small class="font-italic">{{ category.name }}</small>
      on
      <small class="font-italic">{{ created | formatDate }}</small>
    </h6>
    <p>{{ text }}</p>
    <!-- CRUD buttons for a post -->
    <div class="my-2">
      <b-button id="replyButton" size="sm" variant="outline-success" v-if="isParentPost" @click="$emit('replyPost',id)">Reply</b-button>
      <b-btn id="editButton" size="sm" variant="outline-primary" v-if="canEditOrDeletePost">Edit</b-btn>
      <b-btn id="deleteButton" size="sm" variant="outline-danger" v-if="canEditOrDeletePost">Delete</b-btn>
    </div>

    <!-- [Optional: add media children here for nesting] -->
  </b-media>
</template>

<script>
  import constants from '../../common/constants'
  import {dateLongFormat} from '../../filters/dateFormatFilter'

  export default {
    name: 'PostMedia',
    props: {
      id: Number,
      title: String,
      text: String,
      created: String,
      imageUrl: String,
      category: Object,
      user: Object,
      parentPostUrl: String
    },
    computed: {
      avatarUrl () {
        return constants.API_SERVER_URL + this.user.avatarUrl
      },
      canEditOrDeletePost () {
        // the logged in user can delete a post of they created the post
        const loggedInUserId = this.$store.state.user.id
        return (loggedInUserId === this.user.id)
      },
      isParentPost () {
        // a post is a parent post if its parentPostUrl is null
        return (this.parentPostUrl === null)
      }
    },
    filters: {
      formatDate (dateStr) {
        return dateLongFormat(dateStr)
      }
    }
  }
</script>

<style scoped>

</style>
