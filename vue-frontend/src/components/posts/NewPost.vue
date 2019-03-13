// Handles the new post button and new post modal dialog
<template>
  <div>

    <b-button block variant="primary" v-b-modal.newPostModal>
      <icon class="mx-2" name="plus"></icon>
      New Post
    </b-button>

    <b-modal id="newPostModal" ref="newPostModalRef"
             title="New Post"
             :hide-footer="true"
             header-bg-variant="primary"
             header-text-variant="white">

      <app-post-form v-bind="post" @cancelPost="hideModal" @submitPost="submitPost"></app-post-form>

    </b-modal>
  </div>

</template>

<script>
import PostForm from './PostForn'
import constants from '../../common/constants.js'

export default {
  name: 'NewPostModal',
  components: {
    appPostForm: PostForm
  },
  props: {
    postId: Number // the post id to perform CRUD ops on
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
      this.$store.dispatch('createPost', { post: postData })
      this.hideModal()
    },
    showModal () {
      this.$refs.newPostModalRef.show()
    },
    hideModal () {
      // console.log('hide modal')
      this.$refs.newPostModalRef.hide()
    }
  }
}
</script>

<style scoped>

</style>
