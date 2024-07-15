package com.roger.main.youtube.dynamicparse;


import com.roger.main.youtube.dynamicparse.entity.SignSyntax;
import com.roger.main.youtube.dynamicparse.entity.UrlSyntax;

public interface IParserHandler {

    String parseRegex();

    SignSyntax parseSign();

    UrlSyntax parseUrl();
}
