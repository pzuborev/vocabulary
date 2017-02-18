package com.pzuborev.vocabuary;


import android.os.Environment;
import android.util.Log;

import org.apache.commons.lang3.text.WordUtils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnitSet {
    private static final String VOCABULARY_DIR = "vocabulary";
    private static final String FILE_EXT = ".json";
    private static final String TAG = "UnitSet";
    public static UnitSet mInstance;

    private Map<String,String> mFileList;
    private String mDirectory;

    private UnitSet() {
        mFileList = new HashMap<>();

        mDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                + '/' + VOCABULARY_DIR;
        File vocabularyDir = new File(mDirectory);
        File[] files = vocabularyDir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(FILE_EXT);
            }
        });

        for (File f: files) {
            Log.d(TAG, f.getName());
            mFileList.put(unitNameFromFileName(f.getName()), f.getName());
        }

    }

    public static UnitSet get() {
        if (mInstance == null)
            mInstance = new UnitSet();
        return mInstance;
    }

    public List<String> getAvailableUnits() {
        List<String> list = new ArrayList<String>(mFileList.keySet());
        Collections.sort(list);
        return list;
    }

    public String getDirectory() {
        return mDirectory;
    }

    public String fileNameFromUnitName(String unitName) {
        if (unitName == null)
            return null;

        return mFileList.get(unitName);

    }

    private static String unitNameFromFileName(String fileName) {
        if (fileName == null)
            return null;

        return WordUtils.capitalize(fileName.replace("_", " " ).replace(FILE_EXT, ""));
    }
}
