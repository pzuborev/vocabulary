package com.pzuborev.vocabuary.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ListFragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import java.util.Collections;
import java.util.Comparator;

public class WordListFragment extends ListFragment {
    private ArrayList<Word> mWords;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        sortWords();
        notifyDataChange();
    }

    private void notifyDataChange() {
        ((WordListAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_shuffle:
                shuffleWords();
                notifyDataChange();
                return true;
            case R.id.menu_item_sort:
                sortWords();
                notifyDataChange();
                return true;
            case R.id.menu_item_guess:
                if(mWords.size() > 0) {
                    shuffleWords();
                    Intent i = new Intent(getActivity(), VocabularyPagerActivity.class);
                    i.putExtra(WordFragment.P_WORD_ID, mWords.get(0).getId());
                    startActivity(i);
                }
                return true;
            case R.id.menu_item_swap_word_and_translation:
                if(mWords.size() > 0) {
                    swapOriginalWordsAndTranslation();
                    notifyDataChange();
                    return true;
                }
                return true;
            default:  return super.onOptionsItemSelected(item);
        }

    }

    private void swapOriginalWordsAndTranslation() {
        String str;
        for (Word w: mWords) {
            str = w.getOriginalWord();
            w.setOriginalWord(w.getWordTranslation());
            w.setWordTranslation(str);
        }
    }

    private void shuffleWords() {
        Collections.shuffle(mWords);
    }

    private void sortWords() {
        Collections.sort(mWords, new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getOriginalWord().toUpperCase().compareTo(o2.getOriginalWord().toUpperCase());
            }
        });
    }
}
