package com.example.virtualpantry;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class PantryActivity extends ListActivity{
	private EditText entry;
	private Session s;
	private JSONParser parse;
	private ListView lvname;
	private ImageButton submit;
	//private ImageButton scan;
	private ImageButton reload;
	private ArrayList<String> Items = new ArrayList<String>();
	private ArrayList<String> toastString = new ArrayList<String>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pantytab);
		//toastString = new ArrayList<String>();
		parse = new JSONParser();
		s = Session.getSession();
		lvname = (ListView) findViewById(android.R.id.list);
		new LoadPantry().execute();
		entry = (EditText) findViewById(R.id.addItem);
//		scan = (ImageButton) findViewById(R.id.scanItem);
		submit = (ImageButton) findViewById(R.id.addManually);
		reload = (ImageButton) findViewById(R.id.reloadPantry);
		reload.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Items.clear();
				toastString.clear();
				new LoadPantry().execute();
			}
		});
		lvname.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Toast.makeText(getBaseContext(),toastString.get(arg2), Toast.LENGTH_LONG).show();
				//Log.d("String of info", toastString.get(arg2));
			}
		});
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!(entry.getText().toString().length() < 1)){
					Items.clear();
					toastString.clear();
					new addItem().execute();
				}
			}
		});
//		scan.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//				Items.clear();
//				toastString.clear();
//				new LoadPantry().execute();
//			}
//		});
	}

	class LoadPantry extends AsyncTask<Void, Void, Void> {
		private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PantryActivity.this);
			pDialog.setMessage("Loading Pantry ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid",Integer.toString(s.getUid())));
			JSONArray jObj = null;
			String json = parse.makeRequest("http://192.168.33.10/Virtual-Pantry/api/getPantryList", "GET", params);
			try {
				jObj = new JSONArray(json);
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			for (int i = 0; i < jObj.length(); i++){
				try {
					String item = jObj.getString(i);
					item = item.substring(2, item.length()-2);
					Items.add(item);
					params.clear();
					params.add(new BasicNameValuePair("name", item));
					json = parse.makeRequest("http://192.168.33.10/Virtual-Pantry/api/getProductInfo", "GET", params);
					JSONObject jO = new JSONObject(json);
					String info = "PRODUCT INFO:\nCarbs: " + jO.getString("carb") + "g\nCalories: " + jO.getString("cal") + " cal\nFat: " + jO.getString("fat") + "g\nProtein: " + jO.getString("protien") + "g";
					toastString.add(info);
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
					lvname.setAdapter(new ArrayAdapter<String>(PantryActivity.this, android.R.layout.simple_list_item_1 , Items){
						@Override
						public View getView(int position, View convertView, ViewGroup parent) {
							View view =super.getView(position, convertView, parent);

							TextView textView=(TextView) view.findViewById(android.R.id.text1);

							/*YOUR CHOICE OF COLOR*/
							textView.setTextColor(Color.WHITE);

							return view;
						}
					});
					//                    ListAdapter adapter = new SimpleAdapter(
					//                            OutboxActivity.this, outboxList,
					//                            R.layout.outbox_list_item, new String[] { TAG_SUBJECT, TAG_TO, TAG_DATE },
					//                            new int[] { R.id.subject, R.id.to, R.id.date });
					//                    // updating listview
					//                    setListAdapter(adapter);
				}
			});

		}
	}
	class addItem extends AsyncTask<Void, String, Void> {
		//private ProgressDialog pDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			pDialog = new ProgressDialog(PantryActivity.this);
//			pDialog.setMessage("Loading Pantry ...");
//			pDialog.setIndeterminate(false);
//			pDialog.setCancelable(false);
//			pDialog.show();
		}
		@Override
		protected Void doInBackground(Void... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid",Integer.toString(s.getUid())));
			params.add(new BasicNameValuePair("name", entry.getText().toString()));
			parse.makeRequest("http://192.168.33.10/Virtual-Pantry/api/addProduct", "GET", params);
			return null;
		}
		@Override
		protected void onPostExecute(Void v) {
			// dismiss the dialog after getting all products
			//pDialog.dismiss();
			//updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed JSON data into ListView
					 * */
					entry.setText("");
					new LoadPantry().execute();
				}
			});

		}
	}
}

