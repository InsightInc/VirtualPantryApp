package com.example.virtualpantry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.virtualpantry.Main_Page.verify;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class ProfileActivity extends Activity{
	private JSONParser parse;
	private Session s;
	private TextView name;
	private TextView header;
	private TextView email;
	private ListView allergies;
	private Button Logout;
	private ArrayList<String> Allergies = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profiletab);
        parse = new JSONParser();
        s = Session.getSession();
        header = (TextView)findViewById(R.id.header);
        header.setText(s.getFirst() + "'s Profile\n");
        name = (TextView)findViewById(R.id.nameView);
        name.setText("Name: " + s.getFirst() + " " + s.getLast() + "\n");
        email = (TextView)findViewById(R.id.emailV);
        email.setText("Email: " + s.getUsername() + "\n");
        allergies = (ListView)findViewById(R.id.aList);
        Logout = (Button)findViewById(R.id.logoutButton);
        Logout.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				s.destroySession();
				startActivity(new Intent(ProfileActivity.this, Main_Page.class));
			}
		});
//        new getAllergies().execute();
    }
	class getAllergies extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProfileActivity.this);
			pDialog.setMessage("Getting Allergies ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid",Integer.toString(s.getUid())));
			JSONArray jObj = null;
			String json = parse.makeRequest("http://192.168.33.10/Virtual-Pantry/api/getDiet", "GET", params);
			Log.d("HTTP response:", json);
			try {
				jObj = new JSONArray(json);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			for (int i = 0; i < jObj.length(); i++){
				try {
					String allergy = jObj.getString(i);
					allergy = allergy.substring(2, allergy.length()-2);
					Allergies.add(allergy);
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
					allergies.setAdapter(new ArrayAdapter<String>(ProfileActivity.this, android.R.layout.simple_list_item_1 , Allergies){
						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							View view =super.getView(position, convertView, parent);
							TextView textView=(TextView) view.findViewById(android.R.id.text1);
							textView.setTextColor(Color.WHITE);
							return view;
						}
					});
				}
			});

		}
	}
}
