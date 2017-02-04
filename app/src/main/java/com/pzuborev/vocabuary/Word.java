package com.pzuborev.vocabuary;


import java.util.UUID;

public class Word {

    private UUID id;
    private String value;
    private String translation;

    public Word(String value, String translation) {
        this.value = value;
        this.translation = translation;
        id = UUID.randomUUID();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Word{" +
                "value='" + value + '\'' +
                ", translation='" + translation + '\'' +
                '}';
    }
}
