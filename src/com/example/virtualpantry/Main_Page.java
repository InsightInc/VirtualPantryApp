package com.example.virtualpantry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Main_Page extends Activity {
	private JSONParser jParse;
	private Button register;
	private Button login;
	private EditText username;
	private EditText password;
	private String uname;
	private String pass;
	private Session s;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main__page);
		jParse = new JSONParser();
		s = Session.getSession();
		username = (EditText)findViewById(R.id.emailGetter);
		username.setCursorVisible(false);
		password = (EditText)findViewById(R.id.passwordGetter);
		password.setCursorVisible(false);
		register = (Button) findViewById(R.id.registerButton);
		login = (Button) findViewById(R.id.loginButton);
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Main_Page.this, Register.class);
				startActivity(i);
			}
		});
		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				uname = username.getText().toString();
				pass = password.getText().toString();
				new verify().execute();
			}
		});
	}

	class verify extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Main_Page.this);
			pDialog.setMessage("Checking Credentials ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("email", uname));
			params.add(new BasicNameValuePair("password", pass));
			String json = jParse.makeRequest("http://192.168.33.10/Virtual-Pantry/api/loginApp", "POST", params);
			JSONObject jObj = null;
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(jObj != null){
				try {
					s.setUid(jObj.getInt("uid"));
					s.setFirst(jObj.getString("fname"));
					s.setLast(jObj.getString("lname"));
					s.setUsername(uname);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void v) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();

			//updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					if(s.getUid() !=  0){
						Intent i = new Intent(Main_Page.this, TabPages.class);
		            	startActivity(i);
		            	Toast.makeText(getBaseContext(),"Welcome to your Virtual Pantry " + s.getFirst(), Toast.LENGTH_LONG).show();
					}
					else{
						password.setText("");
						Toast.makeText(getBaseContext(),"Not the correct Email/Password Combo", Toast.LENGTH_LONG).show();
					}
				}
			});

		}
	}
	
}
