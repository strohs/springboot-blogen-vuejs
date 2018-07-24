// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import App from './App'
import router from './router'
import {store} from './store/store.js'
import Icon from 'vue-awesome/components/Icon'
import 'vue-awesome/icons/user-circle'
import 'vue-awesome/icons/user-cog'
import 'vue-awesome/icons/user-times'
import 'vue-awesome/icons/user-plus'
import 'vue-awesome/icons/question-circle'
import 'vue-awesome/icons/pencil-alt'
import 'vue-awesome/icons/keyboard'
import 'vue-awesome/icons/plus'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.component('icon', Icon)

Vue.use(BootstrapVue)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
