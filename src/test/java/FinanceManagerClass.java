import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Category;
import server.FinanceManager;
import server.Purchase;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class FinanceManagerClass {

    FinanceManager financeManager;
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

    public void setCategoryForPurchases() {
        purchaseBun.setCategory("еда");
        purchaseSausage.setCategory("еда");
    }

    public void addPurchasesOfAllCategories() {
        financeManager.setCategoryAndAddPurchase(purchaseBun);
        financeManager.setCategoryAndAddPurchase(purchaseSausage);
        financeManager.setCategoryAndAddPurchase(purchaseCap);
        financeManager.setCategoryAndAddPurchase(purchaseSlippers);
        financeManager.setCategoryAndAddPurchase(purchaseSoap);
        financeManager.setCategoryAndAddPurchase(purchaseShares);
        financeManager.setCategoryAndAddPurchase(purchaseNuts);
    }

    @BeforeEach
    public void createManager() {
        financeManager = new FinanceManager();
    }

    @Test
    public void shouldConvertCategoryToJson() {
        addPurchasesOfAllCategories();
       String actualJson = financeManager.calculateEachCategorySum()
                .findCategoryWithMaxSum()
                .convertCategoryWithMaxSumToJSON();
       String expectedJson = "{\"maxCategory\":{\"category\":\"финансы\",\"sum\":800}}";
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
        financeManager.calculateEachCategorySum();
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
        Category category = financeManager.calculateEachCategorySum()
                .findCategoryWithMaxSum().getCategoryWithMaxSum();
        String actual = category.getCategoryName();
        String expected = "финансы";
        Assertions.assertEquals(expected, actual);
    }
}
