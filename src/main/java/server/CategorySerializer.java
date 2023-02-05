package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CategorySerializer {
    private Category maxCategory;
    private Category maxYearCategory;
    private Category maxMonthCategory;
    private Category maxDayCategory;

    public CategorySerializer(Category maxCategory, Category maxYearCategory, Category maxMonthCategory, Category maxDayCategory) {
        this.maxCategory = maxCategory;
        this.maxYearCategory = maxYearCategory;
        this.maxMonthCategory = maxMonthCategory;
        this.maxDayCategory = maxDayCategory;
    }

    public String convertToJSON() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
