package com.pzuborev.vocabuary;

import android.content.Context;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.UUID;

public class Vocabulary {
    private static Vocabulary mVocabulary;
    private Context mContext;
    private ArrayList<Word> mWords;

    private Vocabulary(Context context) {
        mContext = context;
        mWords = new ArrayList<Word>();

        for (int i = 0; i < 10; i++) {
            mWords.add(new Word("word #"+i, "translation #"+i));
        }
    }

    public Word findWord(UUID wordId){
        Word word = null;
        for (int i = 0; i < mWords.size(); i++) {
            word = mWords.get(i);
            if (word.getId().equals(wordId)) break;
        }
        return word;
    }

    public static Vocabulary getVocabulary(Context context){
        if (mVocabulary == null){
            mVocabulary = new Vocabulary(context.getApplicationContext());
        }
        return mVocabulary;
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
            if (mWords.get(i).getId().equals(word.getId())){
                result = i;
            }
        }
        return result;
    }
}
