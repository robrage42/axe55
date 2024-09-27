package com.dsc.insights.har;

import com.dsc.insights.api.har.HARReader;
import com.dsc.insights.api.har.HARSession;
import com.dsc.insights.api.har.HARSessionCall;
import com.pxg.commons.text.TextCommon;
import com.pxg.dio.file.FileCommon;
import com.pxg.web.DomainParser;
import com.pxg.web.URLCommon;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.FileFilter;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class HARMatrixBuilder
{
    public static void main(String[] args)
    {
        HARMatrixBuilder builder = new HARMatrixBuilder();

        String rootDir = "D:\\CISA\\Research\\News\\Master\\";

//        File fileHAR = new File(rootDir, "az_maricopa_results_2.har");
//        builder.process(fileHAR);

        builder.build(rootDir, "domain_script_matrix.tab", "javascript");
    }

    public HARMatrixBuilder()
    {

    }

    public void build(String rootDir, String outputFilename, String mimeFilter)
    {
        File fileRoot = new File(rootDir);
        if (fileRoot.exists() == false)
        {
            return;
        }

        File[] harFiles = fileRoot.listFiles(new FileFilter()
        {
            @Override
            public boolean accept(File pathname)
            {
                String filename = pathname.getName().toLowerCase();
                return (filename.endsWith(".har"));
            }
        });

        File dirContent = new File(rootDir + "content\\");
        FileCommon.getInstance().makeDirectory(dirContent);

        Map<String, HARSession> sessions = new LinkedHashMap<>();
        List<String> domains = new ArrayList<>();

        int contentCount = 0;
        for (File nextFile: harFiles)
        {
            HARSession session = process(nextFile);
            if (session == null)
            {
                continue;
            }

            sessions.put(session.getFileInput().getName(), session);

            for (HARSessionCall nextCall: session.getCalls().values())
            {
                //Check for the mime type filter
                String mimeType = nextCall.getResponse().getContent().getMimeType();
                if (mimeType != null && mimeFilter != null)
                {
                    //Exclude any result that does not match the mimeFilter
                    if (mimeType.toLowerCase().contains(mimeFilter.toLowerCase()) == false)
                    {
                        continue;
                    }
                }

                String requestURL = nextCall.getRequest().getUrl();
                String filename = URLCommon.getInstance().parseFilename(requestURL);
                String hostname = URLCommon.getInstance().parseHost(requestURL);
                if (hostname != null)
                {
                    String domain = DomainParser.getInstance().fromHostname(hostname).toLowerCase();

                    if (domains.contains(domain) == false)
                    {
                        domains.add(domain);
                    }
                }

                //Write out the content
                String outputContent = nextCall.getResponse().getContent().getText();
                if (outputContent != null && outputContent.length() > 0)
                {
                    boolean isEncoded = Base64.isBase64(outputContent);
                    if (isEncoded == true)
                    {
                        byte[] byteDecoded = Base64.decodeBase64(outputContent);
                        outputContent = new String(byteDecoded, StandardCharsets.UTF_8);
                    }
                    contentCount++;
                    if (filename.length() > 250)
                    {
                        int length = filename.length();
                        filename = filename.substring(length - 250);
                    }
                    String uniqueOutputName = contentCount + "_" + TextCommon.getInstance().removeSpecial(filename, false, ".", "");
                    File fileContent = new File(dirContent, uniqueOutputName);
                    FileCommon.getInstance().writeFile(fileContent, outputContent, "UTF-8");
                }
            }
        }

        List<String> inputs = new ArrayList<>();
        inputs.addAll(sessions.keySet());
        inputs.sort((o1,o2) -> o1.compareTo(o2));

        domains.sort((o1, o2) -> o1.compareTo(o2));

        List<String> listOutput = new ArrayList<>();

        StringBuilder sbHeader = new StringBuilder();
        sbHeader.append("Domain");
        sbHeader.append("\t");
        for (String nextInput: inputs)
        {
            sbHeader.append(nextInput);
            sbHeader.append("\t");
        }
        listOutput.add(sbHeader.toString());

        for (String nextDomain: domains)
        {
            StringBuilder nextLine = new StringBuilder();
            nextLine.append(nextDomain);
            nextLine.append("\t");

            for (String nextInput: inputs)
            {
                HARSession nextSession = sessions.get(nextInput);

                int count = countHits(nextDomain, nextSession);
                nextLine.append(count);
                nextLine.append("\t");
            }
            listOutput.add(nextLine.toString());
        }

        File fileOutput = new File(rootDir, outputFilename);
        FileCommon.getInstance().writeFileLines(fileOutput, "UTF-8", listOutput);
    }

    public HARSession process(File fileHAR)
    {
        HARReader reader = new HARReader();
        HARSession session = reader.load(fileHAR);
        if (session == null)
        {
            System.out.println("Failed to load HAR Session from " + fileHAR.getName());
            return null;
        }

//        String inputName = fileHAR.getName();
//
//        for (HARSessionCall nextCall: session.getCalls().values())
//        {
//            String requestURL = nextCall.getRequest().getUrl();
//            String hostname = URLCommon.getInstance().parseHost(requestURL);
//            String mimeType = nextCall.getResponse().getContent().getMimeType();
//
//            if (mimeType.toLowerCase().contains("javascript"))
//            {
//                System.out.println(inputName + "\t" + hostname + "\t" + mimeType + "\t" + requestURL);
//            }
//        }

        return session;
    }

    private int countHits(String nextDomain, HARSession session)
    {
        int count = 0;
        for (HARSessionCall nextCall: session.getCalls().values())
        {
            String requestURL = nextCall.getRequest().getUrl();
            String hostname = URLCommon.getInstance().parseHost(requestURL);
            String domain = DomainParser.getInstance().fromHostname(hostname).toLowerCase();

            if (domain.equals(nextDomain) == true)
            {
                count++;
            }
        }
        return count;
    }
}
