package server;

import utils.TsvMapConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductsAndCategoriesMapStorage { // один обьект, должен быть готов перед приемом покупок
    private Map<String, String> productsAndCategory;
    TsvMapConverter tsvMapConverter = new TsvMapConverter();
    String tsvFile = "categories.tsv";
    String otherCategory = "другое";

    public ProductsAndCategoriesMapStorage() {
        this.productsAndCategory = tsvToMap();
    }

    public Map<String, String> tsvToMap() {
        return tsvMapConverter.convertTSVFileToMap(tsvFile);
    }

    public List<Category> createUniqueCategories() {
        List<Category> categories = new ArrayList<>();
        String otherCategory = "другое";
        // основные операции
        List<String> categoryNameList = new ArrayList<>(productsAndCategory.values());
        List<String> listOfUniqueCategoryNames = categoryNameList.stream().distinct().collect(Collectors.toList());
        for (String uniqueCategoryName : listOfUniqueCategoryNames) {
            categories.add(new Category(uniqueCategoryName, 0));
        }
        categories.add(new Category(otherCategory, 0));
        return categories;
    }

    public Purchase setCategoryForPurchase(Purchase purchase) {
        String purchaseTitle = purchase.getTitle();
        String categoryNameForPurchase = productsAndCategory.get(purchaseTitle);
        if (categoryNameForPurchase != null) {
            purchase.setCategory(categoryNameForPurchase);
        } else {
            purchase.setCategory(otherCategory);
        }
        return purchase;
    }
}
