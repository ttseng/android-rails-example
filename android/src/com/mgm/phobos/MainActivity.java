package com.mgm.phobos;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.app.ListActivity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {

    private static final String TAG = "MainActivity";
            
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Replace the url with the path to your application
        String url = ""
        Log.d("phobos", "performing get " + url );
        
        new NetworkTask().execute(url);
        
        // setContentView(R.layout.activity_main);
    }
    
    public class NetworkTask extends AsyncTask<String, Void, HttpResponse>{
        
        private ArrayList<myProjects> projectArrayList = null;
        String xmlResult;
       
        @Override
        protected HttpResponse doInBackground(String...params){
            String url = params[0];
            Log.d(TAG, url);
            
            HttpClient client = new DefaultHttpClient();
            HttpGet request;
            
            projectArrayList = new ArrayList<myProjects>();
           
            try {
                request = new HttpGet(new URI(url));
            } catch (URISyntaxException e1) {
                request = null;
                Log.d(TAG, "URI Syntax Exception");
                e1.printStackTrace();
            }
            
            try{
                return client.execute(request);
            } catch(IOException e){
                e.printStackTrace();
                return null;
            } finally{

            }
        }
        
        private ArrayList<myProjects> parseXMLString(String xmlString) {
            
            Log.d(TAG, "in parseXMLString");
            Log.d(TAG, xmlString);
            
            try {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                InputSource is = new InputSource();
                is.setCharacterStream(new StringReader(xmlString));

                Document doc = db.parse(is);
                NodeList nodes = doc.getElementsByTagName("project");
                Log.d(TAG, "nodes length: " + nodes.getLength());

                projectArrayList = new ArrayList<myProjects>(); 
                       
                //Iterate the events
                for (int i = 0; i < nodes.getLength(); i++) {
                    
                   Element element = (Element) nodes.item(i);          
                   projectArrayList.add(new myProjects());                  

                   NodeList projectName = element.getElementsByTagName("name");
                   Log.d(TAG, projectName.toString());
                   Element line = (Element) projectName.item(0);           
                   projectArrayList.get(i).name = getCharacterDataFromElement(line).trim();               
                }
                
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            
            return projectArrayList;   
            
        }
         
        @Override
        protected void onPostExecute(HttpResponse result){
            if(result!=null){
                Log.d(TAG, "in post execute");
                //Log.i( "phobos", "received " + getResponse(result.getEntity()) );
                xmlResult = getResponse(result.getEntity());
                projectArrayList = parseXMLString(xmlResult);
                setListAdapter(new ProjectAdapter(MainActivity.this, R.layout.row, projectArrayList));  
            }
            else{
                Log.i( "phobos", "got a null response" );
            }
        }
    }
        
    private String getResponse( HttpEntity entity )
    {
      String response = "";
    
      try
      {
        int length = ( int ) entity.getContentLength();
        StringBuffer sb = new StringBuffer( length );
        InputStreamReader isr = new InputStreamReader( entity.getContent(), "UTF-8" );
        char buff[] = new char[length];
        int cnt;
        while ( ( cnt = isr.read( buff, 0, length - 1 ) ) > 0 )
        {
          sb.append( buff, 0, cnt );
        }
    
          response = sb.toString();
          isr.close();
      } catch ( IOException ioe ) {
        ioe.printStackTrace();
      }
    
      return response;
    }
            
    public static String getCharacterDataFromElement(Element e) {
        Node child = e.getFirstChild();
        if (child instanceof CharacterData) {
           CharacterData cd = (CharacterData) child;
           return cd.getData();
        }
        return "?"; //ListActivity will display a ? if a null value is passed to the Rails server
      }
    
    private class ProjectAdapter extends ArrayAdapter<myProjects>{

        private ArrayList<myProjects> items;

        public ProjectAdapter(Context context, int textViewResourceId, ArrayList<myProjects> items) {
                super(context, textViewResourceId, items);
                this.items = items;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
                View v = convertView;
                if (v == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    v = vi.inflate(R.layout.row, null);
                }
                myProjects o = items.get(position);
                if (o != null) {
                        TextView tt = (TextView) v.findViewById(R.id.toptext);
                        
                        if (tt != null) {
                              tt.setText("Name: "+ o.getProjectName());                            
                        }
                        
                }
                return v;
        }
    }
    
  }
