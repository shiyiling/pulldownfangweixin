package com.renny.simplebrowser.globe.http;


import com.renny.simplebrowser.globe.http.parambuilder.IParamBuilder;
import com.renny.simplebrowser.globe.http.reponse.IResultParse;
import com.renny.simplebrowser.globe.http.request.IHost;

/**
 * Created by LuckyCrystal on 2017/10/25.
 */

public interface IHttpCell {

    IHost getHost();

    String getUA();

    IResultParse getResultParse();

    IParamBuilder getParamBuilder();
}
