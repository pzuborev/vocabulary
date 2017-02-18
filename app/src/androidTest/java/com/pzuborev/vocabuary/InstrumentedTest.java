package com.pzuborev.vocabuary;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.pzuborev.vocabuary.lingvo.api.LingvoApi;
import com.pzuborev.vocabuary.lingvo.api.LingvoCard;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.pzuborev.vocabuary.lingvo.api.LingvoApi.LANG_ENG;
import static com.pzuborev.vocabuary.lingvo.api.LingvoApi.LANG_RUS;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    public static final String TAG = "InstrumentedTest";

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.pzuborev.vocabuary", appContext.getPackageName());
    }

    @Test
    public void lingvoApi() throws Exception {
        LingvoApi lingvoApi = new LingvoApi();
            ArrayList<LingvoCard>  cards = lingvoApi.readTranslation("elaborate", LANG_ENG, LANG_RUS);
            Assert.assertNotNull("cards is null", cards);
            Assert.assertTrue("cards is empty", cards.size() > 0);
            System.out.println("-------------------------------------------------------");
            for (LingvoCard c: cards) {
                Log.d(TAG, "Transcription = " + c.getTranscription() + " " + c.getFileName());
            }

    }

    @Test
    public void testUnitList() throws Exception {
        List<String> unitList = UnitSet.get().getAvailableUnits();
        for (String s: unitList) {
            Log.d("UNIT_TEST", "_"+UnitSet.get().fileNameFromUnitName(s)+"_");
        }

    }
}
