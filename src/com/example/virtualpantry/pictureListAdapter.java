package com.example.virtualpantry;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class pictureListAdapter extends ArrayAdapter<String>{
	private final Activity context;
	 private final ArrayList<String> recipeName;
	 private final ArrayList<Bitmap> imageUrls;

    public pictureListAdapter(RecipeList recipeList, ArrayList<String> urls, ArrayList<Bitmap> stuff){
    	super(recipeList, R.layout.search_item, urls);
    	context = recipeList;
       recipeName = urls;
       imageUrls = stuff;
      }
    public View getView(int position,View view,ViewGroup parent) {
    	 LayoutInflater inflater=context.getLayoutInflater();
    	 View rowView=inflater.inflate(R.layout.search_item, null,true);
    	 
    	 TextView recipe = (TextView) rowView.findViewById(R.id.recipeName);
    	 ImageView picture = (ImageView) rowView.findViewById(R.id.foodImage);
    	 
    	 recipe.setText(Html.fromHtml(recipeName.get(position)));
    	 recipe.setLinkTextColor(Color.WHITE);
    	 recipe.setMovementMethod(LinkMovementMethod.getInstance());
    	 picture.setImageBitmap(imageUrls.get(position));
    	 return rowView;
    	 
    	 };

}
