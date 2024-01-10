<script setup lang="ts">
import type { BookItem } from "@/types";
import { useCartStore } from "@/stores/cart";
import router from "@/router";
import {computed} from "vue";
const cartStore = useCartStore();

const bookImageFileName = function (book: BookItem): string {
  let name = book.title.toLowerCase();
  name = name.replace(/ /g, "-");
  name = name.replace(/'/g, "");
  name = name.replace(/:/g, "");
  return `${name}.jpg`;
};
const bookImagePrefix = `${import.meta.env.BASE_URL}/images/books`;

const updateCart = function (book: BookItem, quantity: number) {
  cartStore.cart.update(book, quantity);
};
const goToCategory = () => {
  router.push('/category/Top-Picks');
};

const goToCheckout = () => {
  router.push('/checkout');
};

const emptyCart = () => {
  cartStore.clearCart()
};

const total = computed(() => {
  return cartStore.cart.items.reduce((acc, item) => {
    return acc + item.quantity * item.book.price / 100;
  }, 0);
});

</script>


<style scoped>
.cart-table {
  display: grid;
  grid-template-columns: max-content minmax(10em, 20em) repeat(3, max-content);
  row-gap: 1em;
  width: fit-content;
  margin: 0 auto;
  background-color: aliceblue;
}

ul {
  display: contents;
}

ul > li {
  display: contents;
}

.table-heading {
  background-color: #333;
  color: white;
}

.table-heading > * {
  background-color: #333;
  padding: 0.5em;
}

.heading-book {
  grid-column: 1 / 3;
}

.heading-price {
  grid-column: 3 / 5;
  text-align: right;
}

.heading-subtotal {
  text-align: right;
  grid-column: -2 / -1;
}

.cart-book-image {
  padding: 0 1em;
}

.cart-book-image > * {
  margin-left: auto;
  margin-right: 0;
}

img {
  display: block;
  width: 100px;
  height: auto;
}


.cart-book-price {
  padding-left: 1em;
  text-align: right;
}

.cart-book-quantity {
  padding-left: 1em;
  padding-right: 1em;
}

.cart-book-subtotal {
  text-align: right;
  padding-left: 1em;
  padding-right: 1em;
}

/* Row separators in the table */

.line-sep {
  display: block;
  height: 1px;
  background-color: black;
  grid-column: 1 / -1;
}

/* Increment/decrement buttons */

.icon-button {
  border: none;
  cursor: pointer;
  background-color: rgb(0,0,0,0);
}

.inc-button {
  font-size: 1.125rem;
  color: var(--primary-color);
  margin-right: 0.25em;
}

.inc-button:hover {
  color: var(--primary-color-dark);
}

.dec-button {
  font-size: 1.125rem;
  color: #a28d8d;
}

.dec-button:hover {
  color: #524747;
}

.cart-total-amount {
  display: flex;
  justify-content: end;
  width: 6em;
  padding:10px;
  border-radius:5px;
  background-color: white;
  margin: 1px auto 20px auto;
  translate: 18.4em;
}

.cart-total-amount p {
  margin: 0; /* remove default margins for <p> */
  text-align: right;
}

/* Navigation buttons */
.nav-buttons{
  text-align:center;
}

.nav-buttons > * {
  margin: 10px;
  font-weight: bold;
  height: 30px;
  width: 150px;
  border-radius: 5px;
  cursor: pointer;
  transition: transform 0.3s ease;
}

#clear-cart-button{
  width: 100px;
}

#proceed-checkout-button{
  background-color: #eec4ae;
}

.nav-buttons > *:hover {
  background-color: #eab37a;
  transform: scale(1.1);
}

#proceed-checkout-button:hover{
  background-color: #eab37a;
}

.empty-text{
  margin-left: 10px;
}

input[type="number"] {
  width: 4em;
}

select {
  background-color: var(--primary-color);
  color: white;
  border: 2px solid var(--primary-color-dark);
  border-radius: 3px;
}
</style>

<template>
  <div class="cart-table">
    <ul>
      <li class="table-heading">
        <div class="heading-book">Book</div>
        <div class="heading-price">Price / Quantity</div>
        <div class="heading-subtotal">Amount</div>
      </li>
      <template v-if="cartStore.count > 0" v-for="item in cartStore.cart.items" :key="item.book.bookId">
        <li>
          <div class="cart-book-image">
            <img :src="`${bookImagePrefix}/${bookImageFileName(item.book)}`" :alt="item.book.title" width="100px" height="auto" />
          </div>
          <div class="cart-book-title">{{ item.book.title }}</div>
          <div class="cart-book-price">${{ (item.book.price / 100).toFixed(2) }}</div>
          <div class="cart-book-quantity">
            <span class="quantity">{{ item.quantity }}</span>
            <button class="icon-button inc-button" @click="updateCart(item.book, item.quantity + 1)">
              <i class="fas fa-plus-circle"></i>
            </button>
            <button class="icon-button dec-button" @click="updateCart(item.book, item.quantity - 1)">
              <i class="fas fa-minus-circle"></i>
            </button>
          </div>
          <div class="cart-book-subtotal">${{ (item.quantity * (item.book.price / 100)).toFixed(2) }}</div>
        </li>
        <li class="line-sep"></li>
      </template>
      <li v-else>
        <p class="empty-text">The cart is currently empty.</p>
      </li>
    </ul>
  </div>
  <div v-if="cartStore.count > 0">
    <div class="cart-total-amount">
      <p>Total: ${{ total.toFixed(2) }}</p>
    </div>
    <div class="nav-buttons button">
      <button id="continue-shopping-button" @click="goToCategory">Continue Shopping</button>
      <button id="proceed-checkout-button" @click="goToCheckout">Proceed to checkout</button>
      <br>
      <button id="clear-cart-button" @click="emptyCart">Clear Cart</button>
    </div>
  </div>
  <div v-else class="nav-buttons button">
    <button id="continue-shopping-button" @click="goToCategory">Continue Shopping</button>
  </div>
</template>