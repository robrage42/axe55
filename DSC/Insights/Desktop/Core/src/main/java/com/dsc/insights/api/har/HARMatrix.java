package com.dsc.insights.api.har;

import com.pxg.commons.ads.MapList;
import com.pxg.web.DomainParser;
import com.pxg.web.URLCommon;

import java.util.ArrayList;
import java.util.List;

public class HARMatrix
{
    private List<String> inputs;
    private MapList<String, String> hostnames;
    private MapList<String, String> mimeTypes;
    private MapList<String, String> domains;

    public HARMatrix()
    {
        inputs = new ArrayList<String>();

        hostnames = new MapList<>();
        mimeTypes = new MapList<>();
        domains = new MapList<>();
    }

    public void append(HARSession session)
    {
        String inputName = session.getFileInput().getName();
        inputs.add(inputName);

        for (HARSessionCall nextCall: session.getCalls().values())
        {
            String requestURL = nextCall.getRequest().getUrl();

            String hostname = URLCommon.getInstance().parseHost(requestURL);
            String domain = DomainParser.getInstance().fromHostname(hostname);
            String mimeType = nextCall.getResponse().getContent().getMimeType();

            hostnames.append(hostname, inputName);
            mimeTypes.append(mimeType, inputName);
            domains.append(domain, inputName);
        }
    }

    public void export()
    {
        //Output the matrix
        for (String nextInput: inputs)
        {
            //Output by domain
        }
    }
}
