package com.pzuborev.vocabuary.lingvo.api;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LingvoApi {
    public static final int LANG_ENG = 1033;
    public static final int LANG_RUS = 1049;


    private static final String PROTOCOL = "https";
    private static final String DOMAIN = "developers.lingvolive.com";
    private static final String API_V1_TRANSLATION = "api/v1/Translation";
    private static final String API_V1_1_AUTHENTICATE = "api/v1.1/authenticate";
    private static final String TRANSCRIPTION_NODE = "Transcription";
    private static final String SOUND_NODE = "Sound";
    private static final String MARKUP = "Markup";
    private static final String DICTIONARY = "Dictionary";
    private static final String BODY = "Body";
    private static final String NODE = "Node";
    private static final String TEXT = "Text";
    private static final String FILE_NAME = "FileName";
    private static final String AUTHORIZATION = "Authorization";
    private static final String BASIC = "Basic";
    private static final String BEARER = "Bearer";

    private static final String API_KEY = "MTVmZTUyOWQtYTRiNS00YmM5LThhZTEtMzg1ODIwMDE0N2ViOjYwZjY3YWI1YWFhYzQzZTk5NmIxZDc5ZDE2ODVlM2Zl";
    private static final String TAG = "LingvoApi";

    private String mToken;
    private Date mTokenDate;

    private String getURLRequest(String function, Map<String, String> params) throws UnsupportedEncodingException {
        StringBuilder urlRequest = new StringBuilder(PROTOCOL + "://" + DOMAIN + "/" + function);
        if (!(params == null || params.isEmpty())) {
            urlRequest.append("?");
            int i = 0;
            for (String key : params.keySet()) {
                if (i++ != 0) urlRequest.append("&");
                urlRequest.append(URLEncoder.encode(key, "UTF-8"));
                urlRequest.append("=");
                urlRequest.append(URLEncoder.encode(params.get(key), "UTF-8"));
            }
        }
        return urlRequest.toString();
    }

    private String readBody(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder stringBuilder = new StringBuilder();
        String s = null;
        while ((s = reader.readLine()) != null) {
            stringBuilder.append(s);
        }
        return stringBuilder.toString();
    }

    public void authenticate() throws IOException, LingvoApiException {

        URL url = new URL(getURLRequest(API_V1_1_AUTHENTICATE, null));
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty(AUTHORIZATION, BASIC + " " + API_KEY);

        InputStream stream = urlConnection.getInputStream();
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            String tokenStr = readBody(urlConnection.getInputStream());
            if (tokenStr.equals("")) throw new LingvoApiException("Authentication failed. Empty token.");
            setToken(tokenStr);
        }
        else {
            throw new LingvoApiException("Authentication failed. " + urlConnection.getResponseMessage());
        }

    }

    public ArrayList<LingvoCard> readTranslation(String text, int srcLang, int dstLang) throws LingvoApiException, IOException, JSONException {
        Calendar c = Calendar.getInstance();
        if ((mToken == null) || mTokenDate.after(c.getTime())) {
            authenticate();
        }
        Map<String, String> params = new HashMap<>(3);
        params.put("text", text);
        params.put("srcLang", String.valueOf(srcLang));
        params.put("dstLang", String.valueOf(dstLang));

        URL url = new URL(getURLRequest(API_V1_TRANSLATION, params));

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty(AUTHORIZATION, BEARER + " " + mToken);

        int responseCode = urlConnection.getResponseCode();

        InputStream stream = urlConnection.getInputStream();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            return parseJSONTranslationCard(readBody(stream));
        } else {
            Log.d(TAG, "Request failed. " + urlConnection.getResponseMessage());
            throw new LingvoApiException("Request failed. " + urlConnection.getResponseMessage());
        }
    }

    private ArrayList<LingvoCard> parseJSONTranslationCard(String response) throws JSONException {
        ArrayList<LingvoCard> cards = new ArrayList<>();

        JSONTokener jst = new JSONTokener(response);
        JSONArray jsonArray = (JSONArray) jst.nextValue();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject dic = (JSONObject) jsonArray.get(i);
            LingvoCard card = new LingvoCard();
            cards.add(card);
            card.setDictionaryName((String) dic.get(DICTIONARY));
            JSONArray bodies = (JSONArray) dic.opt(BODY);
            if (bodies != null) {
                for (int j = 0; j < bodies.length(); j++) {
                    JSONObject body = (JSONObject) bodies.get(j);
                    JSONArray markups = (JSONArray) body.opt(MARKUP);
                    parseMarkups(card, markups);
                    JSONArray items = (JSONArray) body.opt("Items");
                    parseItems(card, items);
                }
            }
        }
        return cards;
    }

    private void parseItems(LingvoCard card, JSONArray items) throws JSONException {
        if (items == null) return;
        for (int k = 0; k < items.length(); k++) {
            JSONObject item = (JSONObject) items.get(k);
            JSONArray markups = (JSONArray) item.opt(MARKUP);
            parseMarkups(card, markups);
        }
    }

    private void parseMarkups(LingvoCard card, JSONArray markups) throws JSONException {
        if (markups == null) return;
        for (int k = 0; k < markups.length(); k++) {
            JSONObject markup = (JSONObject) markups.get(k);
            String node = (String) markup.get(NODE);

            switch (node) {
                case TRANSCRIPTION_NODE:
                    card.setTranscription((String) markup.get(TEXT));
                    break;
                case SOUND_NODE:
                    card.setFileName((String) markup.get(FILE_NAME));
                    break;
            }
            JSONArray markups2 = (JSONArray) markup.opt(MARKUP);
            parseMarkups(card, markups2);

        }

    }

    public void setToken(String token) {
        mToken = token;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, 24);
        c.add(Calendar.SECOND, -60*5);
        mTokenDate = c.getTime();
        Log.d(TAG, "new token = " + mToken);
        Log.d(TAG, "new token date = " + mTokenDate);
    }
}
