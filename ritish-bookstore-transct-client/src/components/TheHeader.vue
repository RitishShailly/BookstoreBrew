<script setup lang="ts">
import TheHeaderDropdown from "@/components/TheHeaderDropdown.vue";
import {useCartStore} from "@/stores/cart";
const cartStore = useCartStore();
const ImagePrefix = `${import.meta.env.BASE_URL}`;
</script>

<style scoped>
.navigation-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 10px;
  background-color: var(--background-color);
}

.bookstore-logo img, .text-logo {
  display: inline-block;
  max-height: 100px;
  margin-right: 10px;
  vertical-align: middle;
}

.text-logo {
  font-size: 1.5em;
  margin-top: 0;
  margin-bottom: 0;
}

.search-icon-button img {
  height: 30px;
  width: auto;
  padding-right: 10px;
}

.header-dropdown ul li a {
  padding: 5px 10px;
  text-decoration: none;
  position:relative;
  color: var(--text-color);
  display: block;
}

.header-dropdown:hover ul li a:hover {
  background-color: #ffffff;
}

.button{
  border-radius: 15px;
  padding: 5px 10px;
  border: 1px solid gray;
  transition: background-color 0.3s, color 0.3s;
}

.button:hover {
  background-color: var(--text-color);
  color: var(--background-color);
}

.button.transparent-button {
  border: none;
  cursor: pointer;
}


.shop-books img {
  height: 25px;
  width: auto;
  margin-left: 10px;
}


.title-and-search-bar {
  position: relative;
  text-align: center;
}


.search-bar {
  padding: 5px 10px 5px 10px;
  margin-right: 5px;
  border-radius: 5px;
  border: 1px solid grey;
  width: 30dvw;
}

.search-icon-button {
  background: none;
  border: none;
  cursor: pointer;
  position:absolute;  /* Positioning the icon inside the search bar */
  left: auto;  /* Position from the left side of the search bar */
  right:0px;
  top: 60%;   /* Center it vertically */
  transform: translateY(-50%);  /* Fine-tuning vertical centering */
  z-index: 10;  /* Ensures the icon is above other elements */
}

.search-icon-button img {
  height: 20px;
  width: auto;
  cursor: pointer;
}

.categories-button{
  min-width: 120px;
}

.header-dropdown .categories-button img {
  height: 15px;  /* Adjusted the size of the drop-caret.png */
  width: auto;
  margin-left: 5px;
  transform: translateY(30%);
}

.categories-list {
  display: none;
  text-align: center;
  position: absolute;
  top: 100%;
  left: 0;
  background-color: rgb(199, 152, 96);
  color: #3b2f2f;
  list-style: none;
  padding: 0;
  margin: 0;
  border: 2px solid #c2c1be;
  z-index: 10;
  border-radius: 5% 5% 10% 10%;
  min-width: 120px;
  transform: translateY(-100%);   /* Add this line */
  opacity: 0;                     /* Add this line */
  max-height: 0;                  /* It's already there */
  transition: transform 0.5s ease-in-out, opacity 0.5s ease-in-out, max-height 0.5s ease-in-out;
}

.header-dropdown:hover .categories-list {
  display: block;
  border-radius: 5% 5% 9% 9%;
  transition: transform 1.5s ease, visibility 0.5s ease;
  transform: translateY(0);   /* Add this line */
  opacity: 1;                 /* Add this line */
  max-height: 500px;
}

.header-dropdown.active  .categories-list{
  display: block;
}

.categories-list li:hover {
  background-color: #6b4f4f; /* Darker shade of background */
  color: orangered; /* Darker shade of text */
}

/* Remove underline from the links */
.categories-list li a {
  text-decoration: none;
  color: inherit; /* Inherit color from parent <li> */
  display: block; /* Make the entire list item clickable */
  padding: 5px 10px; /* Add some padding for better appearance */
}

.categories-list li a:hover{
  text-decoration: none;
  color: inherit;
  border-radius: 30%;
}
.categories-list li .router-link-active {
  text-decoration: none;
  color: antiquewhite;
  font-weight: bold;
  border-radius: 30%;
}

.login{
  padding-left: 20px;
  padding-right: 20px;
  margin-left: 10px
}

.header-dropdown {
  position: relative;
  margin-left: 20px; /* Spacing between login and categories button */
}

.header-dropdown-and-cart .cart-icon {
  position: relative;
}


.header-controls {
  display: flex;
  align-items: center;
  gap: 10px; /* Optional: Adds space between the children */
}



.cart-count {
  position: absolute;
  top: 0;
  right: 0;
  background-color: rgba(35, 23, 22, 0.9);
  color: white;
  border-radius: 50%; /* Makes it circular */
  padding: 3px 5px;
  font-size: 1.0em;
  line-height: 1;
  transform: translate(-35%, 15%);
}

.cart-icon img {
  height: 40px;
  width: auto;
}

.cart-icon {
  position: relative;
  cursor: pointer;
}

.cart-count {
  position: absolute;
  top: 0;
  right: 0;
  font-weight:bolder;
}

</style>

<template>
  <header class="navigation-bar">
    <section class="bookstore-logo">
      <router-link to="/"  style="text-decoration: none; color: inherit;">
        <img :src="ImagePrefix + '/images/site/bookstorebrew.png'" alt="BookstoreBrew Logo" />
        <h1 class="text-logo" style="font-size: 2.4em;">BookstoreBrew</h1>
      </router-link>

    </section>

    <section class="title-and-search-bar">
      <form action="">
        <input type="text" class="search-bar"/>
        <button type="submit" class="search-icon-button">
          <img :src="ImagePrefix + '/images/icons/search.png'" alt="Search">
        </button>
      </form>
    </section>

    <section class="header-controls">
      <router-link to="/cart">
        <div class="cart-icon">
          <img :src="ImagePrefix + '/images/icons/cart.png'" alt="Cart">
          <span class="cart-count">{{cartStore.count}}</span>
        </div>
      </router-link>
      <!-- <button class="button transparent-button login">login</button> -->
      <button v-if="$route.name === 'home'" class="button transparent-button login">login</button>
      <strong v-else style="color: black;">Hi, Ritish!</strong>
      <div class="header-dropdown" id="dropdownToggle">
        <button class="button transparent-button categories-button">
          Categories
          <img :src="ImagePrefix + '/images/icons/drop-caret.png'" alt="Dropdown caret">
        </button>
        <ul class="categories-list">
          <li><router-link to="/category/Top-Picks">Top Picks</router-link></li>
          <li><router-link to="/category/Fiction">Fiction</router-link></li>
          <li><router-link to="/category/Non-Fiction">Non Fiction</router-link></li>
          <li><router-link to="/category/Romance">Romance</router-link></li>
          <li><router-link to="/category/Thriller">Thriller</router-link></li>
          <li><router-link to="/category/Self-help">Self-help</router-link></li>
        </ul>
      </div>
    </section>
  </header>
</template>
