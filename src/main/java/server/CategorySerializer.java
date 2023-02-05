package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CategorySerializer {
    private Category maxCategory;

    public CategorySerializer(Category maxCategory) {
        this.maxCategory = maxCategory;
    }

    public String convertToJSON() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(this);
    }
}
