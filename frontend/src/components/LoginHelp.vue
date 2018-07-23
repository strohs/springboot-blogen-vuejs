<template>
  <!-- LOGIN -->

  <section id="login">
    <div class="container">
      <div class="row my-4">

        <div class="col-md-8 mx-auto">
          <transition appear enter-active-class="animated flipInX" leave-active-class="animated flipOutX">
            <b-card no-body>
              <b-card-header header-text-variant="white" header-bg-variant="secondary">
                <div class="row">
                  <h4>
                    <icon class="mx-2" name="user-circle" scale="2"></icon>
                    Blogen Login Help
                  </h4>

                </div>
              </b-card-header>
              <b-card-body>
                <b-table striped hover :items="users"></b-table>
                <b-button :to="{ name: 'login'}" class="ml-auto" variant="primary" size="sm">Back to Login</b-button>
              </b-card-body>
            </b-card>
          </transition>
        </div>

      </div>
    </div>
  </section>
</template>

<script>
  export default {
    name: 'LoginHelp',
    data () {
      return {
        users: [
          {
            user_name: 'mgill',
            pw: 'password'
          },
          {
            user_name: 'scotsman',
            pw: 'password'
          },
          {
            user_name: 'johndoe',
            pw: 'password'
          },
          {
            user_name: 'lizreed',
            pw: 'password'
          },
          {
            user_name: 'admin',
            pw: 'adminpassword'
          }
        ]
      }
    }
  }
</script>

<style scoped>

</style>
