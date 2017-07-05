protected void onCreate(Bundle savedInstanceState)
{
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.ble_activity);

    runable = new sendRunable("init");
    thread = new Thread(runable);
}

class sendRunable implements Runnable{

    String sendData;
    @Override
    public void run() {
        HTTPPostQuery("http://YOUR_IP/RECEIVE_PHP", sendData);
    }
    sendRunable(String data){
        sendData = data;
    }
    public void update(String s){
        sendData=s;
    }
}

/*  http post method */
/** Method: Do the HTTP query according to hostURL with "post" method. */
public Map<String, String> HTTPPostQuery(String hostURL, String sendData) {
    // Declare a content string prepared for returning.
    String content = "";
    // Have an HTTP client to connect to the web service.
    HttpClient httpClient = new DefaultHttpClient();
    // Have an HTTP response container.
    HttpResponse httpResponse = null;
    // Have map container to store the information.
    Map<String, String> map = new HashMap<String, String>();

    // This try & catch is prepared for the IO exception in case.
    try {
        // Have a post method class with the query URL.
        HttpPost httpQuery = new HttpPost(hostURL);

        // Have a list of key-value pair container.
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        // Add the HTTP post arguments into the list.
        pairs.add(new BasicNameValuePair("value", sendData));
        // Assign the list as the arguments of post being UTF_8 encoding.
        httpQuery.setEntity(new UrlEncodedFormEntity(pairs, HTTP.UTF_8));

        // The HTTP client do the query and have the string type response.
        httpResponse = httpClient.execute(httpQuery);

        // Read the HTTP response content as an encoded string.
        content += EntityUtils.toString(httpResponse.getEntity());
    }
    // Catch the HTTP exception.
    catch(ClientProtocolException ex) {
        content = "ClientProtocolException:" + ex.getMessage();
    }
    // Catch the any IO exception.
    catch(IOException ex) {
        content = "IOException:" + ex.getMessage();
    }
    // The HTTP connection must be closed any way.
    finally    {
        httpClient.getConnectionManager().shutdown();
    }

    // Check the HTTP connection is executed or not.
    if (httpResponse != null) {
        // Put the status code with status key.
        map.put("status", Integer.toString(httpResponse.getStatusLine().getStatusCode()));
        // Put the response content with content key.
        map.put("content", content);
    }
    else {
        // Put the dummy with status key.
        map.put("status", "");
        // Put the dummy with content key.
        map.put("content", "");
    }

    // Return result.
    return map;
}
/* end of http post method*/
