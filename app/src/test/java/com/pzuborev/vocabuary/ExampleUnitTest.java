package com.pzuborev.vocabuary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void httpTest() throws Exception {
//        org.apache.http.client.HttpClient client
        URL url = new URL("https://developers.lingvolive.com/api/v1/Translation?text=rough&srcLang=1033&dstLang=1049");

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Authorization", "Bearer ZXlKaGJHY2lPaUpJVXpJMU5pSXNJblI1Y0NJNklrcFhWQ0o5LmV5SmxlSEFpT2pFME9EWTVNVFUyT1RFc0lrMXZaR1ZzSWpwN0lrTm9ZWEpoWTNSbGNuTlFaWEpFWVhraU9qVXdNREF3TENKVmMyVnlTV1FpT2pNMU1pd2lWVzVwY1hWbFNXUWlPaUl4TldabE5USTVaQzFoTkdJMUxUUmlZemt0T0dGbE1TMHpPRFU0TWpBd01UUTNaV0lpZlgwLmhabldRYWhLQjZqTHd2czg2QWR4SXhxTjNBOTFJODBCX1B3T1lWd0Z2LUk=");
        int responseCode = urlConnection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            System.out.println("OK");
            InputStream stream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream("f:\\work\\AndroidStudioProjects\\Vocabulary\\out.txt");
            StringBuilder stringBuilder = new StringBuilder();
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = stream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                stringBuilder.append(buffer.toString());
            }
            stream.close();
            fileOutputStream.close();

            JSONArray jsonArray = (JSONArray) new JSONTokener(stringBuilder.toString()).nextValue();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject dic = (JSONObject) jsonArray.get(i);
                System.out.println(dic.get("Dictionary"));
            }


        } else {
            System.out.println("not OK");
            InputStream stream = urlConnection.getErrorStream();
            //TODO read stream if needed
        }

    }
}