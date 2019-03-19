// Form for capturing details of a user: firstname, lastname, email, username, password
// Form info is validated and then emitted in an object to the parent component
<template>
  <b-form>
    <b-form-group id="firstNameGroup2" label="First Name" label-for="firstName2"
                  :state="firstNameValidator.state" :invalid-feedback="firstNameValidator.invalidFeedback"
                  :valid-feedback="firstNameValidator.validFeedback">
      <b-form-input id="firstName2" type="text" v-model.trim="user.firstName" placeholder="First Name"
                    :state="firstNameValidator.state" @input="validateFirstName" required></b-form-input>
    </b-form-group>

    <b-form-group id="lastNameGroup2" label="Last Name" label-for="lastName2"
                  :state="lastNameValidator.state" :invalid-feedback="lastNameValidator.invalidFeedback"
                  :valid-feedback="lastNameValidator.validFeedback">
      <b-form-input id="lastName2" type="text" v-model="user.lastName" placeholder="Last Name"
                    :state="lastNameValidator.state" @input="validateLastName" required>
      </b-form-input>
    </b-form-group>

    <b-form-group id="emailGroup2" label="Email" label-for="emailName2"
                  :state="emailValidator.state" :invalid-feedback="emailValidator.invalidFeedback"
                  :valid-feedback="emailValidator.validFeedback">
      <b-form-input id="emailName2" type="email" v-model="user.email" placeholder="Email"
                    :state="emailValidator.state" @input="validateEmail" required></b-form-input>
    </b-form-group>

    <b-form-group id="userNameGroup2" label="User Name" label-for="userName2"
                  :state="userNameValidator.state" :invalid-feedback="userNameValidator.invalidFeedback"
                  :valid-feedback="userNameValidator.validFeedback">
      <b-form-input id="userName2" type="text" v-model="user.userName" placeholder="username"
                    :state="userNameValidator.state" @input="validateUserName" required></b-form-input>
    </b-form-group>

    <app-avatar-select v-model="user.avatarImage"></app-avatar-select>

    <b-button :to="{ name: 'posts'}" type="button" variant="secondary">Cancel</b-button>
    <b-button  :disabled="!allFieldsValid" type="button" variant="primary" @click="doSubmit">Submit</b-button>

  </b-form>
</template>

<script>
import textLengthValidator from '../../validators/textLengthValidator'
import emailValidator from '../../validators/emailValidator'
import AvatarSelect from './AvatarSelect'

export default {
  name: 'UserProfileForm',
  components: {
    appAvatarSelect: AvatarSelect
  },
  props: {
    firstName: String,
    lastName: String,
    userName: String,
    email: String,
    avatarImage: String
  },
  data () {
    return {
      user: {
        firstName: this.firstName,
        lastName: this.lastName,
        email: this.email,
        userName: this.userName,
        avatarImage: this.avatarImage
      },
      formError: false,
      userNameValidator: { state: null, invalidFeedback: '', validFeedback: '' },
      firstNameValidator: { state: null, invalidFeedback: '', validFeedback: '' },
      lastNameValidator: { state: null, invalidFeedback: '', validFeedback: '' },
      emailValidator: { state: null, invalidFeedback: '', validFeedback: '' }
    }
  },
  methods: {
    doSubmit () {
      this.$emit('submit', this.user)
    },
    validateUserName (val) {
      this.userNameValidator = textLengthValidator(val, 2)
    },
    validateFirstName (val) {
      this.firstNameValidator = textLengthValidator(val, 1)
    },
    validateLastName (val) {
      this.lastNameValidator = textLengthValidator(val, 1)
    },
    validateEmail (val) {
      this.emailValidator = emailValidator(val)
    },
    validateAllFields () {
      this.validateUserName(this.user.userName)
      this.validateFirstName(this.user.firstName)
      this.validateLastName(this.user.lastName)
      this.validateEmail(this.user.email)
    }
  },
  computed: {
    allFieldsValid () {
      return (this.userNameValidator.state && this.firstNameValidator.state && this.lastNameValidator.state &&
          this.emailValidator.state)
    }
  },
  created () {
    this.validateAllFields()
  }
}
</script>

<style scoped>

</style>
