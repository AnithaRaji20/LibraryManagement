package library;

public final class Unpaid extends PaymentStatus {
    @Override
    public String statusMessage() {
        return "The payment is overdue.";
    }
}
