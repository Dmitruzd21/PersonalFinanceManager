package server;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FinanceManager { // один объект
    private ProductsAndCategoriesMapStorage mapStorage = new ProductsAndCategoriesMapStorage();
    private List<Category> categoryList = mapStorage.createUniqueCategories();
    private Category categoryWithMaxSum;
    private List<Purchase> purchases = new ArrayList<>();
    private static String binFile = "data.bin";

    public String convertCategoryWithMaxSumToJSON() {
        CategorySerializer categorySerializer = new CategorySerializer(categoryWithMaxSum);
        return categorySerializer.convertToJSON();
    }

    public FinanceManager setCategoryAndAddPurchase(Purchase newPurchase) {
        // предварительно присваиваем категорию покупке
        Purchase purchaseWithSetCategory = mapStorage.setCategoryForPurchase(newPurchase);
        purchases.add(purchaseWithSetCategory);
        return this;
    }

    public FinanceManager calculateEachCategorySum() { // предвариетльно покупке должна быть присвоена категория
        categoryList.stream().forEach(
                categoryItem -> {
                    int categorySum = 0;
                    for (Purchase purchase : purchases) {
                        if (purchase.getCategory().equals(categoryItem.getCategoryName())) {
                            categorySum += purchase.getSum();
                        }
                    }
                    categoryItem.setSum(categorySum);
                }
        );
        return this;
    }

    public FinanceManager findCategoryWithMaxSum() {
        Category categoryWithMaxAmount = categoryList.get(0);
        for (Category category : categoryList) {
            if (category.getSum() > categoryWithMaxAmount.getSum()) {
                categoryWithMaxAmount = category;
            }
        }
        categoryWithMaxSum = categoryWithMaxAmount;
        return this;
    }

    public void loadPurchasesListFromBin() {
        List<Purchase> purchaseList = null;
        try (FileInputStream fis = new FileInputStream(binFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            purchaseList = (List<Purchase>) ois.readObject();
             this.purchases = purchaseList;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void savePurchasesListToBin() {
        try (FileOutputStream fos = new FileOutputStream(binFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(purchases);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public Category getCategoryWithMaxSum() {
        return categoryWithMaxSum;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }
}
