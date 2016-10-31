import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jack on 2016-10-31.
 */
public class QRHttpUtils {
    public static String getAvailableLanguage() throws IOException, URISyntaxException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder().setScheme("http").setHost("cd865df4.compilers.sphere-engine.com").setPath("/api/v3/languages").setParameter("access_token", "03a61ada9a5cb95161d67a49a1320133").build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return content;
        } finally {
            response.close();
        }
    }

    public static List<Pair<String, Integer>> getAvailableLanguages() throws IOException, URISyntaxException, ParseException {
        JSONParser parser = new JSONParser();
        ArrayList<Pair<String, Integer>> availableLanguageList = new ArrayList<Pair<String, Integer>>();
        JSONObject object = (JSONObject) parser.parse(getAvailableLanguage());
        System.out.println(object.keySet().toArray()[0]);
        for (int i = 0; i < object.keySet().toArray().length; i++) {
            int key = Integer.parseInt(object.keySet().toArray()[i].toString());
            String value = object.get(String.valueOf(key)).toString();
            availableLanguageList.add(new ImmutablePair<String, Integer>(value, key));
        }
        return availableLanguageList;
    }

    public static String createSubmissionQuery(String sourceCodeContent, int compileLanguageCode, String compileInputContent) {
        JSONObject submissionQuery = new JSONObject();
        submissionQuery.put("sourceCode", sourceCodeContent);
        submissionQuery.put("language", compileLanguageCode);
        submissionQuery.put("input", compileInputContent);
        return submissionQuery.toJSONString();
    }

    public static String postSubmissionQuery(String submissionQuery) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder().setScheme("http").setHost("cd865df4.compilers.sphere-engine.com").setPath("/api/v3/submissions").setParameter("access_token", "03a61ada9a5cb95161d67a49a1320133").build();
        HttpPost httpPost = new HttpPost(uri);
        StringEntity entity = new StringEntity(submissionQuery);
        httpPost.addHeader("content-type", "application/json");
        httpPost.setEntity(entity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
//            System.out.println(response.getStatusLine());
            HttpEntity entity2 = response.getEntity();
            String content = EntityUtils.toString(entity2);
//            System.out.println(content);
            EntityUtils.consume(entity);
            return content;
        } finally {
            response.close();
        }
    }

    public static String getSubmissionResult(String id) throws URISyntaxException, IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        URI uri = new URIBuilder().setScheme("http").setHost("cd865df4.compilers.sphere-engine.com").setPath("/api/v3/submissions/" + id).setParameter("access_token", "03a61ada9a5cb95161d67a49a1320133").setParameter("withInput", "true").setParameter("withOutput", "true").setParameter("withStderr", "true").setParameter("withCmpinfo", "true").build();
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            System.out.println(response.getStatusLine());
            HttpEntity entity = response.getEntity();
            String content = EntityUtils.toString(entity);
            EntityUtils.consume(entity);
            return content;
        } finally {
            response.close();
        }
    }

    public static boolean isExecuteDone(String result) throws ParseException {
        JSONObject object = (JSONObject) new JSONParser().parse(result);
        String status = object.get("status").toString();
        System.out.println("statusss: " + status);
        if (status.equals("0")) {
            return true;
        } else {
            return false;
        }
    }
}
