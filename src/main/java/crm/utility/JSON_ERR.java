package crm.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum JSON_ERR {
    ERROR("error"),
    NOT_EXIST_PRODUCT("product not exist"),
    EXIST_PRODUCT("product already exist"),
    EMPTY_PRODUCT("empty product name"),
    EMPTY_PARAMETRS("empty query"),
    NOT_ENOUGH_PRODUCT("not enough product"),
    DB_EXCEP("db fail");

    @Getter
    String title;
}
