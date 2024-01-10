<script setup lang="ts">
import { ref, computed } from "vue";
import { useRoute } from 'vue-router';
import type { BookItem } from "@/types";
import {useCartStore} from "@/stores/cart";
const cartStore = useCartStore();

// Define props
const props = defineProps<{
  book: BookItem;
}>();

const bookImagePrefix = `${import.meta.env.BASE_URL}/images/books`;

const bookImageFileName = (book: BookItem): string => {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/:/g, "");
  name = name.replace(/'/g, "");
  return `${name}.jpg`;
};

// Using Vue Router's useRoute composable
const route = useRoute();

// Refactored cartCount data
const cartCount = ref(route.name === 'category-view' ? 3 : 0);

// Refactored addToCart method
const addToCart = () => {
  if (route.name === 'category-view') {
    cartCount.value++;
  }
};

</script>

<style scoped>
.book-box{
    /* width: 400px; */
    height: 230px;
    display: flex;
    justify-content:start;
    background-color: antiquewhite;
    opacity: 90%;
    border-radius: 20px;
    margin:10px;    
}
.book-title {
  font-weight: bold;
}
.book-author {
  font-style: italic;
}
.book-image>img{
    height:200px;
    width:auto;
    padding:15px;
    display: block;
}
.book-description{
    display: flex;
    flex-flow: column wrap;
    justify-content: space-around;
    padding-right:15px;
}
.description-text>h3{
    margin-bottom:0px;
    margin-top:30px
}
.description-text>p{
    text-wrap: wrap;
    margin-top:0px;
}
.add-to-cart-button>button{
    background-color: #11110e;
    opacity:100%;
    height: 30px;
    border-radius: 15px;
    display:flex;
    flex-flow: row wrap;
    justify-content: space-around;
    padding-right: 15px;
    margin-bottom: 40px;
    margin-right: 0px;
    cursor: pointer;
    z-index: 10;
    transition: transform 0.3s;
}

.add-to-cart-button>button:hover {
  background-color: #373737;
  border: 2px solid #373737;
  transform: scale(1.1);
}

.add-to-cart-button>button>p{
    margin-left: 1px;
    margin:auto;
    color:antiquewhite;
}
.add-to-cart-button>button>div{
    height:20px;
    width:auto;
    background-color: antiquewhite;
    border-radius: 5px;
    align-content: center;
    margin-left: 10px;
    margin-top:3px;
}
.add-to-cart-button>button>div>img{
    align-self: center;
    height:20px;
    width:auto;
}
.book-image-container {
    position: relative;
    display: inline-block;  /* Makes the container wrap tightly around the image */
}
.read-now-icon {
    position: absolute;
    border-color: #100000;
    bottom: 5px;  /* Positions the div to the bottom of the book image */
    right: 2px;  /* Positions the div to the right of the book image */
    z-index: 2;  /* Layers the div above the book image */
    height: 35px;
    background-color: rgb(206, 150, 67);
    border-radius: 45%;
    overflow: hidden;
    transition: transform 0.3s;
}
.read-now-icon img {
    height: 25px;
    padding-top: 5px;
    padding-left: 4px;
    padding-right: 3px;
    transition: transform 0.3s;
}

.read-now-icon img:hover{
   transform: scale(1.2);
}
</style>

<template>
  <div>
    <li class = "book-box">
      <div class="book-image-container">
        <div class="book-image">
            <img
                :src="`${bookImagePrefix}/${bookImageFileName(props.book)}`"
                :alt="book.title">

        </div>
        <div class="read-now-icon" v-if="book.isPublic===true">
            <router-link to = #><img src="/images/icons/read_now.jpg"></router-link>
        </div>
      </div>
      
      <div class="book-description">
          <div class = "description-text">
              <h3>{{ props.book.title }}</h3>
              <p class = "book-author">{{ props.book.author }}</p>
              <h4>${{ (props.book.price / 100).toFixed(2) }}</h4>
          </div>
          <div class = "add-to-cart-button">
              <button @click="cartStore.addToCart(book)">
                  <p>Add to cart</p>
                  <div class = "add-cart-image">
                      <img src="/images/icons/cart.png" alt="add to cart">
                  </div>
              </button>
          </div>
      </div>
    </li>
  </div>
<!--   :src="'/public/images/books/' + bookImageFileName(props.book)">-->

</template>
