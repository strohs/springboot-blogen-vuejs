<template>
  <b-dropdown id="categoryFilterButton" text="Filter By Category" variant="success">
    <b-dropdown-item v-for="cat in categories" :key="cat.id" :value="value" @click="$emit('input', cat)">
      {{cat.name}}
    </b-dropdown-item>
  </b-dropdown>
</template>

<script>
export default {
  name: 'CategoryFilterButton',
  props: ['value'],
  computed: {
    categories () {
      let cats = [{ id: -1, name: 'All', categoryUrl: '' }]
      this.$store.state.categories.forEach(cat => cats.push(cat))
      return cats
    }
  }
}
</script>

<style scoped>

</style>
