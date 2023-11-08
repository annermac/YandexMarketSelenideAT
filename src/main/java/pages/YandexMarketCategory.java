package pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import helpers.Assertions;
import io.qameta.allure.Step;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;

/**
 * Класс YandexMarketCategory в категории товаров проверяет правильно ли работают фильтры
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class YandexMarketCategory {

    /**
     * Метод проверяет, корректная ли страница в каталоге
     *
     * @param subCategoryName название подкаталога
     */
    @Step("Проверяем корректно ли открыли страницу категории {subCategoryName}")
    public YandexMarketCategory checkCorrectPage(String subCategoryName) {
        boolean categoryFind = $(byXpath("//div[@data-zone-name='searchTitle']/h1[text()='" + subCategoryName + "']")).exists();
        Assertions.assertTrue(categoryFind, "Заголовок " + subCategoryName + " не найден");
        return this;
    }

    /**
     * Метод выбирает чекбокс в фильтре
     *
     * @param filterCheckbox содержит название фильтра и его значений для чекбокса
     */
    @Step("Выбираем чекбоксы {filterCheckbox} в фильтре")
    public YandexMarketCategory selectCheckboxFields(Map<String, List<String>> filterCheckbox) {
        for (Map.Entry<String, List<String>> entry : filterCheckbox.entrySet()) {
            String titleFilter = entry.getKey();
            List<String> namesCheckbox = entry.getValue();
            for (String nameCheckbox : namesCheckbox) {
                String selectorCheckboxInput = ".//div[@data-zone-name='FilterValue']//span[text()='" + nameCheckbox + "']";
                SelenideElement blockFilterElement = $(byXpath("//div[@data-zone-name='Filter']//h4[text()='" + titleFilter + "']" +
                        "/../../following-sibling::div")).shouldBe(visible);

                SelenideElement loadFilterLink = blockFilterElement.find(byXpath(".//div[@data-zone-name='LoadFilterValues']/button"));

                if (loadFilterLink.exists()) {
                    blockFilterElement.find(byXpath(selectorCheckboxInput)).click();
                } else {
                    loadFilterLink.click();
                    blockFilterElement.find(byXpath(selectorCheckboxInput)).shouldBe(visible).click();
                }
            }
        }
        return this;
    }

    /**
     * Метод проверяет соответствие фильтра в заголовке у товаров
     *
     * @param expectedWordsInTitle ожидаемые слова для заголовка
     */
    @Step("Проверяем на соответствие ключевых слов {expectedWordsInTitle} в заголовах товаров")
    public YandexMarketCategory checkFilterCheckboxInTitleProduct(List<String> expectedWordsInTitle) {
        Instant startTime = Instant.now();
        List<String> errors = new ArrayList<>();
        int page = 1;
        while (paginationPageNext() && checkLoopIsNotInfinite(startTime)) {
            if (!containsFilterInTitleProduct(expectedWordsInTitle)) {
                errors.add("В названии товара не содержится фильтр чекбокса " + expectedWordsInTitle + " на странице " + page + "; ");
            }
            paginationPageNext();
            page++;
        }
        Assertions.assertTrue(errors.isEmpty(), errors.toString());
        return this;
    }

    /**
     * Метод проверяет соответствует ли название товара чекбоксу выбранным в фильтре
     *
     * @param filterCheckboxNames названия чекбоксов
     */
    private boolean containsFilterInTitleProduct(List<String> filterCheckboxNames) {
        boolean contains = false;
        ElementsCollection results = getProductsOnPage();
        for (SelenideElement product : results) {
            String title = product.find(byXpath(".//h3[@data-zone-name='title']/a/span")).getText();
            for (String filterName : filterCheckboxNames) {
                if (title.contains(filterName)) {
                    contains = true;
                    break;
                }
            }
            if(!contains) {
                break;
            }
        }
        return contains;
    }

    /**
     * Метод собирает все товары на текущей странице
     *
     * @return возвращает товары
     */
    @Step("Собираем все товары на текущей странице")
    private ElementsCollection getProductsOnPage() {
        ((JavascriptExecutor) getWebDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        return $$(byXpath("//article[@data-zone-name='snippet-card']"));
    }

    /**
     * Метод перемещается вперед по страницам
     */
    private boolean paginationPageNext() {
        SelenideElement button = $(byXpath("//div[@data-auto='pagination-next']"));
        if (!button.exists()) {
            return false;
        }
        button.click();
        button.shouldBe(visible);
        return true;
    }

    /**
     * Метод проверяет работает ли цикл больше 5 минут
     */
    private boolean checkLoopIsNotInfinite(Instant startTime) {
        Instant endTime = Instant.now();
        Duration duration = Duration.between(startTime, endTime);
        if (duration.toMinutes() > 5) {
            Assertions.fail("Цикл работает больше 5 минут");
            return false;
        }
        return true;
    }
}
