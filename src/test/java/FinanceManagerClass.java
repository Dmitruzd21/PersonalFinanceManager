import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Category;
import server.FinanceManager;
import server.ProductsAndCategoriesMapStorage;
import server.Purchase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class FinanceManagerClass {

    FinanceManager financeManager;

    private Category maxYearCategory = new Category("Нет", 0);
    private Category maxMonthCategory = new Category("Нет", 0);
    private Category maxDayCategory = new Category("Нет", 0);

    Purchase purchaseBun = new Purchase("булка", "2022.02.08", 200);
    Purchase purchaseSausage = new Purchase("колбаса", "2022.02.08", 300);
    Purchase purchaseCap = new Purchase("шапка", "2022.02.08", 100);
    Purchase purchaseSlippers = new Purchase("тапки", "2022.02.08", 100);
    Purchase purchaseSoap = new Purchase("мыло", "2022.02.08", 50);
    Purchase purchaseShares = new Purchase("акции", "2022.02.08", 800);
    Purchase purchaseNuts = new Purchase("орехи", "2022.02.08", 30);

    int expectedSumOfEatCategory = purchaseBun.getSum() + purchaseSausage.getSum();
    int expectedSumOfClothesCategory = purchaseCap.getSum() + purchaseSlippers.getSum();
    int expectedSumOfHouseholdCategory = purchaseSoap.getSum();
    int expectedSumOfFinanceCategory = purchaseShares.getSum();
    int expectedSumOfOtherCategory = purchaseNuts.getSum();

    public void addPurchasesOfAllCategories() {
        financeManager.setCategoryAndAddPurchase(purchaseBun);
        financeManager.setCategoryAndAddPurchase(purchaseSausage);
        financeManager.setCategoryAndAddPurchase(purchaseCap);
        financeManager.setCategoryAndAddPurchase(purchaseSlippers);
        financeManager.setCategoryAndAddPurchase(purchaseSoap);
        financeManager.setCategoryAndAddPurchase(purchaseShares);
        financeManager.setCategoryAndAddPurchase(purchaseNuts);
    }

    public void setCategories() {
        financeManager.setMaxYearCategory(maxYearCategory);
        financeManager.setMaxMonthCategory(maxMonthCategory);
        financeManager.setMaxDayCategory(maxDayCategory);
    }

    public void setCategoryForPurchases() {
        purchaseBun.setCategory("еда");
        purchaseSausage.setCategory("еда");
    }

    @BeforeEach
    public void createManager() {
        financeManager = new FinanceManager();
    }

    @Test
    public void shouldConvertCategoryToJson() {
        addPurchasesOfAllCategories();
        List<Purchase> actualList = financeManager.getPurchases();
        financeManager.calculateEachCategorySum(actualList, financeManager.getCategoryList());
        Category category = financeManager.findCategoryWithMaxSum(financeManager.getCategoryList());
        financeManager.setMaxCategory(category);
        setCategories();
        String actualJson = financeManager.convertCategoriesWithMaxSumToJSON();
        String expectedJson = "{\"maxCategory\":{\"category\":\"финансы\",\"sum\":800},\"maxYearCategory\":{\"category\":\"Не было покупок за текущий год\",\"sum\":0},\"maxMonthCategory\":{\"category\":\"Не было покупок за текущий месяц\",\"sum\":0},\"maxDayCategory\":{\"category\":\"Не было покупок за текущий день\",\"sum\":0}}";
        Assertions.assertEquals(expectedJson, actualJson);
    }

    @Test
    public void shouldSetCategoryAndAddPurchase() {
        Purchase purchase1 = new Purchase("булка", "2022.02.08", 200);
        Purchase purchase2 = new Purchase("колбаса", "2022.02.08", 300);
        financeManager.setCategoryAndAddPurchase(purchase1);
        financeManager.setCategoryAndAddPurchase(purchase2);
        List<Purchase> actualList = financeManager.getPurchases();
        setCategoryForPurchases();
        List<Purchase> expectedList = Arrays.asList(purchaseBun, purchaseSausage);
        assertThat("Содержимое списков разное", expectedList, is(actualList));
    }

    @Test
    public void shouldCalculateEachCategorySum() {
        addPurchasesOfAllCategories();
        List<Purchase> actualList = financeManager.getPurchases();
        financeManager.calculateEachCategorySum(actualList, financeManager.getCategoryList());
        List<Category> categoryList = financeManager.getCategoryList();
        Optional eatOptional = categoryList.stream().filter(category -> category.getCategoryName().equals("еда")).findFirst();
        Optional clothesOptional = categoryList.stream().filter(category -> category.getCategoryName().equals("одежда")).findFirst();
        Optional householdOptional = categoryList.stream().filter(category -> category.getCategoryName().equals("быт")).findFirst();
        Optional financeOptional = categoryList.stream().filter(category -> category.getCategoryName().equals("финансы")).findFirst();
        Optional otherOptional = categoryList.stream().filter(category -> category.getCategoryName().equals("другое")).findFirst();
        Category eatCategory = (Category) eatOptional.get();
        Category clothesCategory = (Category) clothesOptional.get();
        Category householdCategory = (Category) householdOptional.get();
        Category financeCategory = (Category) financeOptional.get();
        Category otherCategory = (Category) otherOptional.get();
        Assertions.assertEquals(expectedSumOfEatCategory, eatCategory.getSum());
        Assertions.assertEquals(expectedSumOfClothesCategory, clothesCategory.getSum());
        Assertions.assertEquals(expectedSumOfHouseholdCategory, householdCategory.getSum());
        Assertions.assertEquals(expectedSumOfFinanceCategory, financeCategory.getSum());
        Assertions.assertEquals(expectedSumOfOtherCategory, otherCategory.getSum());
    }

    @Test
    public void shouldFindCategoryWithMaxSum() {
        addPurchasesOfAllCategories();
        List<Purchase> actualPurchaseList = financeManager.getPurchases();
        financeManager.calculateEachCategorySum(actualPurchaseList, financeManager.getCategoryList());
        Category category = financeManager.findCategoryWithMaxSum(financeManager.getCategoryList());
        String actualCategoryName = category.getCategoryName();
        String expectedCategoryName = "финансы";
        Assertions.assertEquals(expectedCategoryName, actualCategoryName);
    }

    @Test
    public void shouldGetJSONFromServer() {
        String clientJSONRequest = "{\"title\": \"булка\", \"date\": \"2023.02.05\", \"sum\": 200}";
        Purchase newPurchase = Purchase.createFromJSON(clientJSONRequest);
        financeManager.setCategoryAndAddPurchase(newPurchase).findMaxSumCategoriesForDifferentPeriods();
        String actualResponse = financeManager.convertCategoriesWithMaxSumToJSON();
        String expectedResponse = "{\"maxCategory\":{\"category\":\"еда\",\"sum\":200},\"maxYearCategory\":{\"category\":\"еда\",\"sum\":200},\"maxMonthCategory\":{\"category\":\"еда\",\"sum\":200},\"maxDayCategory\":{\"category\":\"еда\",\"sum\":200}}";
        Assertions.assertEquals(expectedResponse, actualResponse);
    }
}
