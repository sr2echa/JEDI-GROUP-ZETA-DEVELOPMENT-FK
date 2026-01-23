package com.flipkart.business;

public interface CustomerInterface {
	public void createCustomer();
	public boolean deleteCustomer(int id);
	public boolean updateCustomer(int id);
	public void listCustomer();
}
