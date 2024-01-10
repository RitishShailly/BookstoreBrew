<script setup lang="ts">
import {reactive, ref} from "vue";
import useVuelidate from "@vuelidate/core";
import { helpers, maxLength, minLength, required } from "@vuelidate/validators";
import { useCartStore } from "@/stores/cart";
import { isCreditCard, isMobilePhone } from "@/validators";
// import { asDollarsAndCents } from "@/utils";
import {useOrderDetailsStore} from "@/stores/orderDetails";
import CheckoutFieldError from "@/components/CheckoutFieldError.vue";

import {computed} from 'vue';
// import router from "@/router";
import {useRouter} from "vue-router";
import type {OrderDetails, ServerErrorResponse} from "@/types";

const router = useRouter();

const cartStore = useCartStore();
const cart = cartStore.cart;

const defaultServerErrorMessage = "An unexpected error occurred, please try again."
// const defaultServerErrorMessage = "Transactions have not been implemented yet."
const serverErrorMessage = ref(defaultServerErrorMessage);
const goToCategory = () => {
  router.push('/category/Top-Picks');
};
const emailPattern = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/;
const emailValidator = (value: string) => {
  return emailPattern.test(value);
};
const total = computed(() => {
  return cartStore.cart.items.reduce((acc, item) => {
    return acc + item.quantity * item.book.price / 100;
  }, 0);
});

const shippingCost = 5.00;
const months: string[] = [
  "January",
  "February",
  "March",
  "April",
  "May",
  "June",
  "July",
  "August",
  "September",
  "October",
  "November",
  "December",
];

// Other script content
const orderDetailsStore = useOrderDetailsStore();
const currentYear = new Date().getFullYear();
const generateYears = computed(() => {
  const years = [];
  for (let i = 0; i < 15; i++) { // Next 10 years
    years.push(currentYear + i);
  }
  return years;
});

const form = reactive({
  name: "",
  address: "",
  phone: "",
  email: "",
  ccNumber: "",
  ccExpiryMonth: new Date().getMonth() + 1,
  ccExpiryYear: new Date().getFullYear(),
  checkoutStatus: "",
});

const rules = {
  name: {
    required: helpers.withMessage("Please provide a name.", required),
    minLength: helpers.withMessage(
      "Name must have at least 4 letters.",
      minLength(4)
    ),
    maxLength: helpers.withMessage(
      "Name can have at most 45 letters.",
      maxLength(45)
    ),
  },

  phone: {
    required: helpers.withMessage("Please provide a phone number.", required),
    isMobilePhone: helpers.withMessage("Please provide a valid phone number.", isMobilePhone),
  },

  address: {
    required: helpers.withMessage("Please provide an address.", required),
    minLength: helpers.withMessage(
        "Address must have at least 4 letters.",
        minLength(4)
    ),
    maxLength: helpers.withMessage(
        "Address can have at most 50 letters.",
        maxLength(50)
    ),
  },

  email: {
    required: helpers.withMessage("Please provide an email address.", required),
    emailValidator: helpers.withMessage("Please provide a valid email address.", emailValidator),
  },

  ccNumber: {
    required: helpers.withMessage("Credit card number is required.", required),
    minLength: helpers.withMessage(
        "Credit card number is too short.",
        minLength(13) // Most credit card numbers are between 13 and 19 digits long
    ),
    maxLength: helpers.withMessage(
        "Credit card number is too long.",
        maxLength(19)
    ),
    isCreditCard: helpers.withMessage(
        "Please enter a valid credit card number.",
        isCreditCard
    ),
  }
};

const v$ = useVuelidate(rules, form);

//I added this
const checkFormCorrectness = () => {
  if (!v$.value.$validate()) {
    console.log(v$.value.$errors);
    form.checkoutStatus = "ERROR";
    return false;
  }
  return true;
};


async function submitOrder() {
  // Set the form status to pending as the process starts
  form.checkoutStatus = "PENDING";
  serverErrorMessage.value = defaultServerErrorMessage;

  // Validate the form before proceeding
  const isFormCorrect = await v$.value.$validate();
  if (!isFormCorrect) {
    form.checkoutStatus = "ERROR";
    console.log(v$.value.$errors);  // Log the validation errors for debugging
    return;  // Exit the function if the form is not valid
  }

  try {
    // Call the store method to place the order
    const placeOrderResponse : OrderDetails | ServerErrorResponse =  await cartStore.placeOrder({
      name: form.name,
      address: form.address,
      phone: form.phone,
      email: form.email,
      ccNumber: form.ccNumber,
      ccExpiryMonth: form.ccExpiryMonth,
      ccExpiryYear: form.ccExpiryYear,
    });

    // Check if the response contains an error
    if ("error" in placeOrderResponse) {
      form.checkoutStatus = "SERVER_ERROR";
      serverErrorMessage.value = placeOrderResponse.message || defaultServerErrorMessage;
      console.log("Error placing order", placeOrderResponse);
    } else {
      // If the order is successfully placed
      form.checkoutStatus = "OK";
      orderDetailsStore.setOrderDetails(placeOrderResponse as OrderDetails);
      await router.push({ name: "confirmation-view" });  // Navigate to the confirmation view
    }
  } catch (e) {
    // Handle any unexpected errors during the order process
    form.checkoutStatus = "SERVER_ERROR";
    serverErrorMessage.value = defaultServerErrorMessage;
    console.error("Error placing order", e);
  }
}


// async function submitOrder() {
//   const isFormCorrect = await v$.value.$validate();
//   if (!isFormCorrect) {
//     form.checkoutStatus = "ERROR";
//   } else {
//     try {
//       form.checkoutStatus = "PENDING";
//       serverErrorMessage.value = defaultServerErrorMessage;
//
//       const placeOrderResponse: OrderDetails | ServerErrorResponse =
//           await cartStore.placeOrder({
//             name: form.name,
//             address: form.address,
//             phone: form.phone,
//             email: form.email,
//             ccNumber: form.ccNumber,
//             ccExpiryMonth: form.ccExpiryMonth,
//             ccExpiryYear: form.ccExpiryYear,
//           });
//
//       if ("error" in placeOrderResponse) {
//         form.checkoutStatus = "SERVER_ERROR";
//         serverErrorMessage.value = (
//             placeOrderResponse as ServerErrorResponse
//         ).message;
//         console.log("Error placing order", placeOrderResponse);
//       } else {
//         form.checkoutStatus = "OK";
//         orderDetailsStore.setOrderDetails(placeOrderResponse as OrderDetails);
//         await router.push({ name: "confirmation-view" });
//       }
//     } catch (e) {
//       form.checkoutStatus = "SERVER_ERROR";
//       serverErrorMessage.value = defaultServerErrorMessage;
//       console.log("Error placing order", e);
//     }
//   }
// }


</script>


<template>
  <div v-if="cartStore.count === 0" class="checkout-page-empty">
    <h2>"Oh no, your cart's plot twist is that it's empty!
      Brew up a new storyline by adding some page-turners from BookstoreBrew."</h2>
    <button id="continue-shopping-button" @click="goToCategory">Continue Shopping</button>
  </div>

  <div v-if="cartStore.count > 0" class="checkout-page">

    <section class="checkout-page-body" v-if="!cart.empty">

      <form @submit.prevent="submitOrder">
        <div class = "form-heading"><h2>Your Cart is Steaming <br> Time to Checkout!</h2></div>
        <div>
          <label for="name">Name</label>
          <input
              type="text"
              size="20"
              id="name"
              name="name"
              v-model.lazy="v$.name.$model"
          />
        </div>
        <CheckoutFieldError :field-name="v$.name"/>
        <div>
          <label for="address">Address</label>
          <input
              type="text"
              size="20"
              id="address"
              name="address"
              v-model.lazy="v$.address.$model"
          />
        </div>
        <CheckoutFieldError :field-name="v$.address"/>

        <div>
          <label for="phone">Phone</label>
          <input
            class="textField"
            type="text"
            size="20"
            id="phone"
            name="phone"
            v-model.lazy="v$.phone.$model"
          />
        </div>
        <CheckoutFieldError :field-name="v$.phone"/>

        <div>
          <label for="email">Email</label>
          <input
              type="text"
              size="20"
              id="email"
              name="email"
              v-model.lazy="v$.email.$model"
          />
        </div>
        <CheckoutFieldError :field-name="v$.email"/>

        <div>
          <label for="ccNumber">Credit Card</label>
          <input
              type="text"
              id="ccNumber"
              name="ccNumber"
              placeholder="4111 1111 1111 1111"
              v-model.lazy="v$.ccNumber.$model"
              @blur="v$.ccNumber.$touch()"
          :class="{ 'is-invalid': v$.ccNumber.$error }"
          required
          />
        </div>
        <CheckoutFieldError :field-name="v$.ccNumber"/>

        <div class="exp-date-group" style="display: flex; align-items: center; justify-content: flex-end;">
          <div class="exp-month">
            <label for="ccExpiryMonth">Exp Month</label>&nbsp;
            <select id="ccExpiryMonth" v-model="form.ccExpiryMonth">
              <option v-for="(month, index) in months" :key="index" :value="index + 1">
                {{ month }}
              </option>
            </select>
          </div>
          <div class="exp-year">
            <label for="ccExpiryYear"> &nbsp; Exp Year</label>&nbsp;
            <select id="ccExpiryYear" v-model="form.ccExpiryYear">
              <option v-for="year in generateYears" :key="year" :value="year">
                {{ year }}
              </option>
            </select>
          </div>
        </div>

        <div class = "cart-total-message">
          <p>
            Your credit card will be charged:
            <b>${{ (shippingCost + total).toFixed(2) }}</b>
          </p>

          <p>(
            <b>${{total.toFixed(2)}}</b> + <b>${{shippingCost.toFixed(2)}}</b>
            Shipping)
          </p>
        </div>

        <div class = "submit-button">
          <input
              type="submit"
              name="submit"
              class="button"
              :disabled="form.checkoutStatus === 'PENDING'"
              value="Complete Purchase"
              @click.prevent="submitOrder"
          />
        </div>



      <!-- TODO: Display the cart total, subtotal and surcharge. -->

      <section v-show="form.checkoutStatus !== ''" class="checkoutStatusBox">
        <div v-if="form.checkoutStatus === 'ERROR'">

          <p>Error: Please fix the problems above and try again.</p>
        </div>

        <div v-else-if="form.checkoutStatus === 'PENDING'">Processing...</div>

        <div v-else-if="form.checkoutStatus === 'OK'">Order placed...</div>

        <div v-else>{{ serverErrorMessage }}</div>
      </section>
      </form>
    </section>
  </div>
</template>


<style scoped>
/* TODO: Adapt these styles to your page */
.checkout-page {
  display: flex;
  justify-content: space-evenly;
  align-items: center;
  background-color: antiquewhite;
  opacity: 100%;
  color: rgba(0,0,0,1);
  margin:10dvh 35dvw;
  min-width: 25rem;
  border-radius: 3rem;
}

.checkout-page-empty{
  display: flex;
  text-align: center;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #341c09;
  height: 50rem;
  padding-left: 20%;
  padding-right: 20%;
  background-color: antiquewhite;
  opacity: 90%;
}

.checkout-page-empty>button{
  text-align:center;
  font-size: larger;
  margin: 10px;
  font-weight: bold;
  height: 40px;
  width: 200px;
  border:none;
  border-radius: 0.7em;
  cursor: pointer;
  background-color: #fad0b6;
  transition: transform 0.3s ease;
}


.checkout-page-empty>button:hover {
  background-color: #eab37a;
  transform: scale(1.1);
}

.submit-button>input{
  text-align: center;
  display: flex;
  justify-content: center;
  margin:10px;
  cursor: pointer;
  border: solid black 2px;
  border-radius: 0.7em;
  background-color: #fad0b6;
  translate: -3em;
  height: 40px;
  width: 200px;
  z-index: 10;
  opacity: 100%;
  transition: transform 0.3s ease;
  padding-bottom: 10px;
}

.submit-button>input:hover{
  background-color: #eab37a;
  transform: scale(1.1);
}

.checkout-page-body {
  display: flex;
  padding: 1em;
}

form {
  display: flex;
  flex-direction: column;
}

form > div {
  display: flex;
  justify-content: flex-end;
  margin-bottom: 0.5em;
  margin-top: 0.9em;
}

form > div > input,
form > div > select {
  background-color: #ffffff;
  margin-left: 0.5em;
  border: solid grey 2px;
}

form > .error {
  color: red;
  text-align: right;
}

.form-heading{
  margin:0;
  display:flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  text-align: center;
}

.checkoutStatusBox {
  margin: 1em;
  padding: 1em;
  text-align: center;
}

.cart-total-message{
  text-align: center;
  display:flex;
  justify-content: center;
  flex-direction: column;

}

.cart-total-message>p{
  margin-top:2px;
  margin-bottom:2px;
}
#continue-shopping-button{
  z-index: 30;
  opacity:100%;
}

</style>
