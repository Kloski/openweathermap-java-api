package org.openweathermap.api.query;

public interface Query {
    String BASE_URL = "https://api.openweathermap.org/data/2.5";

    String QUESTION_MARK = "?";
    String AND = "&";

    String toStringRepresentation(String apiKey);

    Language getLanguage();

    void setLanguage(Language language);

    UnitFormat getUnitFormat();

    void setUnitFormat(UnitFormat unitFormat);
}
