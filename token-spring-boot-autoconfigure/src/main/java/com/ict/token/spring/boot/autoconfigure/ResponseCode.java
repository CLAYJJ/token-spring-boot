package com.ict.token.spring.boot.autoconfigure;

public enum  ResponseCode {
    SUCCESS("0", "成功"),
    MISSING_ARGS("1", "缺少参数"),
    UNKNOWN("2", "未知错误"),
    ILLEGAL_ARGS("3", "参数不合法"),
    ILLEGAL_TOKEN("4", "token不合法"),
    TOKEN_EXPIRED("5", "token失效"),
    CALL_LIMITED("6", "接口调用次数超过当日限制"),
    SYSTEM_BUSY("7", "系统繁忙，稍后再试"),
    DUPLICATE("8", "数据已存在"),
    NONE_EXISTS("9", "数据不存在");
    private String name;
    private String value;
    ResponseCode(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
