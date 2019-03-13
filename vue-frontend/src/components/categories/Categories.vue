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
          <app-new-category @submit="createCategory"></app-new-category>
        </div>
      </div>
      <!-- category status message -->
      <div class="row justify-content-center pt-2">
        <app-status-alert v-bind="status" @dismissed="dismissStatusAlert"></app-status-alert>
      </div>

      <div class="row my-4">
        <div class="col">
          <b-card no-body border-variant="success">
            <b-card-header>
              <h2>Categories</h2>
            </b-card-header>
            <b-card-body>
              <table class="table table-bordered table-striped table-hover">
                <thead>
                <tr>
                  <th scope="col">ID</th>
                  <th scope="col">Name</th>
                  <th scope="col"></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="cat in categories" :key="cat.id">
                  <td>{{cat.id}}</td>
                  <td>{{cat.name}}</td>
                  <td>
                    <transition appear name="fade" mode="out-in">
                      <app-edit-category v-bind="cat" @submit="editCategory"></app-edit-category>
                    </transition>
                  </td>
                </tr>
                </tbody>
              </table>
            </b-card-body>

            <b-card-footer>
              <!-- PAGINATION -->
              <b-pagination class="tpage"
                            v-model="tableCurrentPage"
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
import StatusAlert from '../common/StatusAlert'
import EditCategory from './EditCategory'

export default {
  name: 'Categories',
  components: {
    appNewCategory: NewCategory,
    appStatusAlert: StatusAlert,
    appEditCategory: EditCategory
  },
  data () {
    return {
      category: '',
      status: {
        code: 200,
        message: '',
        show: false
      },
      tableCurrentPage: 1,
      // a page of categories to display in the table
      categories: [],
      // pageInfo holds the category 'page details' returned from the API
      pageInfo: {
        pageNumber: 0,
        totalElements: 0,
        totalPages: 0,
        pageSize: 4
      }
    }
  },
  computed: {
  },
  methods: {
    async fetchCategories (pageNum, pageLimit) {
      console.log(`fetch categories with pageNum:${pageNum} and limit:${pageLimit}`)
      await this.$store.dispatch('fetchCategories', { pageNum: pageNum, pageLimit: pageLimit })
        .then(data => {
          this.categories = data.categories
          this.pageInfo = data.pageInfo
        })
    },
    fetchPage (page) {
      // api pages are 0-based, need to subtract 1 from table page
      this.fetchCategories(page - 1, this.pageInfo.pageSize)
    },
    createCategory (newCategory) {
      this.$store.dispatch('createCategory', newCategory)
        .then(ncat => {
          this.status.code = 200
          this.status.message = `Category: ${newCategory.name} created`
          // display the first page of the table
          this.tableCurrentPage = 1
          this.fetchPage(1)
        })
        .catch(error => {
          this.status.code = error.response.status
          // TODO possibly move global error check into VUEX
          this.status.message = error.response.data.globalError[0].message
        })
      this.displayStatusAlert()
    },
    displayStatusAlert () {
      // display the status code from CRUD request
      this.status.show = true
    },
    dismissStatusAlert () {
      this.status.show = false
    },
    editCategory (cat) {
      console.log('edit category id:', cat.id)
      this.$store.dispatch('updateCategory', cat)
        .then(res => {
          const idx = this.categories.findIndex(c => c.id === cat.id)
          this.categories.splice(idx, 1, cat)
        })
        .catch(error => {
          this.status.code = error.response.status
          // TODO possible global error handling
          this.status.message = error.response.data.globalError[0].message
          this.displayStatusAlert()
        })
    }
  },
  created () {
    this.fetchCategories(0, this.pageInfo.pageSize)
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
