// Handles the reply to post button and reply to post modal window
<template>
  <div class="my-2">

    <b-btn size="sm" variant="outline-success" @click="showModal">
      Reply
    </b-btn>

    <b-modal ref="replyPostModalRef"
             title="Reply to Post"
             :hide-footer="true"
             header-bg-variant="primary"
             header-text-variant="white">

      <app-post-form v-bind="post" @cancelPost="hideModal" @submitPost="submitPost"></app-post-form>

    </b-modal>
  </div>

</template>

<script>
  import PostForm from './PostForn'
  import constants from '../../common/constants'

  export default {
    name: 'ReplyPost',
    components: {
      appPostForm: PostForm
    },
    props: {
      postId: Number          // the post id to perform CRUD ops on
    },
    data () {
      return {
        post: {
          title: '',
          text: '',
          imageUrl: constants.API_SERVER_URL + constants.DEFAULT_IMAGE_PATH,
          categoryId: null
        }
      }
    },
    methods: {
      submitPost (postData) {
        this.$store.dispatch('createPost', {id: this.postId, post: postData})
        this.hideModal()
      },
      showModal () {
        this.$refs.replyPostModalRef.show()
      },
      hideModal () {
        // console.log('hide modal')
        this.$refs.replyPostModalRef.hide()
      }
    }
  }
</script>

<style scoped>

</style>
