<template>
  <div>
    <header id="welcome-head-section" class="bg-primary text-white">
      <div class="container">
        <div class="row">
          <div class="col text-center">
            <div>
              <h1 class="display-4">Welcome To Blogen</h1>
              <p class="lead">Share your thoughts on a variety of topics</p>
            </div>
          </div>
        </div>
      </div>
    </header>

    <!-- MAIN PAGE HEADER -->
    <section id="main-header" class="bg-secondary text-white">
      <div class="container">
        <div class="row">
          <div class="col-md text-md-center">
            <h4><i class="fa fa-leaf"></i> Latest Blogens</h4>
          </div>
        </div>
      </div>
    </section>

    <!-- CARD COLUMNS -->
    <section id="cards">
      <app-post-cards></app-post-cards>
    </section>
  </div>
</template>

<script>
  import PostCards from './PostCards'

  export default {
    name: 'splashPage',
    data () {
      return {
        msg: ''
      }
    },
    components: {
      appPostCards: PostCards
    }
  }
</script>


<style scoped>

</style>
