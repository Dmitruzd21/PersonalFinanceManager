package server;

public class Category {  // много объектов

    private String category;
    private int sum;

    public Category(String category, int sum) {
        this.category = category;
        this.sum = sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getCategoryName() {
        return category;
    }

    public int getSum() {
        return sum;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        return "Категория: " + category + ", " + sum;
    }
}

