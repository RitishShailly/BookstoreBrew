<script setup lang="ts">
import TheCategoryNav from "@/components/TheCategoryNav.vue";
import TheCategoryBookList from "@/components/TheCategoryBookList.vue";
import {useBookStore} from "@/stores/book";
const bookStore = useBookStore();
import { useRoute } from "vue-router";
import {watch} from "vue";
import router from "@/router";
const route = useRoute();


watch(
    () => route.params.name,
    async (newName) => {
      try{
        if(!newName) await router.push('/category/Fiction');
        else await bookStore.fetchBooks(newName as string);
      }
      catch (e) {
        await router.push('/not-found');
      }
    },
    { immediate: true }
);

</script>

<template>
  <div class = "main-content">          
      <div class="category-menu"><the-category-nav></the-category-nav></div>      
      <div class="category-body"><the-category-book-list> </the-category-book-list></div>
  </div>
</template>



<style scoped>
.main-content {
  display: flex;
  flex: 1; /* This will make sure it occupies all available space until the footer */
}
.category-menu {
  width: 30vb;
  height:100vh;
  background-color: rgba(35, 23, 22, 0.1);
  padding: 20px;
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  align-items: center;
}


.category-body {
  width: 80%;
  padding: 20px;
  display: grid;
}

::v-deep .footer {
  position: relative;
}

</style>