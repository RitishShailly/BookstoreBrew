<script setup lang="ts">

import CategoryBookListItem from "@/components/CategoryBookListItem.vue";
import {useBookStore} from "@/stores/book";
const bookStore = useBookStore();

</script>

<style scoped>
ul {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
  gap: 1em;
  padding: 1em;
}

.category-content {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.main-content {
  display: flex;
  flex: 1;
}

.category-body {
  display: flex;
  width: 80%;
  padding: 20px;
}
</style>



<template>
  <ul>
    <div v-for="book in bookStore.bookList" :key="book.bookId">
      <category-book-list-item :book="book"></category-book-list-item>
    </div>
  </ul>
</template>
