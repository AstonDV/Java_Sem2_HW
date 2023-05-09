// [
//     {
//         "фамилия":"Иванов",
//         "оценка":"5",
//         "предмет":"Математика"
//     },
//     {
//         "фамилия":"Петрова",
//         "оценка":"4",
//         "предмет":"Информатика"
//     },
//     {
//         "фамилия":"Краснов",
//         "оценка":"5",
//         "предмет":"Физика"
//     }
// ]

// 1. Задача написать метод(ы), который распарсит строку и выдаст ответ вида:
//     - Студент Иванов получил 5 по предмету Математика.
//     - Студент Петрова получил 4 по предмету Информатика.
//     - Студент Краснов получил 5 по предмету Физика.
// 2. Используйте StringBuilder для подготовки ответа:
//     - Исходная json строка это просто String.
//     - Для работы используйте методы String (replace, split, substring и т.д.) по необходимости.
// 3. Создать метод, который запишет результат работы в файл:
//     - Обработайте исключения и запишите ошибки в лог файл.
// 4. *Получить исходную json строку из файла, используя FileReader или Scanner

package Seminars.Sem02HW;

import java.io.*;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class task01 {
    public static void main(String[] args) {
        String jsonObject = getJsonString();
        String result = parseJSON(jsonObject);
        System.out.println(result);
        saveToFile(result);
    }


    static String getJsonString() {
        String path = "Seminars/Sem02HW/src/gradebook.json";
        Logger logger = Logger.getAnonymousLogger();
        FileHandler logHandler = null;
        try {
            logHandler = new FileHandler("Task01_log.log");
            logger.addHandler(logHandler);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader fileReader = new FileReader(path);
             Scanner scanner = new Scanner(fileReader)) {
            while (scanner.hasNextLine()) {
                stringBuilder.append(scanner.nextLine());
            }
            logger.log(Level.INFO, "The file has been successfully read, we start returning the value,");
        } catch (FileNotFoundException exception) {
            logger.log(Level.WARNING, String.format("Exception thrown from FileReader: %s", exception));
        } catch (IOException exception) {
            logger.log(Level.WARNING, String.format("Exception thrown from Scanner: %s", exception));
        }

        logHandler.close();

        return stringBuilder.toString();
    }

    static String parseJSON(String json) {
        String[] template = new String[]{"- Student ", " received ", " by subject ", ".\n"};
        int templateIndex = 0;
        StringBuilder resultString = new StringBuilder();
        Pattern valuePattern = Pattern.compile(":\".+?[,}]");
        Matcher valueMatcher = valuePattern.matcher(json);
        while (valueMatcher.find()) {
            resultString.append(template[templateIndex++]);
            resultString.append(json.subSequence(valueMatcher.start() + 2, valueMatcher.end() - 2));
            if (templateIndex == template.length - 1) {
                resultString.append(template[templateIndex]);
                templateIndex = 0;
            }
        }
        return resultString.toString();
    }


    static void saveToFile(String source) {
        String path = "Seminars/Sem02HW/src/output.txt";

        Logger logger = Logger.getAnonymousLogger();

        FileHandler logHandler = null;
        try {
            logHandler = new FileHandler("Task01_log.log");
            logger.addHandler(logHandler);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        try (FileWriter newDump = new FileWriter(path, true)) {
            newDump.append(source);
            newDump.append("\n");
            newDump.flush();
            logger.log(Level.INFO, "The output file has been successfully appended with new data.");
        } catch (IOException exception) {
            logger.log(Level.WARNING, exception.toString());
        }

        logHandler.close();
    }
}