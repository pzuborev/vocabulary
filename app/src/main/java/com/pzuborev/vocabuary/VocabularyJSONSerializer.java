package com.pzuborev.vocabuary;


import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

public class VocabularyJSONSerializer {
    private static final String TAG = "VocabularyJSONSerializer";

    private final String mDirectory;
    private Context mContext;
    private String mFileName;

    public VocabularyJSONSerializer(Context context, String fileName, String directory) {
        mContext = context;
        mDirectory = directory;
        mFileName = fileName;
    }

    public ArrayList<Word> loadWord() throws IOException, JSONException {
        ArrayList<Word> words = new ArrayList<>();

        BufferedReader reader = null;
        try {
            FileInputStream fileStream = new FileInputStream(getFilePath());
            reader = new BufferedReader(new InputStreamReader(fileStream));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonString.append(line);
            }

            JSONArray jsonArray = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
            for (int i = 0; i < jsonArray.length(); i++) {
                words.add(new Word(jsonArray.getJSONObject(i)));
            }

        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return words;
    }

    public void saveWords(ArrayList<Word> words) throws JSONException, IOException {
        JSONArray jsonArray = new JSONArray();

        for (Word w : words) {
            jsonArray.put(w.toJSON());
        }


        Writer writer = null;
        try {
            try {
                FileOutputStream fileStream = new FileOutputStream(getFilePath());

                writer = new OutputStreamWriter(fileStream);
                writer.write(jsonArray.toString());
                writer.flush();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "File not found ", e);
            }
        } finally {
            if (writer != null) writer.close();
        }
    }

    private String getFilePath() {
        return mDirectory + "/" + mFileName;
    }
}

