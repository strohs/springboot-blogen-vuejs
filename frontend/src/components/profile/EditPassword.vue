// Handles the edit password button and edit password modal window
<template>
  <div class="mx-1">

    <b-btn variant="danger" @click="showModal">
      Change Password
    </b-btn>

    <b-modal ref="editPasswordModalRef"
             title="Change Password"
             :hide-footer="true"
             header-bg-variant="info"
             header-text-variant="white">

      <b-form>
        <b-form-group id="password2Group" label="Password" label-for="password"
                      :state="passwordValidator.state" :invalid-feedback="passwordValidator.invalidFeedback"
                      :valid-feedback="passwordValidator.validFeedback">
          <b-form-input v-model="password" id="password" type="text" placeholder="your password"
                        :state="passwordValidator.state" @input="validatePassword" required></b-form-input>
        </b-form-group>

        <b-button type="button" variant="secondary" @click="hideModal">Cancel</b-button>
        <b-button type="button" variant="primary" @click="submit" :disabled="!allFieldsValid">Submit</b-button>
      </b-form>

    </b-modal>
  </div>

</template>

<script>
  import textLengthValidator from '../../validators/textLengthValidator'

  export default {
    name: 'EditPassword',
    data () {
      return {
        password: '',
        fieldsValid: false,
        passwordValidator: {state: null, invalidFeedback: '', validFeedback: ''}
      }
    },
    methods: {
      submit () {
        // submit the form data to parent component
        this.$emit('submit', this.password)
        this.hideModal()
      },
      validatePassword (val) {
        this.passwordValidator = textLengthValidator(val, 8)
      },
      showModal () {
        this.$refs.editPasswordModalRef.show()
      },
      hideModal () {
        this.$refs.editPasswordModalRef.hide()
      }
    },
    computed: {
      allFieldsValid () {
        return (this.passwordValidator.state)
      }
    }
  }
</script>

<style scoped>

</style>
