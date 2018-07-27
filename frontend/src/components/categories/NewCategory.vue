// Handles the new post button and new post modal dialog
<template>
  <div>

    <b-button block variant="success" v-b-modal.newCategoryModal>
      <icon class="mx-2" name="plus"></icon>
      New Category
    </b-button>

    <b-modal id="newCategoryModal" ref="newCategoryModalRef"
             title="New Category"
             :hide-footer="true"
             header-bg-variant="success"
             header-text-variant="white">

      <app-category-form v-bind="category" @cancel="hideModal" @submit="submitCategory"></app-category-form>

    </b-modal>
  </div>

</template>

<script>
  import CategoryForm from './CategoryForn'

  export default {
    name: 'NewCategory',
    components: {
      appCategoryForm: CategoryForm
    },
    data () {
      return {
        category: {
          id: -1,
          name: ''
        }
      }
    },
    methods: {
      submitCategory (newCategory) {
        // TODO possibly check if category exists
        this.$store.dispatch('createCategory', newCategory)
        this.hideModal()
      },
      showModal () {
        this.$refs.newCategoryModalRef.show()
      },
      hideModal () {
        this.$refs.newCategoryModalRef.hide()
      }
    }
  }
</script>

<style scoped>

</style>
