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

      <app-reply-post v-if="isParentPost" :postId="id" @submitPost="replyToPost">
      </app-reply-post>

      <!--<b-btn id="editButton" size="sm" variant="outline-primary"-->
             <!--v-if="canEditOrDeletePost" @click="$emit('editPost',id)">-->
        <!--Edit-->
      <!--</b-btn>-->
      <!--<b-btn id="deleteButton" size="sm" variant="outline-danger"-->
             <!--v-if="canEditOrDeletePost" @click="$emit('deletePost',id)">-->
        <!--Delete-->
      <!--</b-btn>-->

    <!-- [Optional: add media children here for nesting] -->
  </b-media>
</template>

<script>
  import constants from '../../common/constants'
  import {dateLongFormat} from '../../filters/dateFormatFilter'
  import ReplyPost from './ReplyPost'

  export default {
    name: 'PostMedia',
    components: {
      appReplyPost: ReplyPost
    },
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
    methods: {
      replyToPost (postData) {
        console.log(`reply to post id:${this.id} with data:${postData}`)
        this.$store.dispatch('createPost', {id: this.id, post: postData})
      }
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
