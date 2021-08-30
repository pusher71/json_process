import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Program {

    public static void main(String[] args) throws Exception {
        //проверить количество аргументов
        if (args.length != 2) {
            System.out.println("Неверное количество аргументов");
            return;
        }

        //считать аргументы
        String functionName = args[0];
        String jsonPath = args[1];

        //считать файл с JSON
        String jsonStr = new String(Files.readAllBytes(Paths.get(jsonPath)), StandardCharsets.UTF_8);

        //перевести в JSON[]
        //JSON[] jsons = (new Gson()).fromJson(jsonStr, JSON[].class);

        //выполнить статистическую функцию (для парсинга объектов используется org.json)
        if (functionName.equals("avg")) {
            System.out.println("avg = " + avg(jsonStr, "ups_adv_battery_run_time_remaining"));
        } else if (functionName.equals("max")) {
            System.out.println("max = " + max(jsonStr, "ups_adv_output_voltage"));
        } else if (functionName.equals("values")) {
            System.out.println("values = " + values(jsonStr, "host"));
        } else {
            System.out.println("Неизвестное название функции!");
        }
    }

    // Найти среднее значение поля
    private static double avg(String jsonStr, String fieldToUse) {
        int numberCount = 0; //количество числовых значений на данном поле
        long sum = 0; //сумма числовых значений

        JSONArray array = new JSONArray(jsonStr);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = (JSONObject)array.get(i);
            if (obj.has(fieldToUse)) {
                Object number = obj.get(fieldToUse);
                if (number instanceof Integer) { //org.json парсит все числа в Integer
                    numberCount++;
                    sum += (long)(int)number;
                }
            }
        }

        return (double)sum / numberCount;
    }

    // Найти максимальное значение поля
    private static int max(String jsonStr, String fieldToUse) {
        int max = Integer.MIN_VALUE; //максимальное значение

        JSONArray array = new JSONArray(jsonStr);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = (JSONObject)array.get(i);
            if (obj.has(fieldToUse)) {
                Object number = obj.get(fieldToUse);
                if (number instanceof Integer) { //org.json парсит все числа в Integer
                    if (max < (Integer)number) {
                        max = (Integer)number;
                    }
                }
            }
        }

        return max;
    }

    // Найти список уникальных значений поля
    private static List values(String jsonStr, String fieldToUse) {
        List list = new ArrayList<>(); //список уникальных значений

        JSONArray array = new JSONArray(jsonStr);
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = (JSONObject)array.get(i);
            if (obj.has(fieldToUse)) {
                Object value = obj.get(fieldToUse);
                if (!value.equals(JSONObject.NULL) && !list.contains(value)) {
                    list.add(value);
                }
            }
        }

        return list;
    }
}
