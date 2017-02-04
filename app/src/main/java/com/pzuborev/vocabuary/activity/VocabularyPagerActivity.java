package com.pzuborev.vocabuary.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.pzuborev.vocabuary.R;
import com.pzuborev.vocabuary.Vocabulary;
import com.pzuborev.vocabuary.Word;
import com.pzuborev.vocabuary.fragment.WordFragment;

import java.util.UUID;

public class VocabularyPagerActivity extends FragmentActivity {
    private static final String TAG = "VocabularyPagerActivity";

    private ViewPager mViewPager;
    Vocabulary mVocabulary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVocabulary = Vocabulary.getVocabulary(this);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.view_pager);

        setContentView(mViewPager);

        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Word word = mVocabulary.getWord(position);
                return WordFragment.newInstance(word.getId());
            }

            @Override
            public int getCount() {
                return mVocabulary.wordsCount();
            }
        });

        UUID wordId = (UUID) getIntent().getSerializableExtra(WordFragment.P_WORD_ID);
        Word word = mVocabulary.findWord(wordId);
        mViewPager.setCurrentItem(mVocabulary.getPosition(word));

    }
}
