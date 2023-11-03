package helpers;

import org.junit.jupiter.params.provider.Arguments;

import java.util.*;
import java.util.stream.Stream;

/**
 * Класс DataProvider содержит тестовые данные
 *
 * @author Ермаченкова Анна
 * @version 1.0
 */
public class DataProvider {
    public static Stream<Arguments> providerCheckingCategory() {
        Map<String, List<String>> filerCheckbox = new HashMap<>();
        filerCheckbox.put("Производитель", List.of("Apple"));

        List<String> expectedWordsInTitle = new ArrayList<>();
        expectedWordsInTitle.add("iPhone");

        return Stream.of(Arguments.of(
                "Электроника",
                "Смартфоны",
                filerCheckbox,
                expectedWordsInTitle
        ));
    }
}
