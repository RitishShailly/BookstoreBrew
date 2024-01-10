import { createRouter, createWebHistory } from "vue-router";
import HomeView from "../views/HomeView.vue";
import CategoryView from "@/views/CategoryView.vue";
import CartView from "@/views/CartView.vue";
import CheckoutView from "@/views/CheckoutView.vue";
import ConfirmationView from "@/views/ConfirmationView.vue";
import NotFound from "@/views/NotFound.vue";
import {useBookStore} from "@/stores/book";
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: "/",
      name: "home",
      component: HomeView,
      alias: ["/home", "/Home","/index.html","/"],
    },
    {
      path: "/category",
      redirect: "/category/Top-Picks"
    },

    {
      path: "/category/:name?",
      name: "category-view",
      component: CategoryView,
      beforeEnter: async (to, from, next) => {
        const bookStore = useBookStore();
        const categoryName =
            to.params.name || bookStore.currentCategory || "Top-Picks";
        if (to.params.name) next();
        else
          next({
            name: "category-view",
            params: { name: categoryName },
            replace: true,
          });
      },
    },
    // {
    //   path: "/category/:name",
    //   name: "category-view",
    //   component: CategoryView,
    //   props:true,
    // },
    {
      path: "/cart",
      name: "cart-view",
      component: CartView,
    },
    {
      path: "/checkout",
      name: "checkout-view",
      component: CheckoutView,
    },
    {
      path: "/confirmation",
      name: "confirmation-view",
      component: ConfirmationView,
    },
    {
      path: "/:pathMatch(.*)*",
      name: "not-found",
      component: NotFound,
    },
  ],
});

export default router;
