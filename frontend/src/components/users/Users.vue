<template>
  <div>

    <!-- HEADER -->
    <header id="user-posts-header" class="py-2 bg-secondary text-white">
      <div class="container">
        <div class="row">
          <h2>
            <b-img thumbnail fluid width="50" alt="avatar image" :src="avatarUrl"></b-img>
            Latest Posts from
            <small>{{ getUserName }}</small>
          </h2>
        </div>
      </div>
    </header>

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
  import constants from '../../common/constants'
  import StatusAlert from '../common/StatusAlert'
  import {mapState} from 'vuex'

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
        }
      }
    },
    computed: {
      ...mapState([
        'posts',
        'pageInfo',
        'user'
      ]),
      avatarUrl () {
        // first post will always be a parent post containing the user info we want
        return constants.API_SERVER_URL + this.posts[0].user.avatarUrl
      },
      getUserName () {
        if (this.posts[0].user) {
          return this.posts[0].user.userName
        }
      }
    },
    methods: {
      fetchPostsByUser (id, pageNum, pageLimit, categoryId) {
        this.$store.dispatch('fetchPostsByUser', {id, pageNum, pageLimit, categoryId})
          .then(data => {
            console.log('RESPONSE')
          })
          .catch(error => {
            // TODO some other error occurred, may need global alert box
            this.status.code = error.response.status
            this.status.message = error.response.data.globalError[0].message
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
    }
  }
</script>

<style scoped>

</style>
