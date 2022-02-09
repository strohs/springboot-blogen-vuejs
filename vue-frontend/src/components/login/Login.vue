<template>
  <!-- LOGIN -->
  <section id="login">
    <div class="container">
      <div class="row my-4">

        <div class="col-md-8 mx-auto">
          <transition name="flip" mode="out-in">
            <b-card border-variant="primary" no-body v-if="!showHelp" key="login">
              <b-card-header>
                <div class="row">
                  <h4>
                    <font-awesome-icon class="mx-2" icon="user-circle" scale="2"></font-awesome-icon>
                    Account Login
                  </h4>
                  <b-link class="ml-auto mx-2" @click="showHelp = !showHelp">
                    <font-awesome-icon icon="question-circle" scale="1.5"></font-awesome-icon>
                  </b-link>
                </div>
              </b-card-header>
              <b-card-body>

                <b-alert variant="info" dismissible :show="isStatusMessage" @dismissed="statusMessage = ''">
                  {{ statusMessage }}
                </b-alert>

                <b-form v-on:submit.prevent="doLogin">
                  <b-form-group id="userNameGroup" label="User Name:" label-for="userName">
                    <b-form-input id="userName" type="text" v-model="login.username" required placeholder="username">
                    </b-form-input>
                  </b-form-group>

                  <b-form-group id="passwordGroup" label="Password:" label-for="password">
                    <b-form-input id="password" type="password" v-model="login.password" required>
                    </b-form-input>
                  </b-form-group>

                  <b-button block size="lg" type="submit" variant="primary">Login with Blogen</b-button>

                </b-form>

                <div class="row my-2 justify-content-center">
                  <div class="col-md-4 mx-2">
                    <b-button variant="danger" :href="googleLogin">Login with Google</b-button>
                  </div>
                  <div class="col-md-4 mx-2">
                    <b-button variant="dark" :href="githubLogin">Login with Github</b-button>
                  </div>
                </div>
              </b-card-body>
            </b-card>
            <!-- Login help -->
            <b-card no-body v-if="showHelp" key="loginHelp">
              <b-card-header header-text-variant="white" header-bg-variant="secondary">
                <div class="row">
                  <h4>
                    <font-awesome-icon class="mx-2" icon="user-circle"></font-awesome-icon>
                    Blogen Login Help
                  </h4>

                </div>
              </b-card-header>
              <b-card-body>
                <b-table striped hover :items="blogenUsers"></b-table>
                <b-button @click="showHelp = !showHelp" class="ml-auto" variant="primary" size="sm">Back to Login
                </b-button>
              </b-card-body>
            </b-card>
          </transition>
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import { defaultUsers } from '../../common/defaultUsers'

export default {
  name: 'Login',
  props: {
    message: String
  },
  data () {
    return {
      login: {
        username: '',
        password: ''
      },
      githubLogin: process.env.VUE_APP_GITHUB_LOGIN_URL,
      googleLogin: process.env.VUE_APP_GOOGLE_LOGIN_URL,
      blogenUsers: defaultUsers,
      statusMessage: this.message,
      loginError: false,
      showHelp: false
    }
  },
  methods: {
    async doLogin () {
      this.statusMessage = ''
      await this.$store.dispatch('loginWithUsername', this.login)
        .then(() => {
          this.$router.push({ name: 'posts' })
        })
        .catch(apiError => {
          this.statusMessage = apiError.message
        })
    }
  },
  computed: {
    isStatusMessage () {
      return (this.statusMessage != null && this.statusMessage.length > 0)
    }
  },
  mounted () {
    this.login.username = this.$store.state.user.userName
  }
}
</script>

<style scoped>
  /*@import './../../assets/css/bootstrap-social.css';*/

  .flip-enter-active, .flip-leave-active {
    transition: 0.3s ease-out;
  }

  .flip-enter, .flip-leave-to {
    transform: scaleX(0);
  }
</style>
