/**
 * 
 */
package com.flipkart.client;

import com.flipkart.business.CustomerImpl;
import com.flipkart.business.CustomerInterface;
/**
 * 
 */
public class CustomerClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//create the instance of the class here
		CustomerInterface customer=new CustomerImpl();
		customer.createCustomer();
		customer.listCustomer();
		System.out.println("update customer->" +customer.updateCustomer(101));
		System.out.println("delete customer->" +customer.deleteCustomer(101));
	}

}
