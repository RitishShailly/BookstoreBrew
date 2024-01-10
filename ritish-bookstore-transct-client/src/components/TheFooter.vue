<script setup lang="ts">
const ImagePrefix = `${import.meta.env.BASE_URL}`;
</script>

<style scoped>
footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 10px;
  margin-bottom: 0;
  margin-top: 200px;
  background-color: var(--footer-background-color);
  position: fixed;
  bottom: 0;
  width: 100%;
  z-index: 10;
}

.copyright {
  padding-left: 10px;
}

.footer-links {
  padding-right: 20px;
}

.social-media-icons img {
  height: 30px;
  width: auto;
  margin: 0 5px;
}

.footer-links a {
  color: var(--text-color);
  text-decoration: none;
  margin: 5px 0;
  display: block;
}

.footer-links a:hover {
  text-decoration: underline;
}

body {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.social-media-container {
  text-align: start;
  flex-grow: 1;
  display: flex;
  justify-content: center;
  translate: -2%;
}


</style>

<template>
  <footer>
    <div class="copyright">©Copyright 2023 BookstoreBrew</div>
    <div class="social-media-container">
      <div class="social-media-icons">
        <router-link to="#"><img :src="ImagePrefix+'/images/icons/facebook.png'" alt="Facebook"></router-link>
        <router-link to="#"><img :src="ImagePrefix+'/images/icons/instagram.png'" alt="Instagram"></router-link>
        <router-link to="#"><img :src="ImagePrefix+'/images/icons/twitter.png'" alt="Twitter"></router-link>
      </div>
    </div>
    <div class="footer-links">
      <router-link to="#">Contact Us</router-link>
      <router-link to="#">Get Directions</router-link>
    </div>
  </footer>
</template>
