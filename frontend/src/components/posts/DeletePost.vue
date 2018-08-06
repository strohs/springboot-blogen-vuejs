// Handles the reply to post button and reply to post modal window
<template>
  <div class="mx-1">

    <b-btn size="sm" variant="outline-danger" @click="showModal">
      Delete
    </b-btn>

    <b-modal ref="deletePostModalRef"
             title="Delete Post"
             ok-variant="danger"
             ok-title="Delete"
             header-bg-variant="danger"
             header-text-variant="white" @ok="deletePost">

      <h4>Are you sure?</h4>

    </b-modal>
  </div>

</template>

<script>
  import PostForm from './PostForn'
  import constants from '../../common/constants.js'

  export default {
    name: 'DeletePost',
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
      deletePost () {
        console.log(`delete post with id:${this.postId}`)
        this.$store.dispatch('deletePost', this.postId)
        this.hideModal()
      },
      showModal () {
        this.$refs.deletePostModalRef.show()
      },
      hideModal () {
        // console.log('hide modal')
        this.$refs.deletePostModalRef.hide()
      }
    }
  }
</script>

<style scoped>

</style>
