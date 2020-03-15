package crm.utility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@AllArgsConstructor
public class Params {
    public String name;
    public int nums;
    public double price;
    public Date date;

    private static final JsonParser JSON_PARSER = new JsonParser();

    public static Params getParams(String param) throws Exception {
        String name;
        int nums;
        double price;
        String dateStr;
        Date date;

        try {
            JsonObject elem = new JsonParser().parse(param).getAsJsonObject();
            name = elem.get(JSON_FIELDS.NAME.getTitle())
                    .getAsString()
                    .trim();
            nums = elem.get(JSON_FIELDS.NUMS.getTitle())
                    .getAsInt();
            price = elem.get(JSON_FIELDS.PRICE.getTitle())
                    .getAsDouble();
            dateStr = elem.get(JSON_FIELDS.DATE.getTitle())
                    .getAsString()
                    .trim();
        } catch (Exception e) {
            throw new Exception("Incorrect query");
        }

        date = getDateFromString(dateStr);

        if (name.isEmpty()) {
            throw new Exception("Empty product name");
        }

        if (nums < 1) {
            throw new Exception("\"nums\" can't be less than 1");
        }

        if (price < 0) {
            throw new Exception("Negative price");
        }

        return new Params(name, nums, price, date);
    }

    public static String getJsonParam(String param, String key) throws Exception {
        String value;
        try {
            value = JSON_PARSER.parse(param)
                    .getAsJsonObject()
                    .get(key)
                    .getAsString()
                    .trim();
        } catch (Exception e) {
            throw new Exception("Incorrect query");
        }
        return value;
    }

    public static Date getDateFromString(String date) throws Exception{
        try {
            return new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            throw new Exception("Incorrect date format. Correct: \"dd-MM-yyyy\"");
        }
    }
}
