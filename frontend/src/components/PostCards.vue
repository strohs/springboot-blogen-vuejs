<template>
  <!-- Main Splash Page recent posts -->
  <b-card-group deck class="my-4">
    <app-post-card v-for="post in posts" :key="post.id" v-bind="post"></app-post-card>
  </b-card-group>

</template>

<script>
  import PostCard from './PostCard'

  export default {
    name: 'PostCards',
    data () {
      return {
        posts: [
          { id: 1,
            category: 'Category1',
            title: 'Title 1',
            userName: 'JoeBlow11',
            image: 'https://picsum.photos/300/200/?image=1070',
            text: 'sample text 1 for the first post'
          },
          { id: 2, category: 'Category2', title: 'Title 2', userName: 'JoeBlow22', image: 'https://picsum.photos/300/200/?image=1070', text: 'sample text 2 for the first post' },
          { id: 3, category: 'Category4', title: 'Title 4', userName: 'JoeBlow44', image: 'https://picsum.photos/300/200/?image=1070', text: 'sample text 3 for the first post' }
        ]
      }
    },
    components: {
      appPostCard: PostCard
    }
  }
</script>

<style scoped>

</style>
