<script setup lang="ts">

import {useRouter} from "vue-router";
const router = useRouter();
</script>
<style>
.rounded-button {
  display: inline-block;
  padding: 10px 20px;
  background-color: #eab37a;
  font-weight: bolder;
  color: black;
  border-radius: 10px;
  text-decoration: none;
  transition: background-color 0.2s ease-in-out;
  border: none;

}

.rounded-button:hover {
  background-color: #b27758;
  cursor: pointer;
}
.not-found{
  text-align: center;

  background-color: antiquewhite;
  display:flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  height:85dvh;
  opacity:100%;
}
.heading{
  padding-bottom: 20px;
}
.button{
  padding-bottom: 100px;
}
</style>
<template>
  <div class="not-found">
    <div class="heading">
      <h1>Can't Find your page!</h1>
    </div>
    <br>

    <router-link to="/category">
    <button class="rounded-button">
      Continue shopping
    </button>
    </router-link>
  </div>
</template>