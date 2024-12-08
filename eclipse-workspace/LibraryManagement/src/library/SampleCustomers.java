package library;

import java.util.ArrayList;
import java.util.List;

public class SampleCustomers {

	public static List<Customer> createCustomers() {
        List<Customer> customersList = new ArrayList<>();
        
        Customer customer1 = new Customer("Alice", 5);
        Customer customer2 = new Customer("Bob", 3);
        Customer customer3 = new Customer("Charlie", 4);
        Customer customer4 = new Customer("Diana", 2);
        Customer customer5 = new Customer("Eva", 6);
        Customer customer6 = new Customer("Frank", 5);
        Customer customer7 = new Customer("Grace", 2);
        Customer customer8 = new Customer("Hannah", 3);
        Customer customer9 = new Customer("Ian", 1);
        Customer customer10 = new Customer("Jack", 4);
        
        customersList.add(customer1);
        customersList.add(customer2);
        customersList.add(customer3);
        customersList.add(customer4);
        customersList.add(customer5);
        customersList.add(customer6);
        customersList.add(customer7);
        customersList.add(customer8);
        customersList.add(customer9);
        customersList.add(customer10);
        
        return customersList;
    }
}