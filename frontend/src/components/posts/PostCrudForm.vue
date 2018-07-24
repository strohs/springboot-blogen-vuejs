<template>
  <!-- Form for CRUD ops on a post -->
  <div>
    <b-form-group id="titleGroup" label="Title" label-for="title"
                  :state="titleValidator.state" :invalid-feedback="titleValidator.invalidFeedback"
                  :valid-feedback="titleValidator.validFeedback">
      <b-form-input id="title" type="text" v-model.trim="post.title" placeholder="post title"
                    :state="titleValidator.state" @change="validateTitle" required></b-form-input>
    </b-form-group>

    <b-form-group id="categoryGroup" label="Category" label-for="category">
      <b-form-select v-model="post.categoryId" :options="categoryOptions"></b-form-select>
    </b-form-group>

    <b-form-group id="imageUrlGroup" label="Image URL" label-for="imageUrl"
                  :state="imageUrlValidator.state" :invalid-feedback="imageUrlValidator.invalidFeedback"
                  :valid-feedback="imageUrlValidator.validFeedback">
      <b-form-input id="imageUrl" type="text" v-model.trim="post.imageUrl" placeholder="image link URL (optional)"
                    :state="imageUrlValidator.state" @change="validateImageUrl" required></b-form-input>
    </b-form-group>

    <b-form-group id="textGroup" label="Text" label-for="text"
                  :state="textValidator.state" :invalid-feedback="textValidator.invalidFeedback"
                  :valid-feedback="textValidator.validFeedback">
      <b-form-textarea id="text" type="text" v-model.trim="post.text" placeholder="post text" rows="3"
                       :state="textValidator.state" @change="validateText" required></b-form-textarea>
    </b-form-group>
  </div>


</template>

<script>


  export default {
    name: 'PostCrudForm',
    props: {
      postData: Object
    },
    data () {
      return {
        post: this.postData,
        categoryOptions: [],
        allFieldsValid: false,
        titleValidator: { state: null, invalidFeedback: '', validFeedback: '' },
        textValidator: { state: null, invalidFeedback: '', validFeedback: '' },
        imageUrlValidator: { state: null, invalidFeedback: '', validFeedback: '' }
      }
    },
    methods: {
      buildCategoryOptions () {
        this.$store.getters.getCategories.forEach(cat => this.categoryOptions.push({ value: cat.id, text: cat.name }))
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
      doOk () {
        console.log('OK Button clicked')
      }
    },
    mounted () {
      this.buildCategoryOptions()
    }
  }
</script>

<style scoped>

</style>
