package com.pzuborev.vocabuary.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pzuborev.vocabuary.R;
import com.pzuborev.vocabuary.Vocabulary;
import com.pzuborev.vocabuary.Word;

import org.w3c.dom.Text;

import java.util.UUID;

public class WordFragment extends Fragment {
    // constants
    public static final String P_WORD_ID = "com.pzuborev.vocabuary.fragment.WordFragment.WORD_ID";
    // class fields
    private Word mWord;
    private Vocabulary mVocabulary;
    // view fields
    private TextView mWordTextView;
    private TextView mTranslationTextView;
    private Button mShowTranslationButton;
    private TextView mExampleTextView;

    public static WordFragment newInstance(UUID wordId){
        WordFragment fragment = new WordFragment();
        if (wordId != null){
            Bundle arg = new Bundle();
            arg.putSerializable(P_WORD_ID, wordId);
            fragment.setArguments(arg);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVocabulary = Vocabulary.getVocabulary(getActivity());
        Bundle arg = getArguments();
        if (arg != null) {
            UUID wordId = (UUID) arg.getSerializable(P_WORD_ID);
            if (wordId != null){
                mWord = mVocabulary.findWord(wordId);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word, container, false);
        getActivity().setTitle(R.string.remember_the_word);

        mWordTextView = (TextView) view.findViewById(R.id.word_original_value);
        mWordTextView.setText(mWord.getOriginalWord());

        mTranslationTextView = (TextView) view.findViewById(R.id.word_translation);
        mTranslationTextView.setText(mWord.getWordTranslation());
        mTranslationTextView.setVisibility(View.INVISIBLE);

        mExampleTextView = (TextView) view.findViewById(R.id.word_example);
        mExampleTextView.setText(mWord.getExample());
        mExampleTextView.setVisibility(View.INVISIBLE);

        mShowTranslationButton = (Button) view.findViewById(R.id.btn_show_translation);
        if (mTranslationTextView.getText().equals(""))
            mShowTranslationButton.setEnabled(false);
        mShowTranslationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTranslationTextView.setVisibility(View.VISIBLE);
                mExampleTextView.setVisibility(View.VISIBLE);
                mShowTranslationButton.setEnabled(false);
            }
        });


       return view;
    }
}
