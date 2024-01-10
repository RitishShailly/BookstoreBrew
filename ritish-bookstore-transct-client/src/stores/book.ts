import {defineStore} from "pinia";
import type {BookItem} from "@/types";
import {apiUrl} from "@/api";
import {useRoute} from "vue-router";
import {useCategoryStore} from "@/stores/category";

// const route = useRoute();
export const useBookStore = defineStore("book", {
    state: () => ({
        bookList: [] as BookItem[],
        currentCategory: "",
    }),

    actions: {
        async fetchBooks(categoryName: string) {
            const categoryStore = useCategoryStore()

            const selectedCategoryName =
                categoryStore.categoryList?.find((category) => category.name === categoryName)?.name ||
                categoryName
            console.log('cat name', categoryName)
            try {
                const response = await fetch(`${apiUrl}/categories/name/${selectedCategoryName || 'Fiction'}/books`)

                if (!response.ok) {
                    throw new Error(
                        `Failed to fetch books for category '${categoryName}': ${response.status} ${response.statusText}`
                    )
                }

                this.bookList = await response.json()
            } catch (e) {
                console.error(`Error fetching books for category '${categoryName}':`, e)

                // Propagate the error further, allowing the caller to handle or report it
                throw e
            }
            if (this.currentCategory !== selectedCategoryName) {
                this.bookList = await fetch(
                    `${apiUrl}/categories/name/${selectedCategoryName}/books`
                ).then((response) => response.json());
                this.currentCategory = selectedCategoryName;
            }
        },
    },


    // actions: {
    //     async fetchBooks(categoryName: string) {
    //         const response = await fetch(`${apiUrl}/categories/name/${categoryName}/books/`);
    //         const data = await response.json();
    //         this.bookList = data as BookItem[];
    //     }
    // },
    // getters
});


//${route.params.name}