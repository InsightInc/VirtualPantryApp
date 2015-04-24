package com.example.virtualpantry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

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
public class Register extends Activity {
	JSONParser parser;
	private Button register;
	private Session s;
	private EditText first;
	private EditText last;
	private EditText email;
	private EditText password;
	private EditText confirm;
	private String f;
	private String l;
	private String e;
	private String p;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		parser = new JSONParser();
		s = Session.getSession();
		register = (Button) findViewById(R.id.RegistrationButton);
		first = (EditText)findViewById(R.id.firstEdit);
		last = (EditText)findViewById(R.id.lastEdit);
		email = (EditText)findViewById(R.id.emailEdit);
		password = (EditText)findViewById(R.id.passEdit);
		confirm = (EditText)findViewById(R.id.confirmEdit);
		register.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				f= first.getText().toString();
				l= last.getText().toString();
				e= email.getText().toString();
				p= password.getText().toString();
				if (p.compareTo(confirm.getText().toString()) != 0 || p.length() == 0){
					password.setText("");
					confirm.setText("");
					if(isEmailValid(email.getText())){
						Toast.makeText(getBaseContext(),"Passwords Don't match", Toast.LENGTH_LONG).show();
					}
					else{
						email.setText("");
						Toast.makeText(getBaseContext(),"Not an email format", Toast.LENGTH_LONG).show();
					}
				}
				else{
					if(!isEmailValid(email.getText())){
						email.setText("");
						Toast.makeText(getBaseContext(),"Not an email format", Toast.LENGTH_LONG).show();
					}
					else{
						new createProfile().execute();
					}
				}
			}
		});
	}
	boolean isEmailValid(CharSequence email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}
	
	class createProfile extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Register.this);
			pDialog.setMessage("Creating Profile...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("firstname", f));
			params.add(new BasicNameValuePair("lastname", l));
			params.add(new BasicNameValuePair("email", e));
			params.add(new BasicNameValuePair("password", p));
			JSONObject jObj = null;
			String json = parser.makeRequest("http://192.168.33.10/Virtual-Pantry/api/registerApp", "POST", params);
			try {
				jObj = new JSONObject(json);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(jObj.getInt("uid") != 0){
					try {
						s.setUid(jObj.getInt("uid"));
						s.setFirst(f);
						s.setFirst(l);
						s.setUsername(e);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
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
						Intent i = new Intent(Register.this, TabPages.class);
						startActivity(i);
						Toast.makeText(getBaseContext(),"Welcome to your Virtual Pantry " + s.getFirst(), Toast.LENGTH_LONG).show();
					}
					else{
						Toast.makeText(getBaseContext(),"Email Already Exists", Toast.LENGTH_LONG).show();
					}
				}
			});

		}
	}

}
