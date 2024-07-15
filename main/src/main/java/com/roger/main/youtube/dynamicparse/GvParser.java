package com.roger.main.youtube.dynamicparse;


import android.util.Log;

import com.roger.main.youtube.YoutubeHelper;
import com.roger.main.youtube.dynamicparse.entity.MethodValue;
import com.roger.main.youtube.dynamicparse.entity.SignSyntax;
import com.roger.main.youtube.dynamicparse.entity.UrlSyntax;

import java.util.ArrayList;
import java.util.List;

public class GvParser extends AbstractCipherParser implements IParserHandler {
    private static final String TAG = GvParser.class.getSimpleName();

    private UrlSyntax mUrlSyntax;

    @Override
    String parseUrl(String url) {
//        return YoutubeHelper.parseCipherUrl(url, false);
        Log.d(TAG, "using gv parser");
        return YoutubeHelper.parseConfigCipherUrl(url, this);
    }

    @Override
    public String parseRegex() {
        return DefaultParserProvider.provideDefaultRegex();
    }

    @Override
    public SignSyntax parseSign() {
        return DefaultParserProvider.provideDefaultSignSyntax();
    }

    @Override
    public UrlSyntax parseUrl() {
        if (mUrlSyntax == null) {
            mUrlSyntax = new UrlSyntax();

            mUrlSyntax.setPrefix(DefaultParserProvider.providerDefaultPrefix());

            List<MethodValue> gvInvokes = new ArrayList<>();
            gvInvokes.add(new MethodValue("gT", 71));
            gvInvokes.add(new MethodValue("KQ", 19));
            gvInvokes.add(new MethodValue("KQ", 22));
            gvInvokes.add(new MethodValue("KQ", 66));
            gvInvokes.add(new MethodValue("gT", 79));
            gvInvokes.add(new MethodValue("KQ", 36));
            mUrlSyntax.setMethodInvokes(gvInvokes);
        }

        return mUrlSyntax;
    }
}
