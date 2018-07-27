// form for creating new categories
<template>
  <div>
    <header id="categoryHeader" class="py-2 bg-success text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1><icon class="mx-2" name="folder" scale="2"></icon>Categories</h1>
          </div>
        </div>
      </div>
    </header>

    <!-- New Category Button -->
    <div class="container">
      <div class="row mt-4">
        <div class="col-md-4 offset-md-4">
          <app-new-category></app-new-category>
        </div>
      </div>
      <!-- category status message -->
      <div class="row justify-content-center mt-2">
        <b-alert variant="info" dismissible :show="isStatusMessage" @dismissed="statusMessage = ''">
          {{ statusMessage }}
        </b-alert>
      </div>

      <div class="row my-4">
        <div class="col">
          <b-card no-body>
            <b-card-header><h2>Category List</h2></b-card-header>
            <b-card-body>
              <b-table striped hover :items="fetchCategories"></b-table>

            </b-card-body>
            <b-card-footer>
              <!-- PAGINATION -->
              <b-pagination class="ml-4 mt-5"
                            v-model="currentNavPage"
                            :total-rows="pageInfo.totalElements"
                            :per-page="pageInfo.pageSize"
                            @change="fetchPage"
              >
              </b-pagination>
            </b-card-footer>
          </b-card>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
  import NewCategory from './NewCategory'

  export default {
    name: 'Categories',
    components: {
      appNewCategory: NewCategory
    },
    data () {
      return {
        category: '',
        statusMessage: '',
        pageInfo: {
          currentNavPage: 1,
          totalElements: 0,
          pageSize: 0
        }
      }
    },
    computed: {
      isStatusMessage () {
        return (this.statusMessage.length > 0)
      },
      fetchCategories () {
        return this.$store.getters.getCategories
      }
    },
    methods: {
    }
  }
</script>

<style>
  .fade-enter {
    opacity: 0;
  }

  .fade-enter-active {
    transition: opacity 1s;
  }

  .fade-leave {

  }

  .fade-leave-active {
    transition: opacity 1s;
    opacity: 0;
  }

  .slide-enter-active {
    animation: slide-in 500ms ease-out forwards;
  }
  .slide-leave-active {
    animation: slide-out 500ms ease-out forwards;
  }

  @keyframes slide-in {
    from {
      transform: translateX(50px);
      opacity: 0;
    }
    to {
      transform: translateX(0);
      opacity: 1;
    }
  }
  @keyframes slide-out {
    from {
      transform: translateX(0px);
      opacity: 1;
    }
    to {
      transform: translateX(50px);
      opacity: 0;
    }
  }
</style>
