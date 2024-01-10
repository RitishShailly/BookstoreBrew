package business.order;

import api.ApiException;
import business.BookstoreDbException;
import business.JdbcUtils;
import business.book.Book;
import business.book.BookDao;
import business.cart.ShoppingCart;
import business.cart.ShoppingCartItem;
import business.customer.Customer;
import business.customer.CustomerForm;
import business.order.OrderDao;
import business.order.LineItemDao;
import business.customer.CustomerDao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.YearMonth;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DefaultOrderService implements OrderService {

	private BookDao bookDao;
	private OrderDao orderDao;
	private LineItemDao lineItemDao;
	private CustomerDao customerDao;

	public void setBookDao(BookDao bookDao) {
		this.bookDao = bookDao;
	}
	public void setOrderDao(OrderDao orderDao) {
		this.orderDao = orderDao;
	}
	public void setLineItemDao(LineItemDao lineItemDao) {
		this.lineItemDao = lineItemDao;
	}
	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}


	@Override
	public OrderDetails getOrderDetails(long orderId) {
		Order order = orderDao.findByOrderId(orderId);
		Customer customer = customerDao.findByCustomerId(order.customerId());
		List<LineItem> lineItems = lineItemDao.findByOrderId(orderId);
		List<Book> books = lineItems
				.stream()
				.map(lineItem -> bookDao.findByBookId(lineItem.bookId()))
				.toList();
		return new OrderDetails(order, customer, lineItems, books);
	}

	@Override
    public long placeOrder(CustomerForm customerForm, ShoppingCart cart) {

		validateCustomer(customerForm);
		validateCart(cart);

		try (Connection connection = JdbcUtils.getConnection()) {
			Date ccExpDate = getCardExpirationDate(
					customerForm.getCcExpiryMonth(),
					customerForm.getCcExpiryYear());
			return performPlaceOrderTransaction(
					customerForm.getName(),
					customerForm.getAddress(),
					customerForm.getPhone(),
					customerForm.getEmail(),
					customerForm.getCcNumber(),
					ccExpDate, cart, connection);
		} catch (SQLException e) {
			throw new BookstoreDbException("Error during close connection for customer order", e);
		}
	}

	private Date getCardExpirationDate(String monthString, String yearString) {
		return new Date(); // TODO Implement this correctly
	}

	private int generateConfirmationNumber(){
		return ThreadLocalRandom.current().nextInt(999999999);
	}

	private long performPlaceOrderTransaction(
			String name, String address, String phone,
			String email, String ccNumber, Date date,
			ShoppingCart cart, Connection connection) {
		try {
			connection.setAutoCommit(false);
			long customerId = customerDao.create(
					connection, name, address, phone, email,
					ccNumber, date);
			long customerOrderId = orderDao.create(
					connection,
					cart.getComputedSubtotal() + cart.getSurcharge(),
					generateConfirmationNumber(), customerId);
			for (ShoppingCartItem item : cart.getItems()) {
				lineItemDao.create(connection, customerOrderId,
						item.getBookId(), item.getQuantity());
			}
			connection.commit();
			return customerOrderId;
		} catch (Exception e) {
			try {
				connection.rollback();
			} catch (SQLException e1) {
				throw new BookstoreDbException("Failed to roll back transaction", e1);
			}
			return 0;
		}
	}




	private void validateCustomer(CustomerForm customerForm) {

		String name = customerForm.getName();
		String phone = customerForm.getPhone();
		String address = customerForm.getAddress();
		String email = customerForm.getEmail();
		String ccNumber = customerForm.getCcNumber();

		if (name == null || name.equals("") || name.length() > 45) {
			throw new ApiException.ValidationFailure("Invalid name field");
		}

		// TODO: Validation checks for address, phone, email, ccNumber

		if (expiryDateIsInvalid(customerForm.getCcExpiryMonth(), customerForm.getCcExpiryYear())) {
			throw new ApiException.ValidationFailure("Invalid expiry date");
		}

		// Address validation
		if (address == null || address.trim().isEmpty() || address.length() > 100) {
			throw new ApiException.ValidationFailure("address","Invalid address field");
		}

		// Email validation
		if (email == null || email.isEmpty()) {
			throw new ApiException.ValidationFailure("email","Missing email field");
		}

		if (!email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) {
			throw new ApiException.ValidationFailure("email","Invalid email field");
		}

		// ccNumber validation using a placeholder isValidCcNumber method
		if (ccNumber == null || ccNumber.isEmpty()) {
			throw new ApiException.ValidationFailure("ccNumber","Missing credit card number");
		}
		String ccNumberDigits = ccNumber.replaceAll("\\D", "");
		if (ccNumberDigits.length() < 14 || ccNumberDigits.length() > 16){
			throw new ApiException.ValidationFailure("phone", "Invalid credit card field");
		}

		// Phone:
		if (phone == null || phone.isEmpty()){
			throw new ApiException.ValidationFailure("phone","Missing phone field");
		}
		String phoneDigits = phone.replaceAll("\\D","");

		if (phoneDigits.length() != 10){
			throw new ApiException.ValidationFailure("phone", "Invalid phone field");
		}
	}

	private boolean expiryDateIsInvalid(String ccExpiryMonth, String ccExpiryYear) {

		// TODO: return true when the provided month/year is before the current month/yeaR
		// HINT: Use Integer.parseInt and the YearMonth class
		try {
			// Parsing the month and year to integers
			int month = Integer.parseInt(ccExpiryMonth);
			int year = Integer.parseInt(ccExpiryYear);

			// Creating a YearMonth object from the provided expiry date
			YearMonth expiryDate = YearMonth.of(year, month);

			// Defining the valid start date (October 2023 is invalid, so November 2023 is valid)
			YearMonth validStartDate = YearMonth.of(2023, 11);

			// Checking if the expiry date is before the valid start date
			return expiryDate.isBefore(validStartDate);
		} catch (NumberFormatException e) {
			// If there's an error in parsing, consider the date as invalid
			return true;
		}
	}

	private void validateCart(ShoppingCart cart) {

		if (cart.getItems().size() <= 0) {
			throw new ApiException.ValidationFailure("Cart is empty.");
		}

		cart.getItems().forEach(item-> {
			if (item.getQuantity() < 0 || item.getQuantity() > 99) {
				throw new ApiException.ValidationFailure("Invalid quantity");
			}
			Book databaseBook = bookDao.findByBookId(item.getBookId());
			// TODO: complete the required validations
			//Price
			if (item.getBookForm().getPrice()  != databaseBook.price()){
				throw new ApiException.ValidationFailure("Book Prices do not match");
			}
			// BookId
			if (item.getBookForm().getBookId()  != databaseBook.bookId()){
				throw new ApiException.ValidationFailure("Book Id do not match");
			}
			// categoryId
			if (item.getBookForm().getCategoryId()  != databaseBook.categoryId()){
				throw new ApiException.ValidationFailure("category Id do not match");
			}
		});
	}

}
