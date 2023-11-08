package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;

/**
 * Класс YandexMarketMain на главной странице ищет в Каталоге категорию и переходит в подкатегорию
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class YandexMarketMain {
    /**
     * Метод открывает страницу подкатегории
     *
     * @param categoryName    название каталога
     * @param subCategoryName название подкаталога
     */
    @Step("Переходим на страницу категории {categoryName} - > {subCategoryName}")
    public YandexMarketCategory openPageCategory(String categoryName, String subCategoryName) {
        $x("//header//button/span[text()='Каталог']").click();
        SelenideElement categoryLinkElement = $(byXpath("//li[@data-zone-name='category-link']//span[text()='" + categoryName + "']")).shouldBe(exist);
        SelenideElement subCategoryLinkElement = $(byXpath("//ul[@data-autotest-id='subItems']/li//a[text()='" + subCategoryName + "']"));
        actions().moveToElement(categoryLinkElement);
        subCategoryLinkElement.shouldBe(exist);
        actions().moveToElement(subCategoryLinkElement).click().perform();
        return page(YandexMarketCategory.class);
    }
}
