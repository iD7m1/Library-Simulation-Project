public class Member {
    private int id;
    private String name;
    private int borrowedCount;
    private int numViewBorrowed;
    private int numBorrows;
    private int numReturns;
    private double sessionFees;
    public double TotalRevenue;
    public int TotalViewBorrowed;
    public int TotalBorrows;
    public int TotalReturns;

    public Member(int id, String name, int borrowedCount) {
        this.id = id;
        this.name = name;
        this.borrowedCount = borrowedCount;

        this.numViewBorrowed = 0;
        this.numBorrows = 0;
        this.numReturns = 0;
        this.sessionFees = 0.0;
        this.TotalRevenue = 0.0;
        this.TotalViewBorrowed = 0;
        this.TotalBorrows = 0;
        this.TotalReturns = 0;
    }

    private boolean canBorrow() {
        return borrowedCount < 5;
    }

    private boolean canReturn() {
        return borrowedCount > 0;
    }

    public void viewBorrowedCount() {
    }

    public boolean borrowOne() {
        if (canBorrow()) {
            borrowedCount++;
            return true;
        }
        return false;
    }

    public boolean returnOne() {
        if (canReturn()) {
            borrowedCount--;
            return true;
        }
        return false;
    }

    public void displayStatistics() {
    }

    public void reset() {
    }

}
