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
    },
    computed: {
      avatarUrl () {
        return this.$store.getters.getAvatarUrlByFileName(this.avatarImage)
      }
    },
    created () {
      this.avatars = this.$store.state.avatars
    }
  }
</script>

<style scoped>

</style>
