package library;

public final class Paid extends PaymentStatus {
    @Override
    public String statusMessage() {
        return "The payment has been completed.";
    }
}