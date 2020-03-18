package crm.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum JSON_ANSW {
    STATUS("status"),
    SALES_REPORT("sales_report"),
    OK("ok");

    @Getter
    String title;
}
