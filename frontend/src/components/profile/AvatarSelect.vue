// This Component is a select dropdown with associated image element that lets a user select an
//  avatar image. It expects the parent component to use v-model with this component to capture the selected
//  avatar image name
<template>

  <div class="row">
    <div class="col">
      <b-form-group id="avatarGroup" label="Select Your Avatar" label-for="avatarSelect">
        <b-form-select id="avatarSelect"
                       :value="value"
                       v-model="avatarImage"
                       :options="avatars" @input="$emit('input', avatarImage)"></b-form-select>
      </b-form-group>
    </div>

    <div class="col">
      <b-img class="ml-5" thumbnail fluid width="100" :src="avatarUrl" alt="avatar image"></b-img>
    </div>

  </div>

</template>

<script>
  import constants from '../../common/constants'

  export default {
    name: 'AvatarSelect',
    props: ['value'],
    data () {
      return {
        avatars: [],
        avatarImage: this.value
      }
    },
    methods: {
      fetchAvatarFileNames () {
        this.$store.dispatch('fetchAvatarFileNames')
          .then(data => {
            this.avatars = data.avatars
          })
      }
    },
    computed: {
      avatarUrl () {
        return constants.DEFAULT_AVATAR_URL + '/' + this.avatarImage
      }
    },
    created () {
      this.fetchAvatarFileNames()
    }
  }
</script>

<style scoped>

</style>
