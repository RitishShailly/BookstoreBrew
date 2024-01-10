<script setup lang="ts">
import TheHomeCategoryList from "@/components/TheHomeCategoryList.vue";
const ImagePrefix = `${import.meta.env.BASE_URL}/`;
</script>

<style scoped>
.home-content {
  display: flex;
  align-items: center;
  padding-top: 60px;
  justify-content: space-around;
}

.home-content>* {
  align-items: center;
}

.arrow-img{
  height: 30px;
  background-color: antiquewhite;
  opacity: 70%;
  border-radius: 50%;
  padding: 40%;
}

.home-content-category {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background-color: antiquewhite;
  opacity: 90%;
  height: 250px;
  width: 150px;
  border-radius: 35px;
  transition: transform 0.3s;
}

.home-content-category>>>img {
  height: 180px;
  width: 100px;
  margin-top: 30px;
}


.category-text {
  text-align: center;
  margin-top: 10px;
}

.banner, .home-body {
  position: relative;
  z-index: 2;
}

.banner{
  background-color: rgba(35, 23, 22, 0.6);
  padding:5dvh;
}
body .banner {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  padding-left: 60px;
}

.banner>h2{
  text-align: left;
  color: antiquewhite;
}

footer {
  position: fixed;
  bottom: 0;
  width: 100%;
}

.home-body {
  flex-grow: 1;
}

.shop-books {
  margin-top: 20px;
  border-radius: 25px;
  border:1px solid grey;
  padding: 10px 20px;
  font-size: 1.25em;
  display: inline-flex;
  align-items: center;
  cursor: pointer;
  background-color: antiquewhite;
  transition: transform 0.2s;
}

.shop-books img {
  height: 25px;
  width: auto;
  margin-left: 10px;
}

.shop-books:hover {
  background-color: var(--shop-button-color);
  color: antiquewhite;
  transform: scale(1.2);
}

.home-content-category a {
  text-decoration: none;
  color: inherit;
}

.home-content-category a:visited {
  color: inherit;
}

.home-content-category a:hover {
  text-decoration: none;
  color: inherit;
}

.home-content-category:hover{
  transform: scale(1.2);
}


</style>

<template>
  <div class="main-content">
    <div class="banner">
      <h2>Curated selections<br>brewed to perfection...</h2>
      <router-link to='/category/Top-Picks'>
        <button class="shop-books">
          SHOP BOOKS!
          <img :src="ImagePrefix + 'images/icons/buy-books.png'" alt="Shop books">
        </button>
      </router-link>
    </div>

    <div class="home-body">
      <!-- Content for the home page -->
      <div class="home-content">
<!--        <div class="home-content-arrow">-->
<!--          <router-link to="/">-->
<!--            <img class = "arrow-img" :src="ImagePrefix + 'images/icons/left.png'" alt="left">-->
<!--          </router-link>-->
<!--        </div>-->
        <div class="home-content-category">
          <router-link to="/category/Top-Picks">
            <img :src="ImagePrefix + 'images/categories/top_picks.png'" alt="Top Picks">
            <p class="category-text">Top Picks</p>
          </router-link>
        </div>
        <div class="home-content-category">
          <router-link to="/category/Fiction">
            <img :src="ImagePrefix + 'images/categories/fiction.jpeg'" alt="Fiction">
            <p class="category-text">Fiction</p>
          </router-link>
        </div>
        <div class="home-content-category">
          <router-link to="/category/Non-Fiction">
            <img :src="ImagePrefix + 'images/categories/nonfiction.jpeg'" alt="Non-Fiction">
            <p class="category-text">Non Fiction</p>
          </router-link>
        </div>
        <div class="home-content-category">
          <router-link to="/category/Romance">
            <img :src="ImagePrefix+ 'images/categories/romance.jpeg'" alt="Romance">
            <p class="category-text">Romance</p>
          </router-link>
        </div>
        <div class="home-content-category">
          <router-link to="/category/Thriller">
            <img :src="ImagePrefix + 'images/categories/thriller.jpeg'" alt="Thriller">
            <p class="category-text">Thriller</p>
          </router-link>
        </div>
        <div class="home-content-category">
          <router-link to="/category/Self-help">
            <img :src="ImagePrefix + 'images/categories/self-help.png'" alt="Self-help">
            <p class="category-text">Self help</p>
          </router-link>
        </div>
<!--        <div class="home-content-arrow">-->
<!--          <router-link to="/">-->
<!--            <img class = "arrow-img" :src="ImagePrefix + 'images/icons/right.png'" alt="right">-->
<!--          </router-link>-->
<!--        </div>-->
      </div>
    </div>
  </div>
</template>
