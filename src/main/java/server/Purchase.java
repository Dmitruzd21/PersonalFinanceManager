package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Purchase { // много обьектов создается в сервере в цикле
    private String title;
    private String date;
    private int sum;
    private String category;

   public Purchase(String title, String date, int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public String getCategory() {
        return category;
    }

    public int getSum() {
        return sum;
    }

    public String getTitle() {
        return title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public static Purchase createFromJSON(String jsonText) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Purchase purchase = gson.fromJson(jsonText, Purchase.class);
        return purchase;
    }

    public String toString () {
       return "Покупка: " + title + ", " + date + ", " + sum+ ", " + category;
    }
}
