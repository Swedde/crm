package crm.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum JSON_FIELDS {
    NAME("name"),
    NUMS("nums"),
    PRICE("price"),
    DATE("date");

    @Getter
    String title;
}
