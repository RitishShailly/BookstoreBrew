<script setup lang="ts">
import {useCategoryStore} from "@/stores/category";
const categoryStore = useCategoryStore();
</script>

<style scoped>

ul {
  list-style-type: none;
  padding: 1em;
  display: grid;
  flex-wrap: wrap;
  flex-direction: column;
}

li{
  display:flex;
  text-emphasis: none;
  margin-right: 1em;
  flex-wrap: wrap;
}

.category-nav{
  align-content: center;
}

h2{
  padding: 0 0.8em;
}

/* Style for the buttons */
.category-nav a {
    display: block;
    margin: 0.5em 0;
    padding: 1em 1em;
    border: none;
    cursor: pointer;
    background-color: antiquewhite;
    color: black;
    border-radius: 5px;
    text-align: center;
    width: 100%;
    text-decoration: none; 
}

.category-nav a:hover{
    background-color: #eecca2;
}

.category-nav a:active,
.category-nav .router-link-active {
  background-color: #c79860;
  font-weight: bolder;
  color:inherit;
}

</style>

<template>
  <nav class="category-nav">
    <h2 style="color: antiquewhite;">Categories</h2>
    <ul class="category-buttons">
      <li v-for="category in categoryStore.categoryList" :key="category.categoryId">
        <router-link
          :to="'/category/' + category.name"
          class="button category-button"
        >
          {{ category.name }}
        </router-link>
      </li>
    </ul>
  </nav>
</template>
