package com.pzuborev.vocabuary.activity;

import android.support.v4.app.Fragment;

import com.pzuborev.vocabuary.fragment.WordListFragment;


public class WordListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment CreateFragment() {
        return new WordListFragment();
    }
}
