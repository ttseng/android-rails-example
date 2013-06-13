package com.mgm.phobos;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class AddProjectView extends Activity {
    
    private static final String TAG = "AddProjectView";

    private EditText projectNameView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project_layout);
        View addProjectButton = findViewById(R.id.add_project_button);
        projectNameView = (EditText) findViewById(R.id.edit_name_of_project);
        
        addProjectButton.setOnClickListener(new View.OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Log.d(TAG, "clicked add project");
                String projectName = projectNameView.getText().toString();
                new postProject().execute(projectName);
            }
        });
    }
        
    public class postProject extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String...params){
            DefaultHttpClient client = new DefaultHttpClient();
            Log.d(TAG, "in postProject");
            // Add the url of your project to the HTTPPost call below:
            HttpPost post = new HttpPost("");
            
            JSONObject holder = new JSONObject();
            JSONObject projectObj = new JSONObject();
            String name = params[0];
        
            try {
                
                Log.d(TAG, "attempting to post project");
                projectObj.put("name", name);
                Log.d(TAG, "projectName: " + name);
                
                holder.put("project", projectObj);
                
                Log.e("Event JSON", "Event JSON = "+ holder.toString());
                
                StringEntity se = new StringEntity(holder.toString());
                post.setEntity(se);
                post.setHeader("Content-Type","application/json");
                    
                } catch (UnsupportedEncodingException e) {
                    Log.e("Error",""+e);
                    e.printStackTrace();
                } catch (JSONException js) {
                    js.printStackTrace();
                }
            
                HttpResponse response = null;
                
                try {
                    response = client.execute(post);
                } catch (ClientProtocolException e) {
                    e.printStackTrace();
                    Log.e("ClientProtocol",""+e);
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("IO",""+e);
                    return null;
                }
            
                HttpEntity result = response.getEntity();
                
                if (result != null) {
                    try {
                        result.consumeContent();
                    } catch (IOException e) {
                        Log.e("IO E",""+e);
                            e.printStackTrace();
                        }
                    }
                   return null;    
                }
        protected void onPostExecute(){
        }
   }
}
        
