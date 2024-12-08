package library;

//Sealed class for PaymentStatus (marked as abstract because it contains an abstract method)
public abstract sealed class PaymentStatus permits Paid, Unpaid {
		public abstract String statusMessage();  
}

