package com.pzuborev.vocabuary;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;


public class Word {

    private static final String JSON_ORIGINAL_WORD = "originalWord";
    private static final String JSON_WORD_TRANSLATION = "wordTranslation";
    private static final String JSON_ID = "id";
    private static final String JSON_EXAMPLE = "example";
    private static final String TAG = "Word";
    private static final String JSON_TRANSCRIPTION = "transcription";

    private UUID mId;
    private String mOriginalWord;
    private String mWordTranslation;
    private String mExample;
    private String mTranscription;

    public Word(String mOriginalWord, String mWordTranslation) {
        this.mOriginalWord = mOriginalWord;
        this.mWordTranslation = mWordTranslation;
        mId = UUID.randomUUID();
    }

    public Word(JSONObject jsonObject) throws JSONException {
        JSONObject jsonId = jsonObject.optJSONObject(JSON_ID);
        if (jsonId != null)
            mId = UUID.fromString(jsonId.toString());
        else
            mId = UUID.randomUUID();
        mOriginalWord = jsonObject.getString(JSON_ORIGINAL_WORD);
        mWordTranslation = jsonObject.getString(JSON_WORD_TRANSLATION);
        mExample = jsonObject.getString(JSON_EXAMPLE);
        mTranscription = (String) jsonObject.opt(JSON_TRANSCRIPTION);
    }

    public String getOriginalWord() {
        return mOriginalWord;
    }

    public void setOriginalWord(String originalWord) {
        this.mOriginalWord = originalWord;
    }

    public String getWordTranslation() {
        return mWordTranslation;
    }

    public void setWordTranslation(String wordTranslation) {
        this.mWordTranslation = wordTranslation;
    }

    public UUID getId() {
        return mId;
    }

    @Override
    public String toString() {
        return "Word{" +
                "mOriginalWord='" + mOriginalWord + '\'' +
                ", mWordTranslation='" + mWordTranslation + '\'' +
                '}';
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_ORIGINAL_WORD, getOriginalWord());
        jsonObject.put(JSON_WORD_TRANSLATION, getWordTranslation());
        jsonObject.put(JSON_ID, getId());
        jsonObject.put(JSON_EXAMPLE, getExample());
        jsonObject.put(JSON_TRANSCRIPTION, getTranscription());
        return jsonObject;
    }

    public String getExample() {
        return mExample;
    }

    public void setExample(String example) {
        mExample = example;
    }

    public void setTranscription(String transcription) {
        mTranscription = transcription;
    }

    public String getTranscription() {
        return mTranscription;
    }
}
