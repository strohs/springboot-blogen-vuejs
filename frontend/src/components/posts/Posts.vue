<template>
  <!-- Main Posts Page -->
  <section>
    <!-- DASHBOARD HEADER -->
    <header id="main-header" class="py-2 bg-primary text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
                <span>
                    <h1><icon class="mx-2" name="pencil-alt" scale="1.5"></icon>Posts (<small>{{selectedCategory}}</small>)</h1>
                </span>
          </div>
        </div>
      </div>
    </header>

    <!-- FILTER POSTS / SEARCH POSTS -->
    <section id="search-posts" class="py-4 mb-4 bg-light">
      <div class="container">
        <div class="row">
          <!-- POST Category FILTER -->
          <div class="col-md-4">
            <b-dropdown id="filterPostDdown1" text="Filter Posts" variant="success">
              <b-dropdown-item v-for="cat in categories" :key="cat.id" @click="setCategory($event,cat)">{{cat.name}}
              </b-dropdown-item>
            </b-dropdown>
          </div>
          <!-- NEW POST Button -->
          <div class="col-md-4">
            <b-button block variant="primary" @click="showNewPostModal">
              <icon class="mx-2" name="plus"></icon>
              New Post
            </b-button>
          </div>
          <!-- SEARCH -->
          <div class="col-md-4">
            <form id="searchPostsForm" th:action="@{/posts/search}" method="get">
              <div class="form-row">
                <div class="form-inline ml-auto">
                  <input type="text" name="searchStr" class="form-control" placeholder="Search Post Title and Text"/>
                  <input type="submit" class="btn btn-primary" value="Search"/>
                </div>
              </div>
            </form>
          </div>
        </div>

      </div>

    </section>

    <!-- POSTS IN MEDIA OBJECTS STYLE -->
    <section id="posts" class="py-2">
      <div class="container">

        <!-- POST BEGIN HERE -->
        <div class="media mt-3" th:each="parentPost : ${page.posts}">
          <img class="d-flex mr-3" src="img/avatar2.jpg" th:src="@{'/img/'+${parentPost.userAvatar}}">
          <div class="media-body">

            <h5 class="font-weight-bold" th:text="${parentPost.title}">Loving the echo</h5>

            <h6>By
              <small th:text="${parentPost.userName}">rogueTwo</small>
              in
              <small class="font-italic" th:text="${parentPost.categoryName}">Tech Gadgets</small>
              at
              <small class="font-italic" th:text="${parentPost.created}">date</small>
            </h6>

            <span th:text="${parentPost.text}">Got one of these Amazon Echo things for Christmas. I don't think I'll leave the house.</span>
            <div class="my-2">
              <!--EDIT BUTTON - only users who created a post OR admins can edit it -->
              <a th:if="${parentPost.userId == user.id || #authorization.expression('hasAuthority(''ADMIN'')')}"
                 href="#" class="btn btn-outline-primary btn-sm"
                 th:data-edit-post-id="${parentPost.id}" data-edit-post-id="2"
                 th:data-edit-user-name="${parentPost.userName}" data-edit-user-name="johndoe"
                 th:data-edit-title="${parentPost.title}" data-edit-title="Title"
                 th:data-edit-text="${parentPost.text}" data-edit-text="Text"
                 th:data-edit-image-url="${parentPost.imageUrl}" data-edit-image-url="http://blog.com"
                 data-toggle="modal" data-target="#editPostModal">
                Edit
              </a>

              <!--REPLY BUTTON - any user can reply to a parent post -->
              <a href="#" class="btn btn-outline-success btn-sm"
                 th:data-post-id="${parentPost.id}" data-post-id="2"
                 th:data-cat-name="${parentPost.categoryName}" data-cat-name="Business" data-toggle="modal"
                 data-target="#replyPostModal">
                Reply
              </a>

              <!-- DELETE BUTTON only user that created a post can delete it -->
              <a th:if="${parentPost.userId == user.id || #authorization.expression('hasAuthority(''ADMIN'')')}"
                 href="#" th:href="@{'/posts/'+${parentPost.id}+'/delete'}"
                 class="btn btn-outline-danger btn-sm" th:data-val="${parentPost.id}" data-val="2">
                Delete
              </a>
            </div>


            <!--CHILD POSTS start here -->
            <div class="media mt-3" th:each="childPost : ${parentPost.children}">
              <a class="d-flex pr-3" href="#">
                <img src="img/avatar3.jpg" th:src="@{'/img/'+${childPost.userAvatar}}" class="img-thumbnail" width="100"
                     height="100">
              </a>
              <div class="media-body">
                <h5 class="mt-0" th:text="${childPost.title}">Loving the echo....be careful</h5>
                <h6>By
                  <small th:text="${childPost.userName}">rogueTwo</small>
                  in
                  <small class="font-italic" th:text="${childPost.categoryName}">Tech Gadgets</small>
                  at
                  <small class="font-italic" th:text="${childPost.created}">date</small>
                </h6>
                <span
                  th:text="${childPost.text}">Be careful...I don't trust those things. They're always on... :(</span>
                <div class="my-2">
                  <!--EDIT BUTTON - only users who created a post OR admins can edit it -->
                  <a th:if="${childPost.userId == user.id || #authorization.expression('hasAuthority(''ADMIN'')')}"
                     href="#" class="btn btn-outline-primary btn-sm"
                     th:data-edit-post-id="${childPost.id}" data-edit-post-id="2"
                     th:data-edit-user-id="${childPost.userId}" data-edit-user-id="3"
                     th:data-edit-user-name="${childPost.userName}" data-edit-user-name="3"
                     th:data-edit-title="${childPost.title}" data-edit-title="Title"
                     th:data-edit-text="${childPost.text}" data-edit-text="Text"
                     th:data-edit-image-url="${childPost.imageUrl}" data-edit-image-url="http://blog.com"
                     data-toggle="modal" data-target="#editPostModal">
                    Edit
                  </a>

                  <!-- DELETE BUTTON only user that created a post can delete it -->
                  <a th:if="${childPost.userId == user.id || #authorization.expression('hasAuthority(''ADMIN'')')}"
                     href="#" th:href="@{'/posts/'+${childPost.id}+'/delete'}" class="btn btn-outline-danger btn-sm"
                     th:data-val="${parentPost.id}" data-val="2">
                    Delete
                  </a>
                </div>
              </div>
            </div>

            <div class="media mt-3" th:remove="all">
              <a class="d-flex pr-3" href="#">
                <img src="img/avatar4.jpg" class="img-thumbnail" width="100" height="100">
              </a>
              <div class="media-body">
                <h5 class="mt-0">Loving the echo...You're not alone</h5>
                <h6>Tech Gadgets</h6>
                I'm loving it too. I just played a game of chess on it. :)
              </div>
            </div>

          </div>
        </div>

        <div class="media mt-3" th:remove="all">
          <img class="d-flex mr-3 align-self-start img-thumbnail" width="100" height="100" src="img/avatar.png">
          <div class="media-body">
            <h5 class="mt-0">Bootstrap 4</h5>
            <h6>Web Development</h6>
            Finally took the time to dig into this. It's amazing how you can do a lot with so little.
          </div>
        </div>


        <!-- PAGINATION -->
        <nav class="ml-4 mt-5">
          <ul class="pagination">
            <li class="page-item disabled" th:class="${page.requestedPage == 0 ? 'page-item disabled' : 'page-item'}">
              <a href="#" th:href="@{/posts/show(cat=${page.selectedCategoryId},page=${page.requestedPage - 1})}"
                 class="page-link">Previous</a>
            </li>
            <li class="page-item" th:each="i: ${#numbers.sequence(0,page.totalPages - 1)}">
              <a href="#" th:href="@{/posts/show(cat=${page.selectedCategoryId},page=${i})}" class="page-link"
                 th:text="${i + 1}">1</a>
            </li>
            <li class="page-item" th:remove="all">
              <a href="#" class="page-link">2</a>
            </li>
            <li class="page-item" th:remove="all">
              <a href="#" class="page-link">3</a>
            </li>
            <li class="page-item"
                th:class="${page.requestedPage + 1 == page.totalPages ? 'page-item disabled' : 'page-item'}">
              <a href="#" th:href="@{/posts/show(cat=${page.selectedCategoryId},page=${page.requestedPage + 1})}"
                 class="page-link">Next</a>
            </li>
          </ul>
        </nav>
      </div>
    </section>
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
                         :options="postModal.categoryOptions"></b-form-select>
        </b-form-group>

        <b-form-group id="imageUrlGroup" label="Image URL" label-for="imageUrl"
                      :state="imageUrlValidator.state" :invalid-feedback="imageUrlValidator.invalidFeedback"
                      :valid-feedback="imageUrlValidator.validFeedback">
          <b-form-input id="imageUrl" type="text" v-model.trim="postModal.post.imageUrl"
                        placeholder="image link URL (optional)"
                        :state="imageUrlValidator.state" @change="validateImageUrl" required></b-form-input>
        </b-form-group>

        <b-form-group id="textGroup" label="Text" label-for="text"
                      :state="textValidator.state" :invalid-feedback="textValidator.invalidFeedback"
                      :valid-feedback="textValidator.validFeedback">
          <b-form-textarea id="text" type="text" v-model.trim="postModal.post.text" placeholder="post text" rows="3"
                           :state="textValidator.state" @input="validateText" required></b-form-textarea>
        </b-form-group>
      </b-form>

    </b-modal>
  </section>
</template>

<script>
  import axios from '../../axios-auth'
  import {logAxiosError} from '../../common'
  import textLengthValidator from '../../validators/textLengthValidator'
  import selectValidator from '../../validators/selectValidator'

  export default {
    name: 'Posts',
    data () {
      return {
        selectedCategory: 'All',
        categories: [],
        pageNum: 0,
        // array of posts to display in this component
        posts: [],
        pageInfo: {
          totalElements: 0,
          totalPages: 0,
          pageNumber: 0,
          pageSize: 0
        },
        // data that is used by our modal
        postModal: {
          title: 'Modal Title',
          headerBgVariant: 'primary',
          headerTextVariant: 'white',
          post: {id: 0, title: '', text: '', imageUrl: '', categoryId: null},
          categoryOptions: [{value: null, text: 'Please Select a Category'}],
          fieldsValid: false
        },
        titleValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        textValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        categoryValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        imageUrlValidator: {state: null, invalidFeedback: '', validFeedback: ''}
      }
    },
    methods: {
      fetchPosts (pageNum, pageLimit = 5, category = -1) {
        axios.get('/api/v1/posts', {
          params: {
            page: pageNum,
            limit: pageLimit,
            category: category
          }
        })
          .then(res => {
            console.log('fetchPosts response:', res.data)
            this.posts = res.data.posts
            this.pageInfo = res.data.pageInfo
          })
          .catch(error => {
            logAxiosError(error)
          })
      },
      showNewPostModal () {
        this.$refs.postModalRef.show()
      },
      submitPostModal () {
        // TODO
        console.log('SUBMIT CLICKED')
      },
      // set the selected category
      setCategory (event, cat) {
        this.selectedCategory = cat.name
        this.fetchPosts(0, 5, cat.id)
      },
      buildCategories () {
        // manually add the 'All' category to the Category list, this will require us to build our own category list
        //  using categories from the $store
        this.categories.push({id: -1, name: 'All', categoryUrl: ''})
        this.$store.dispatch('fetchCategories')
          .then(() => {
            this.$store.state.categories.forEach(c => {
              this.categories.push(c)
            })
            this.buildCategoryOptions()
          })
      },
      buildCategoryOptions () {
        this.$store.getters.getCategories.forEach(cat => this.postModal.categoryOptions.push({
          value: cat.id,
          text: cat.name
        }))
      },
      validateTitle (val) {
        this.titleValidator = textLengthValidator(val, 1)
      },
      validateText (val) {
        this.textValidator = textLengthValidator(val, 1)
      },
      validateImageUrl (val) {
        this.imageUrlValidator = textLengthValidator(val, 1)
      },
      validateCategory (val) {
        this.categoryValidator = selectValidator(val)
      }
    },
    computed: {
      allModalFieldsValid () {
        return (this.titleValidator.state && this.categoryValidator.state && this.imageUrlValidator.state && this.textValidator.state)
      }
    },
    created () {
      this.fetchPosts(0)
      this.buildCategories()
    }
  }
</script>

<style scoped>

</style>
