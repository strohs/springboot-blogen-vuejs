// A form component that validates the fields that make up a post and then emits the validated fields back
// to the parent component in a 'post' object
//
<template>
  <b-form>
    <b-form-group id="titleGroup" label="Title" label-for="title"
                  :state="titleValidator.state" :invalid-feedback="titleValidator.invalidFeedback"
                  :valid-feedback="titleValidator.validFeedback">
      <b-form-input v-model="post.title" id="title" type="text" placeholder="post title"
                    :state="titleValidator.state" @change="validateTitle" required></b-form-input>
    </b-form-group>

    <b-form-group id="categoryGroup" label="Category" label-for="category"
                  :state="categoryValidator.state" :invalid-feedback="categoryValidator.invalidFeedback"
                  :valid-feedback="categoryValidator.validFeedback">
      <b-form-select v-model="post.categoryId" :state="categoryValidator.state" @change="validateCategory"
                     :options="categoryOptions"></b-form-select>
    </b-form-group>

    <b-form-group id="imageUrlGroup" label="Image URL" label-for="imageUrl"
                  :state="imageUrlValidator.state" :invalid-feedback="imageUrlValidator.invalidFeedback"
                  :valid-feedback="imageUrlValidator.validFeedback">
      <b-form-input v-model="post.imageUrl" id="imageUrl" type="text"
                    placeholder="image link URL (optional)"
                    :state="imageUrlValidator.state" @change="validateImageUrl"></b-form-input>
    </b-form-group>

    <b-form-group id="textGroup" label="Text" label-for="text"
                  :state="textValidator.state" :invalid-feedback="textValidator.invalidFeedback"
                  :valid-feedback="textValidator.validFeedback">
      <b-form-textarea v-model="post.text" id="text" type="text" placeholder="post text" rows="3"
                       :state="textValidator.state" @input="validateText" required></b-form-textarea>
    </b-form-group>
    <b-button type="button" variant="secondary" @click="$emit('cancelPost')">Cancel</b-button>
    <b-button type="button" variant="primary" @click="$emit('submitPost',post)" :disabled="!allModalFieldsValid">Submit</b-button>
  </b-form>


</template>

<script>
  import textLengthValidator from '../../validators/textLengthValidator'
  import selectValidator from '../../validators/selectValidator'

  export default {
    name: 'PostForm',
    props: {
      title: String,
      text: String,
      imageUrl: String,
      categoryId: null
    },
    data () {
      return {
        // make a copy of the props so we don't mutate them
        post: {
          title: this.title,
          text: this.text,
          imageUrl: this.imageUrl,
          categoryId: this.categoryId
        },
        fieldsValid: false,
        titleValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        textValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        categoryValidator: {state: null, invalidFeedback: '', validFeedback: ''},
        imageUrlValidator: {state: true, invalidFeedback: '', validFeedback: ''}
      }
    },
    methods: {
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
      }
    },
    created () {
      // pre-validate any filled in form fields
      this.validateTitle(this.title)
      this.validateText(this.text)
      this.validateImageUrl(this.imageUrl)
      this.validateCategory(this.categoryId)
    }
  }
</script>

<style scoped>

</style>
