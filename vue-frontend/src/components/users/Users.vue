<template>
  <div>

    <!-- HEADER -->
    <header id="user-posts-header" class="py-2 bg-secondary text-white">
      <div class="container">
        <div class="row">
          <h2>
            <b-img thumbnail fluid width="50" alt="avatar image" :src="avatarUrl"></b-img>
            Latest Posts from
            <small>{{ user.userName }}</small>
          </h2>
        </div>
      </div>
    </header>

    <!-- Status Alert -->
    <div class="container">
      <div class="row my-4 justify-content-center">
        <app-status-alert v-bind="status"></app-status-alert>
      </div>
    </div>

    <div class="container">
      <b-card-group columns class="mt-4">
        <b-card overlay v-for="post in posts" :key="post.id"
                :img-src="post.imageUrl" img-alt="Card Image" text-variant="black"
                :title="post.title" :sub-title="post.category.name"
        >
          <p class="card-text">{{post.text}}</p>
        </b-card>
      </b-card-group>
    </div>
  </div>
</template>

<script>
import StatusAlert from '../common/StatusAlert'
import { mapState } from 'vuex'

export default {
  name: 'Users',
  props: {
    userId: Number
  },
  components: {
    appStatusAlert: StatusAlert
  },
  data () {
    return {
      status: {
        code: 200,
        message: '',
        show: false
      },
      user: {
        id: -1,
        userName: '',
        avatarImage: ''
      }
    }
  },
  computed: {
    ...mapState([
      'posts',
      'pageInfo'
    ]),
    avatarUrl () {
      return process.env.VUE_APP_DEFAULT_AVATAR_URL + '/' + this.user.avatarImage
    }
  },
  methods: {
    fetchPostsByUser (id, pageNum, pageLimit, categoryId) {
      this.$store.dispatch('fetchPostsByUser', { id, pageNum, pageLimit, categoryId })
        .then(data => {
          if (data.posts === undefined || data.posts.length === 0) {
            console.log('no posts made for user id:', id)
            this.status.code = 301
            this.status.message = 'This user has not made any recent posts'
            this.displayStatusAlert()
          }
        })
        .catch(apiError => {
          // TODO some other error occurred, may need global alert component
          this.status.code = apiError.code
          this.status.message = apiError.message
          this.displayStatusAlert()
        })
    },
    async fetchUserInfo (userId) {
      await this.$store.dispatch('fetchUserInfo', userId)
        .then(data => {
          this.user = data
        })
        .catch(apiError => {
          this.status.code = apiError.code
          this.status.message = apiError.message
          this.displayStatusAlert()
        })
    },
    displayStatusAlert () {
      // display the status code from CRUD request
      this.status.show = true
    },
    dismissStatusAlert () {
      this.status.show = false
    }
  },
  created () {
    this.fetchPostsByUser(this.userId, 0, 9, -1)
    this.fetchUserInfo(this.userId)
  }
}
</script>

<style scoped>

</style>
