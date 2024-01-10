<script setup lang="ts">
import CartTable from "@/components/CartTable.vue";
import { useCartStore } from "@/stores/cart";
const cartStore = useCartStore();
const clearCart = function () {
  cartStore.clearCart();
};
</script>
<style scoped>
h1 {
  margin: 0.5em auto;
  text-align: center;
  color: rgba(239, 206, 155, 0.96);
}
</style>

<template>
  <h1>Shopping Cart</h1>
  <cart-table></cart-table>
  <div style="height: 100px"></div>
</template>