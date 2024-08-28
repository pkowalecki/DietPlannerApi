package pl.kowalecki.dietplannerrestapi.model.enums;

public enum ErrorPole {
    TOKEN("");

    private String fieldName;

    ErrorPole(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }
}
