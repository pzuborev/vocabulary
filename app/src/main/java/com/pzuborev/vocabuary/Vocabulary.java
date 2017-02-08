package com.pzuborev.vocabuary;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

public class Vocabulary {
    private static final String TAG = "Vocabulary";
    private static Vocabulary mVocabulary;
    private final String mFileName = "vocabulary.json";
    private Context mContext;
    private ArrayList<Word> mWords;
    private VocabularyJSONSerializer mJSONSerializer;

    private Vocabulary(Context context) {
        mContext = context;
        mJSONSerializer = new VocabularyJSONSerializer(context, mFileName);
        try {
            mWords = mJSONSerializer.loadWord();
            sort();
        } catch (Exception e) {
            Log.e(TAG, "Error on vocabulary loading" + e.getMessage(), e);
            mWords = new ArrayList<>();
        }


    }

    public static Vocabulary getVocabulary(Context context) {
        if (mVocabulary == null) {
            mVocabulary = new Vocabulary(context.getApplicationContext());
        }
        return mVocabulary;
    }

    public Word findWord(UUID wordId) {
        Word word = null;
        for (int i = 0; i < mWords.size(); i++) {
            word = mWords.get(i);
            if (word.getId().equals(wordId)) break;
        }
        return word;
    }

    public int wordsCount() {
        return mWords.size();
    }

    public Word getWord(int position) {
        return mWords.get(position);
    }

    public ArrayList<Word> getWordsArray() {
        return mWords;
    }

    public int getPosition(Word word) {
        int result = -1;
        for (int i = 0; i < mWords.size(); i++) {
            if (mWords.get(i).getId().equals(word.getId())) {
                result = i;
            }
        }
        return result;
    }

    public void saveWords() {
        try {
            mJSONSerializer.saveWords(mWords);
        } catch (Exception e) {
            Log.e(TAG, "Не удалось сохранить данные в файл", e);
        }
    }

    public void sort(){
        Collections.sort(mWords, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getOriginalWord().toUpperCase().compareTo(o2.getOriginalWord().toUpperCase());
            }
        });
    }
}
