<template>
  <!-- This the component that renders a single post using a Bootstrap Media Object -->

  <b-media>

      <b-img thumbnail fluid slot="aside" width="100" alt="image"
             @mouseover="image = imageUrl" @mouseout="image = avatarUrl" :src="image"></b-img>

    <h5 class="font-weight-bold">{{ title }}</h5>
    <h6>Posted By:
      <small>
        <b-link :to="{ name: 'users', params: { userId: user.id } }">{{user.userName}}</b-link>
      </small>
      in
      <small class="font-italic">{{ category.name }}</small>
      on
      <small class="font-italic">{{ created | formatDate }}</small>
    </h6>
    <p>{{ text }}</p>

    <!-- CRUD buttons for a post -->
    <div class="row">
      <app-reply-post v-if="isParentPost" :postId="id"></app-reply-post>

      <app-edit-post v-if="canEditOrDeletePost" :postId="id"></app-edit-post>

      <app-delete-post v-if="canEditOrDeletePost" :postId="id"></app-delete-post>

    </div>

    <!-- [Optional: add media children here for nesting] -->
  </b-media>
</template>

<script>
  import constants from '../../common/constants.js'
  import {dateLongFormat} from '../../filters/dateFormatFilter'
  import { mapGetters } from 'vuex'
  import ReplyPost from './ReplyPost'
  import EditPost from './EditPost'
  import DeletePost from './DeletePost'

  export default {
    name: 'PostMedia',
    components: {
      appReplyPost: ReplyPost,
      appEditPost: EditPost,
      appDeletePost: DeletePost
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
    data () {
      return {
        // image will hold a url to either the users avatar or the image the user posted
        image: constants.API_SERVER_URL + this.user.avatarUrl
      }
    },
    methods: {
    },
    computed: {
      ...mapGetters([
        'isAdmin',
        'getAuthUserId',
        'getAvatarByFileName'
      ]),
      avatarUrl () {
        return constants.API_SERVER_URL + this.user.avatarUrl
      },
      canEditOrDeletePost () {
        // the logged in user can delete a post of they created the post
        // ADMINS can edit or delete all posts
        return ((this.getAuthUserId === this.user.id) || this.isAdmin)
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
