// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import VueCookies from 'vue-cookies'
import BootstrapVue from 'bootstrap-vue'
import App from './App'
import router from './router'
import { store } from './store/store.js'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserCircle, faLock, faUserCog, faUserTimes, faUserPlus, faQuestionCircle, faPencilAlt, faKeyboard, faPlus, faFolder, faFolderPlus } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'

import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

library.add(faUserCircle, faLock, faUserCog, faUserTimes, faUserPlus, faQuestionCircle, faPencilAlt, faKeyboard, faPlus, faFolder, faFolderPlus)

Vue.component('font-awesome-icon', FontAwesomeIcon)

Vue.use(VueCookies)
Vue.use(BootstrapVue)

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
