package com.pzuborev.vocabuary.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pzuborev.vocabuary.R;
import com.pzuborev.vocabuary.Vocabulary;
import com.pzuborev.vocabuary.Word;
import com.pzuborev.vocabuary.lingvo.api.LingvoApi;
import com.pzuborev.vocabuary.lingvo.api.LingvoCard;

import java.util.ArrayList;
import java.util.UUID;


public class WordFragment extends Fragment {
    // constants
    public static final String TAG = "WordFragment";
    public static final String P_WORD_ID = "com.pzuborev.vocabuary.fragment.WordFragment.WORD_ID";
    // class fields
    private Word mWord;
    private Vocabulary mVocabulary;
    // view fields
    private TextView mWordTextView;
    private TextView mTranslationTextView;
    private Button mShowTranslationButton;
    private TextView mExampleTextView;
    private TextView mTranscriptionTextView;

    public static WordFragment newInstance(UUID wordId) {
        Log.d(TAG, "newInstance = " + wordId.toString());
        WordFragment fragment = new WordFragment();
        if (wordId != null) {
            Bundle arg = new Bundle();
            arg.putSerializable(P_WORD_ID, wordId);
            fragment.setArguments(arg);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mVocabulary = Vocabulary.getVocabulary(getActivity());
        Bundle arg = getArguments();
        if (arg != null) {
            UUID wordId = (UUID) arg.getSerializable(P_WORD_ID);
            Log.d(TAG, "onCreate = " + wordId.toString());
            if (wordId != null) {
                mWord = mVocabulary.findWord(wordId);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word, container, false);
        getActivity().setTitle(R.string.remember_the_word);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        mWordTextView = (TextView) view.findViewById(R.id.word_original_value);
        mWordTextView.setText(mWord.getOriginalWord());

        mTranslationTextView = (TextView) view.findViewById(R.id.word_translation);
        mTranslationTextView.setText(mWord.getWordTranslation());
        mTranslationTextView.setVisibility(View.INVISIBLE);

        mExampleTextView = (TextView) view.findViewById(R.id.word_example);
        mExampleTextView.setText(mWord.getExample());
        mExampleTextView.setVisibility(View.INVISIBLE);

        mTranscriptionTextView = (TextView) view.findViewById(R.id.word_transcription);
        setTranscription(mWord.getTranscription());
        mTranscriptionTextView.setVisibility(View.INVISIBLE);

        mShowTranslationButton = (Button) view.findViewById(R.id.btn_show_translation);
        if (mTranslationTextView.getText().equals(""))
            mShowTranslationButton.setEnabled(false);
        mShowTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTranslationTextView.setVisibility(View.VISIBLE);
                mExampleTextView.setVisibility(View.VISIBLE);
                mTranscriptionTextView.setVisibility(View.VISIBLE);
                mShowTranslationButton.setEnabled(false);
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.one_word_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                    return true;
                } else return false;
            case R.id.menu_one_item_load_extra:
                loadExtra();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void loadExtra() {
        Log.d(TAG, "loadExtra");
        (new FetchLingvoExtra()).execute(mWord.getOriginalWord());
    }

    private void setTranscription(String transcription) {

        String curTranscription = mWord.getTranscription();
        if (transcription != null
                && (curTranscription == null || !curTranscription.equals(transcription))) {
            mWord.setTranscription(transcription);
            Vocabulary.getVocabulary(getActivity()).saveWords();
        }

        if (mWord.getTranscription() != null)
            mTranscriptionTextView.setText("[" + mWord.getTranscription() + "]");
        else
            mTranscriptionTextView.setText("");

    }

    private class FetchLingvoExtra extends AsyncTask<String, Void, ArrayList<LingvoCard>> {

        @Override
        protected ArrayList<LingvoCard> doInBackground(String... params) {
            LingvoApi api = new LingvoApi();
            ArrayList<LingvoCard> cards = null;
            String text = params[0];
            try {
                cards = api.readTranslation(text, api.LANG_ENG, api.LANG_RUS);
                Log.d(TAG, "doInBackground finished success");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
                Log.d(TAG, "Loading transcription failed. " + e.getMessage());
            }
            return cards;
        }

        @Override
        protected void onPostExecute(ArrayList<LingvoCard> lingvoCards) {
            Log.d(TAG, "onPostExecute");
            if (lingvoCards != null) {
                for (int i = 0; i < lingvoCards.size(); i++) {
                    LingvoCard card = lingvoCards.get(i);
                    String transcription = card.getTranscription();
                    Log.d(TAG, "dic = " + card.getDictionaryName() + " transcription = " + card.getTranscription());
                    if (transcription != null) {
                        Log.d(TAG, "Loaded transcription = " + transcription);
                        setTranscription(transcription);
                        break;
                    }
                }
            } else Log.d(TAG, "Loading transcription: empty result");
        }
    }
}
