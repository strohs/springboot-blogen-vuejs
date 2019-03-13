// A form component that validates the fields that make up a category and then emits the validated fields back
// to the parent component
//
<template>
  <b-form>
    <b-form-group id="categoryNameGroup" label="Category Name" label-for="categoryName"
                  :state="nameValidator.state" :invalid-feedback="nameValidator.invalidFeedback"
                  :valid-feedback="nameValidator.validFeedback">
      <b-form-input v-model="category.name" id="categoryName" type="text" placeholder="category name"
                    :state="nameValidator.state" @change="validateName" required></b-form-input>
    </b-form-group>

    <b-button type="button" variant="secondary" @click="$emit('cancel')">Cancel</b-button>
    <b-button type="button" variant="success" @click="$emit('submit',category)" :disabled="!allModalFieldsValid">Submit</b-button>
  </b-form>

</template>

<script>
import textLengthValidator from '../../validators/textLengthValidator'

export default {
  name: 'CategoryForm',
  props: {
    id: Number,
    name: String
  },
  data () {
    return {
      // make a copy of the props so we don't mutate them
      category: {
        id: this.id,
        name: this.name
      },
      fieldsValid: false,
      nameValidator: { state: null, invalidFeedback: '', validFeedback: '' }
    }
  },
  methods: {
    validateName (val) {
      this.nameValidator = textLengthValidator(val, 3)
    }
  },
  computed: {
    allModalFieldsValid () {
      return (this.nameValidator.state)
    }
  }
}
</script>

<style scoped>

</style>
