package ru.yandex.market;

import helpers.Properties;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import pages.YandexMarketCategory;
import pages.YandexMarketMain;

import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;

/**
 * Класс Tests тестирует работоспособность страницы содержащую товары в определенной категории
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class Tests extends BaseTests {
    /**
     * Метод проверяет, что правильно работает фильтр и поиск в категории товаров
     *
     * @param categoryName         название каталога
     * @param subCategoryName      название подкаталог
     * @param filterCheckbox       содержит название фильтра и его значений для чекбокса
     * @param expectedWordsInTitle ожидаемаемые заголовки товаров
     */
    @Feature("Проверка работоспособности страницы содержащую товары в определенной категории")
    @DisplayName("Проверка, что правильно работает фильтр и поиск в категории товаров")
    @MethodSource("helpers.DataProvider#providerCheckingCategory")
    @ParameterizedTest(name = "{displayName}: {arguments}")
    public void testCategory(String categoryName, String subCategoryName, Map<String, List<String>> filterCheckbox, List<String> expectedWordsInTitle) {
        open(Properties.testsProperties.yandexMarketURL(), YandexMarketMain.class)
                .openPageCategory(categoryName, subCategoryName);

        new YandexMarketCategory()
                .checkCorrectPage(subCategoryName)
                .selectCheckboxFields(filterCheckbox)
                .checkFilterCheckboxInTitleProduct(expectedWordsInTitle);

    }
}
