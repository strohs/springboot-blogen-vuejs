<template>
  <!-- Main Posts Page -->
  <section>
    <!-- Post Page HEADER -->
    <header id="post-header" class="py-2 bg-primary text-white">
      <div class="container-fluid">
        <div class="row">
          <div class="col-sm-6">
                <span>
                    <h1><icon class="mx-2" name="pencil-alt" scale="1.5"></icon>Posts (<small>{{selectedCategory.name}}</small>)</h1>
                </span>
          </div>
        </div>
      </div>
    </header>

    <!-- FILTER POSTS / SEARCH POSTS -->
    <section id="search-posts" class="py-4 mb-4 bg-light">
      <div class="container">
        <div class="row">
          <!-- POST Category Filter Dropdown -->
          <div class="col-md-4">
            <app-category-filter-button v-model="selectedCategory"></app-category-filter-button>
          </div>
          <!-- NEW Post Button -->
          <div class="col-md-4">
            <b-button block variant="primary" @click="showNewPostModal">
              <icon class="mx-2" name="plus"></icon>
              New Post
            </b-button>
          </div>
          <!-- SEARCH Post input -->
          <div class="col-md-4">
            <app-post-search v-model="postSearchStr"></app-post-search>
          </div>
        </div>
      </div>
    </section>

    <!-- List of Posts and child posts -->
    <section id="posts" class="py-2">
      <div class="container-fluid">
        <div v-for="post in posts" :key="post.id">
          <div class="row my-2">
            <div class="col">
              <transition appear name="fade" mode="out-in">
                <app-post-media v-bind="post" @replyPost="replyToPost"></app-post-media>
              </transition>


              <div class="row my-2" v-for="child in post.children" :key="child.id">
                <div class="col-md-6 offset-md-2">
                  <transition appear name="fade" mode="out-in">
                    <app-post-media v-bind="child" @replyPost="replyToPost"></app-post-media>
                  </transition>
                </div>
              </div>

            </div>
          </div>

        </div>

      </div>
    </section>

    <!-- PAGINATION -->
    <b-pagination class="ml-4 mt-5"
                  v-model="currentPageNum"
                  :total-rows="pageInfo.totalElements"
                  :per-page="pageInfo.pageSize"
                  @input="fetchPage"
    >
    </b-pagination>

    <!-- Modal for CRUD ops on a post -->
    <b-modal id="postModal" ref="postModalRef"
             :title="postModal.title"
             ok-title="Submit" :ok-disabled="!allModalFieldsValid" @ok="submitPostModal"
             :header-bg-variant="postModal.headerBgVariant"
             :header-text-variant="postModal.headerTextVariant">
      <b-form>
        <b-form-group id="titleGroup" label="Title" label-for="title"
                      :state="titleValidator.state" :invalid-feedback="titleValidator.invalidFeedback"
                      :valid-feedback="titleValidator.validFeedback">
          <b-form-input id="title" type="text" v-model.trim="postModal.post.title" placeholder="post title"
                        :state="titleValidator.state" @change="validateTitle" required></b-form-input>
        </b-form-group>

        <b-form-group id="categoryGroup" label="Category" label-for="category"
                      :state="categoryValidator.state" :invalid-feedback="categoryValidator.invalidFeedback"
                      :valid-feedback="categoryValidator.validFeedback">
          <b-form-select v-model="postModal.post.categoryId" :state="categoryValidator.state" @change="validateCategory"
                         :options="categoryOptions"></b-form-select>
        </b-form-group>

        <b-form-group id="imageUrlGroup" label="Image URL" label-for="imageUrl"
                      :state="imageUrlValidator.state" :invalid-feedback="imageUrlValidator.invalidFeedback"
                      :valid-feedback="imageUrlValidator.validFeedback">
          <b-form-input id="imageUrl" type="text" v-model="postModal.post.imageUrl"
                        placeholder="image link URL (optional)"
                        :state="imageUrlValidator.state" @change="validateImageUrl"></b-form-input>
        </b-form-group>

        <b-form-group id="textGroup" label="Text" label-for="text"
                      :state="textValidator.state" :invalid-feedback="textValidator.invalidFeedback"
                      :valid-feedback="textValidator.validFeedback">
          <b-form-textarea id="text" type="text" v-model="postModal.post.text" placeholder="post text" rows="3"
                           :state="textValidator.state" @input="validateText" required></b-form-textarea>
        </b-form-group>
      </b-form>

    </b-modal>

  </section>
</template>

<script>
  import axios from '../../axios-auth'
  import {logAxiosError} from '../../common'
  import CategoryFilterButton from './CategoryFilterButton'
  import PostSearch from './PostSearch'
  import PostMedia from './PostMedia'
  import textLengthValidator from '../../validators/textLengthValidator'
  import selectValidator from '../../validators/selectValidator'

  export default {
    name: 'Posts',
    components: {
      appCategoryFilterButton: CategoryFilterButton,
      appPostSearch: PostSearch,
      appPostMedia: PostMedia
    },
    data () {
      return {
        selectedCategory: { id: -1, name: 'All' },
        postSearchStr: '',
        currentPageNum: 0,
        pageLimit: 5,
        // array of posts to display in this component
        posts: [{
          id: 0,
          text: '',
          title: '',
          created: '',
          imageUrl: '',
          user: {},
          category: {},
          postUrl: '',
          parentPostUrl: null,
          children: []
        }],
        pageInfo: {          // pageInfo is returned by the API on each page request
          totalElements: 0,   // total posts available using the current filter criteria
          totalPages: 0,      // total pages available using the current filter criteria
          pageNumber: 0,      // the page number of data that was requested (0 based indices)
          pageSize: 0         // the maximum number of parent posts that will be displayed on a page
        },
        // data that is used by our modal
        postModal: {
          title: 'Modal Title',
          headerBgVariant: 'primary',
          headerTextVariant: 'white',
          post: {id: 0, title: '', text: '', imageUrl: '', categoryId: null},
          fieldsValid: false
        },
        titleValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        textValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        categoryValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        imageUrlValidator: {state: true, invalidFeedback: '', validFeedback: ''}
      }
    },
    methods: {
      fetchPosts (pageNum, pageLimit = 5, categoryId = -1) {
        axios.get('/api/v1/posts', {
          params: {
            page: pageNum,
            limit: pageLimit,
            category: categoryId
          }
        })
          .then(res => {
            console.log('fetchPosts response:', res.data)
            this.posts = res.data.posts
            this.pageInfo = res.data.pageInfo
          })
          .catch(error => {
            // TODO check for 401 errors
            logAxiosError(error)
          })
      },
      fetchPage (pageNum) {
        // API page numbers are 0-based, so subtract one from pageNum
        this.fetchPosts(pageNum - 1, this.pageLimit, this.selectedCategory.id)
      },
      searchPosts (searchStr, pageLimit = 5) {
        axios.get(`/api/v1/posts/search/${searchStr}`, {
          params: {
            limit: pageLimit
          }
        })
          .then(res => {
            console.log('searchPosts response:', res.data)
            this.posts = res.data.posts
            this.pageInfo = res.data.pageInfo
          })
          .catch(error => {
            logAxiosError(error)
          })
      },
      showNewPostModal () {
        this.postModal.title = 'New Post'
        this.postModal.headerBgVariant = 'primary'
        this.postModal.headerTextVariant = 'white'
        this.postModal.post = {id: 0, title: '', text: '', imageUrl: '', categoryId: null}
        this.$refs.postModalRef.show()
      },
      submitPostModal () {
        // console.log('submitting post:', this.postModal.post)
        axios.post('/api/v1/posts', this.postModal.post)
          .then(res => {
            this.posts.splice(0, 0, res.data)
          })
          .catch(error => {
            // TODO display user friendly message on page top
            logAxiosError(error)
          })
      },
      replyToPost (id) {

      },
      validateTitle (val) {
        this.titleValidator = textLengthValidator(val, 1)
      },
      validateText (val) {
        this.textValidator = textLengthValidator(val, 1)
      },
      validateImageUrl (val) {
        // TODO possibly implement image link validator
        // this.imageUrlValidator = textLengthValidator(val, 0)
      },
      validateCategory (val) {
        this.categoryValidator = selectValidator(val)
      }
    },
    computed: {
      allModalFieldsValid () {
        return (this.titleValidator.state && this.categoryValidator.state && this.imageUrlValidator.state && this.textValidator.state)
      },
      categoryOptions () {
        let options = [{value: null, text: 'Please Select a Category'}]
        this.$store.getters.getCategories.forEach(cat => options.push({ value: cat.id, text: cat.name }))
        return options
      },
      navPageNumber () {
        return this.pageInfo.pageNumber + 1
      }
    },
    watch: {
      selectedCategory (newCategory) {
        // when selected category changes, re-fetch posts with the new category
        this.fetchPosts(0, this.pageLimit, newCategory.id)
      },
      postSearchStr (newSearchStr) {
        if (newSearchStr.length > 0) {
          this.searchPosts(newSearchStr)
        } else {
          this.fetchPosts(this.currentPageNum, this.pageLimit, this.selectedCategory.id)
        }
      }
    },
    created () {
      this.fetchPosts(0)
    }
  }
</script>

<style scoped>
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
