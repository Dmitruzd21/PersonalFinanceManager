package server;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinanceManager { // один объект
    private ProductsAndCategoriesMapStorage mapStorage = new ProductsAndCategoriesMapStorage();

    private List<Category> categoryList = mapStorage.createUniqueCategories();
    private List<Category> categoryListForYear = mapStorage.createUniqueCategories();
    private List<Category> categoryListForMonth = mapStorage.createUniqueCategories();
    private List<Category> categoryListForDay = mapStorage.createUniqueCategories();

    private Category maxCategory;
    private Category maxYearCategory;
    private Category maxMonthCategory;
    private Category maxDayCategory;

    private List<Purchase> purchases = new ArrayList<>();
    private static String binFile = "data.bin";
    private LocalDate currentDate = LocalDate.now();

    public String convertCategoriesWithMaxSumToJSON() {
        if (maxDayCategory.getSum() == 0) {
            maxDayCategory.setCategory("Не было покупок за текущий день");
        }
        if (maxMonthCategory.getSum() == 0) {
            maxMonthCategory.setCategory("Не было покупок за текущий месяц");
        }
        if (maxYearCategory.getSum() == 0) {
            maxYearCategory.setCategory("Не было покупок за текущий год");
        }
        CategorySerializer categorySerializer = new CategorySerializer(maxCategory, maxYearCategory, maxMonthCategory, maxDayCategory);
        return categorySerializer.convertToJSON();
    }

    public FinanceManager setCategoryAndAddPurchase(Purchase newPurchase) {
        // предварительно присваиваем категорию покупке
        Purchase purchaseWithSetCategory = mapStorage.setCategoryForPurchase(newPurchase);
        purchases.add(purchaseWithSetCategory);
        return this;
    }

    public void findMaxSumCategoriesForDifferentPeriods() {
        List<Purchase> purchasesForCurrentDay = getPurchasesForCurrentDay();
        List<Purchase> purchasesForCurrentMonth = getPurchasesForCurrentMonth();
        List<Purchase> purchasesForCurrentYear = getPurchasesForCurrentYear();
        maxCategory = calculateEachCategorySum(purchases, categoryList)
                .findCategoryWithMaxSum(categoryList);
        maxYearCategory = calculateEachCategorySum(purchasesForCurrentYear, categoryListForYear)
                .findCategoryWithMaxSum(categoryListForYear);
        maxMonthCategory = calculateEachCategorySum(purchasesForCurrentMonth, categoryListForMonth)
                .findCategoryWithMaxSum(categoryListForMonth);
        maxDayCategory = calculateEachCategorySum(purchasesForCurrentDay, categoryListForDay)
                .findCategoryWithMaxSum(categoryListForDay);
    }

    public FinanceManager calculateEachCategorySum(List<Purchase> purchasesList, List<Category> categories) { // предвариетльно покупке должна быть присвоена категория
        categories.stream().forEach(
                categoryItem -> {
                    int categorySum = 0;
                    for (Purchase purchase : purchasesList) {
                        if (purchase.getCategory().equals(categoryItem.getCategoryName())) {
                            categorySum += purchase.getSum();
                        }
                    }
                    categoryItem.setSum(categorySum);
                }
        );
        return this;
    }

    public Category findCategoryWithMaxSum(List<Category> categories) {
        Category categoryWithMaxAmount = categories.get(0);
        for (Category category : categories) {
            if (category.getSum() > categoryWithMaxAmount.getSum()) {
                categoryWithMaxAmount = category;
            }
        }
        return categoryWithMaxAmount;
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

    public List<Purchase> getPurchasesForCurrentYear() {
        return purchases.stream().
                filter(purchase ->
                        purchase.getYear() == currentDate.getYear())
                .collect(Collectors.toList());
    }

    public List<Purchase> getPurchasesForCurrentMonth() {
        return purchases.stream().
                filter(purchase ->
                        purchase.getYear() == currentDate.getYear()
                                && purchase.getMonth().equals(currentDate.getMonth()))
                .collect(Collectors.toList());
    }

    public List<Purchase> getPurchasesForCurrentDay() {
        return purchases.stream().
                filter(purchase ->
                        purchase.getYear() == currentDate.getYear()
                                && purchase.getMonth().equals(currentDate.getMonth())
                                && purchase.getDay() == currentDate.getDayOfMonth())
                .collect(Collectors.toList());
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public Category getMaxCategory() {
        return maxCategory;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setMaxCategory(Category maxCategory) {
        this.maxCategory = maxCategory;
    }

    public void setMaxYearCategory(Category maxYearCategory) {
        this.maxYearCategory = maxYearCategory;
    }

    public void setMaxMonthCategory(Category maxMonthCategory) {
        this.maxMonthCategory = maxMonthCategory;
    }

    public void setMaxDayCategory(Category maxDayCategory) {
        this.maxDayCategory = maxDayCategory;
    }
}
