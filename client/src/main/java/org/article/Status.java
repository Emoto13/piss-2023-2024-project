package org.article;

public enum Status {
    OK("ok"),
    ERROR("error");

    private final String value;

    Status(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Status fromString(String value) {
        if (value.equals(Status.OK.getValue())) {
            return Status.OK;
        } else if (value.equals(Status.ERROR.getValue())) {
            return Status.ERROR;
        }
        return Status.OK;
    }
}
