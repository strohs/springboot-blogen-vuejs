<template>
  <div id="userProfile">
    <header id="signup-header" class="py-2 bg-success text-white">
      <div class="container">
        <div class="row">
          <div class="col-md-6">
            <h1><icon name="user-plus" scale="2"></icon>Blogen Sign-Up</h1>
          </div>
        </div>
      </div>
    </header>
    <div class="container">
      <div class="row">

        <div class="col-md-6 mx-auto my-4">
          <b-card no-body border-variant="success">
            <b-card-header class="text-center">
              <h4>User Info</h4>
            </b-card-header>
            <b-card-body>
              <b-alert variant="danger" :show="formError">
                Invalid username or password
              </b-alert>

              <b-form @submit="doSignUp">
                <b-form-group id="firstNameGroup2" label="First Name" label-for="firstName2"
                              :state="firstNameValidator.state" :invalid-feedback="firstNameValidator.invalidFeedback"
                              :valid-feedback="firstNameValidator.validFeedback">
                  <b-form-input id="firstName2" type="text" v-model.trim="user.firstName" placeholder="First Name"
                                :state="firstNameValidator.state" @change="validateFirstName" required></b-form-input>
                </b-form-group>

                <b-form-group id="lastNameGroup2" label="Last Name" label-for="lastName2"
                              :state="lastNameValidator.state" :invalid-feedback="lastNameValidator.invalidFeedback"
                              :valid-feedback="lastNameValidator.validFeedback">
                  <b-form-input id="lastName2" type="text" v-model="user.lastName" placeholder="Last Name"
                                :state="lastNameValidator.state" @change="validateLastName" required>
                  </b-form-input>
                </b-form-group>

                <b-form-group id="emailGroup2" label="Email" label-for="emailName2"
                              :state="emailValidator.state" :invalid-feedback="emailValidator.invalidFeedback"
                              :valid-feedback="emailValidator.validFeedback">
                  <b-form-input id="emailName2" type="email" v-model="user.email" placeholder="Email"
                                :state="emailValidator.state" @change="validateEmail" required></b-form-input>
                </b-form-group>

                <b-form-group id="userNameGroup2" label="User Name" label-for="userName2"
                              :state="userNameValidator.state" :invalid-feedback="userNameValidator.invalidFeedback"
                              :valid-feedback="userNameValidator.validFeedback">
                  <b-form-input id="userName2" type="text" v-model="user.userName" placeholder="username"
                                :state="userNameValidator.state" @change="validateUserName" required></b-form-input>
                </b-form-group>

                <b-form-group id="passwordGroup2" label="Password" label-for="password2"
                              :state="passwordValidator.state" :invalid-feedback="passwordValidator.invalidFeedback"
                              :valid-feedback="passwordValidator.validFeedback">
                  <b-form-input id="password2" type="password" v-model="user.password" placeholder=""
                                :state="passwordValidator.state" @change="validatePassword"required>
                  </b-form-input>
                </b-form-group>

                <b-button :to="{ name: 'home'}" type="submit" variant="secondary">Cancel</b-button>
                <b-button  :disabled="!allFieldsValid" type="submit" variant="success">Submit</b-button>

              </b-form>
            </b-card-body>
          </b-card>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
  import axios from '../axios-auth'
  import textLengthValidator from '../validators/textLengthValidator'
  import emailValidator from '../validators/emailValidator'
  import { logAxiosError } from '../common'

  export default {
    name: 'UserProfile',
    data () {
      return {
        user: {
          firstName: '',
          lastName: '',
          email: '',
          userName: '',
          password: '',
          avatarImage: ''
        },
        formError: false,
        userNameValidator: { state: null, invalidFeedback: '', validFeedback: '' },
        firstNameValidator: { state: null, invalidFeedback: '', validFeedback: '' },
        lastNameValidator: { state: null, invalidFeedback: '', validFeedback: '' },
        passwordValidator: { state: null, invalidFeedback: '', validFeedback: '' },
        emailValidator: { state: null, invalidFeedback: '', validFeedback: '' }
      }
    },
    methods: {
      doSignUp (event) {
        event.preventDefault()
        console.log('USER:', this.user)
        axios.post('/auth/signup', this.user)
          .then(res => {
            console.log('/sign-up OK user:', res.data)
            this.$store.commit('SET_USER', res.data)
            this.$router.push({
              name: 'login',
              params: {
                message: `Welcome ${this.user.firstName}. Login with your username and password`
              }
            })
          })
          .catch(error => {
            logAxiosError(error)
          })
      },
      validateUserName (val) {
        this.userNameValidator = textLengthValidator(val, 4)
      },
      validateFirstName (val) {
        this.firstNameValidator = textLengthValidator(val, 1)
      },
      validateLastName (val) {
        this.lastNameValidator = textLengthValidator(val, 1)
      },
      validatePassword (val) {
        this.passwordValidator = textLengthValidator(val, 8)
      },
      validateEmail (val) {
        this.emailValidator = emailValidator(val)
      }
    },
    computed: {
      allFieldsValid () {
        return (this.userNameValidator.state && this.firstNameValidator.state && this.lastNameValidator.state &&
          this.emailValidator.state && this.passwordValidator.state)
      }
    }
  }
</script>

<style scoped>

</style>
