package com.example.virtualpantry;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class RecipeList extends ListActivity{
	private ListView list;
	private Session s;
	private JSONParser parse;
	private EditText searchBar;
	private TextView results;
	private ImageButton enter;
	private ArrayList<String> recipes = new ArrayList<String>();
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<Bitmap> pictures = new ArrayList<Bitmap>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchtab);
		list = (ListView)findViewById(android.R.id.list);
		parse = new JSONParser();
		s = Session.getSession();
		searchBar = (EditText)findViewById(R.id.searchBar);
		enter = (ImageButton)findViewById(R.id.searchButton);
		results = (TextView)findViewById(R.id.results);
		enter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				recipes.clear();
				String search = searchBar.getText().toString();
				searchBar.setText("");
				results.setText("Results for " + search + ":");
				new downloadRecipes().execute(search);
				

			}
		});
	}
	private class downloadRecipes extends AsyncTask<String, Void, Void> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RecipeList.this);
			pDialog.setMessage("Getting recipes ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected Void doInBackground(String ...args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid",Integer.toString(s.getUid())));
			params.add(new BasicNameValuePair("query", args[0]));
			JSONArray jobj = null;
			String json = parse.makeRequest("http://192.168.33.10/Virtual-Pantry/api/getRecipes", "GET", params);
			Log.d("Returned", json);
			try {
				jobj = new JSONArray(json);
				for (int i = 0; i < jobj.length(); i++){
					String innerText = jobj.getString(i);
					JSONArray temp = new JSONArray(innerText);
					JSONObject temp1 = new JSONObject(temp.getString(0));
					JSONArray names = temp1.names();
					recipes.add("<a href='" + temp1.getString(names.getString(0))+"'>"+names.getString(0)+"</a>");
					urls.add(temp.getString(1));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < urls.size(); i++){
				try {
					InputStream in = new java.net.URL(urls.get(i)).openStream();
					pictures.add(BitmapFactory.decodeStream(in));
				} catch (Exception e) {
					Log.e("Error", e.getMessage());
					e.printStackTrace();
				}
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void v) {
			pDialog.dismiss();
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					pictureListAdapter adapter=new pictureListAdapter(RecipeList.this, recipes, pictures);
					list.setAdapter(adapter);
				}
			});
		}
	}
}
