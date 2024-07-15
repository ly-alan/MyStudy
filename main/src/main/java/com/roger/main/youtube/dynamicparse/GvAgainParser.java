package com.roger.main.youtube.dynamicparse;



import android.util.Log;

import com.roger.main.youtube.YoutubeHelper;
import com.roger.main.youtube.dynamicparse.entity.MethodValue;
import com.roger.main.youtube.dynamicparse.entity.SignSyntax;
import com.roger.main.youtube.dynamicparse.entity.UrlSyntax;

import java.util.ArrayList;
import java.util.List;

public class GvAgainParser extends AbstractCipherParser implements IParserHandler {
    private static final String TAG = GvAgainParser.class.getSimpleName();
    private UrlSyntax mUrlSyntax;

    @Override
    String parseUrl(String url) {
//        return YoutubeHelper.parseCipherUrl(url, true);
        Log.d(TAG, "using gvAgain parser");
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

            List<MethodValue> gvAgainInvokes = new ArrayList<>();
            gvAgainInvokes.add(new MethodValue("KQ", 49));
            gvAgainInvokes.add(new MethodValue("KQ", 20));
            gvAgainInvokes.add(new MethodValue("KQ", 25));
            gvAgainInvokes.add(new MethodValue("d3", 1));
            mUrlSyntax.setMethodInvokes(gvAgainInvokes);
        }

        return mUrlSyntax;
    }
}
