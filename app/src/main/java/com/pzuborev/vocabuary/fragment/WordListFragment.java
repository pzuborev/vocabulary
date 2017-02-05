package com.pzuborev.vocabuary.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.pzuborev.vocabuary.R;
import com.pzuborev.vocabuary.Vocabulary;
import com.pzuborev.vocabuary.Word;
import com.pzuborev.vocabuary.activity.VocabularyPagerActivity;

import java.util.ArrayList;

public class WordListFragment extends ListFragment {
    private ArrayList<Word> mWords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mWords = Vocabulary.getVocabulary(getActivity()).getWordsArray();
        setListAdapter(new WordListAdapter(getActivity()));

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Word word = ((WordListAdapter) getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), VocabularyPagerActivity.class);
        i.putExtra(WordFragment.P_WORD_ID, word.getId());
        startActivity(i);
    }

    private class WordListAdapter extends ArrayAdapter<Word> {

        public WordListAdapter(Context context) {
            super(context, 0, mWords);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.fragment_list_item, null);
            }
            Word word = getItem(position);
            TextView originalWord = (TextView) convertView.findViewById(R.id.list_item_original_word);
            originalWord.setText(word.getOriginalWord());
            TextView wordTranslation = (TextView) convertView.findViewById(R.id.list_item_word_translation);
            wordTranslation.setText(word.getWordTranslation());
            return convertView;
        }
    }
}
