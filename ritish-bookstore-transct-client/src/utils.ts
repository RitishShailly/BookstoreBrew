// From https://flaviocopes.com/how-to-format-number-as-currency-javascript/
import type {BookItem} from "@/types";

const PriceFormatter = new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2,
});


const bookImagePrefix = `${import.meta.env.BASE_URL}/images/books`;

export const asDollarsAndCents = function (cents: number) {
    return PriceFormatter.format(cents / 100.0);
};

export const getMonthYear = function (dateInMs: number) {
    const date = new Date(dateInMs);
    const month = date.toLocaleString("default", { month: "short" });
    const year = date.getFullYear();
    return `${month}/${year}`;
};

const bookImageFileName = function (book: BookItem): string {
    let name = book.title.toLowerCase();
    name = name.replace(/ /g, "-");
    name = name.replace(/'/g, "");
    name = name.replace(/:/g, "");
    return `${name}.jpg`;
};
function bookImageUrl(imageFileName: String) {
    return new URL(`${bookImagePrefix}/${imageFileName}`, import.meta.url).href;
}

export const getBookImageUrl = function (book: BookItem) {
    return bookImageUrl(bookImageFileName(book));
};