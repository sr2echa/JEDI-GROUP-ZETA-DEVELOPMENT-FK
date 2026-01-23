/**
 * 
 */
package com.flipkart.business;

/**
 * 
 */
public class CustomerImpl implements CustomerInterface {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createCustomer() {
		// TODO Auto-generated method stub
		System.out.println("the Create Customer Method");
	}

	@Override
	public boolean deleteCustomer(int id) {
		// TODO Auto-generated method stub
		System.out.println("the delete by id Methods-->"+id);
		return false;
	}

	@Override
	public boolean updateCustomer(int id) {
		// TODO Auto-generated method stub
		System.out.println("update by id-->"+id);
		return false;
	}

	@Override
	public void listCustomer() {
		// TODO Auto-generated method stub
		System.out.println("list of Cutomers");
		
	}

}
