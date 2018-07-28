// form for creating new categories
<template>
  <div>
    <header id="categoryHeader" class="py-2 bg-success text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1>
              <icon class="mx-2" name="folder" scale="2"></icon>
              Categories
            </h1>
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
          <b-table striped hover caption-top :fields="fields"
                   :items="tableDataProvider"
                   :per-page="itemsPerPage" :current-page="tableCurrentPage" :total-rows="pageInfo.totalElements"
          >
            <template slot="table-caption">
              <h2>Category List</h2>
            </template>
          </b-table>
        </div>
      </div>
      <div class="row">
        <!-- PAGINATION -->
        <b-pagination class="ml-4 mt-5"
                      v-model="tableCurrentPage"
                      :total-rows="pageInfo.totalElements"
                      :per-page="itemsPerPage"
        >
        </b-pagination>
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
        tableCurrentPage: 1,
        itemsPerPage: 3,
        // category fields to display in the table
        fields: ['id', 'name'],
        categories: [
          {id: 0, name: '', categoryUrl: ''}
        ],
        // pageInfo holds the category 'page details' returned from the API
        pageInfo: {
          pageNumber: 0,
          totalElements: 0,
          totalPages: 0,
          pageSize: 0
        }
      }
    },
    computed: {
      isStatusMessage () {
        return (this.statusMessage.length > 0)
      }
    },
    methods: {
      async fetchCategories (pageNum, pageLimit) {
        console.log(`fetch categories with pageNum:${pageNum} and limit:${pageLimit}`)
        await this.$store.dispatch('fetchCategories', {pageNum: pageNum, pageLimit: pageLimit})
          .then(data => {
            this.categories = data.categories
            // this.categories.splice(0, this.categories.length)
            // data.categories.forEach(c => this.categories.push(c))
            this.pageInfo = data.pageInfo
          })
      },
      tableDataProvider (ctx, callback) {
        this.$store.dispatch('fetchCategories', {pageNum: ctx.currentPage - 1, pageLimit: ctx.perPage})
          .then(data => {
            this.pageInfo = data.pageInfo
            this.categories = data.categories
            // let categories = data.categories
            callback(this.categories)
          })
          .catch(error => {
            console.log(error)
            callback([])
          })
        return null
      },
      storeCategories () {
        this.$store.commit('SET_CATEGORIES', this.categories)
      }
    }
    // created () {
    //   this.fetchCategories(0)
    // }
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
