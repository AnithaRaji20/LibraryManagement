package library;

public class Customer extends Person {
    private int maxBooksAllowed;
    
    public Customer(String name) {
        this(name, 5);  
    }

    public Customer(String name, int maxBooksAllowed) {
        super(name); 
        this.maxBooksAllowed = maxBooksAllowed;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    public void setMaxBooksAllowed(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }
    
    @Override
    public void displayInfo() {
        super.displayInfo();  
        System.out.println("Max books allowed: " + maxBooksAllowed);
    }
}

