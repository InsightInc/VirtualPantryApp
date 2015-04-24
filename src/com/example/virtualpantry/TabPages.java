package com.example.virtualpantry;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class TabPages extends TabActivity{
	private static final String PANTRY_SPEC = "Pantry";
	private static final String RECIPE_SPEC = "Recipes";
	private static final String PROFILE_SPEC = "Profile";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabpage);
        
        TabHost tabHost = getTabHost();
        
        // Inbox Tab
        TabSpec pantrySpec = tabHost.newTabSpec(PANTRY_SPEC);
        // Tab Icon
        pantrySpec.setIndicator(PANTRY_SPEC);
        Intent pantryIntent = new Intent(TabPages.this, PantryActivity.class);
        // Tab Content
        pantrySpec.setContent(pantryIntent);
       
        // Outbox Tab
        TabSpec recipesSpec = tabHost.newTabSpec(RECIPE_SPEC);
        recipesSpec.setIndicator(RECIPE_SPEC);
        Intent outboxIntent = new Intent(TabPages.this, RecipeList.class);
        recipesSpec.setContent(outboxIntent);
        
        // Profile Tab
        TabSpec profileSpec = tabHost.newTabSpec(PROFILE_SPEC);
        profileSpec.setIndicator(PROFILE_SPEC);
        Intent profileIntent = new Intent(TabPages.this, ProfileActivity.class);
        profileSpec.setContent(profileIntent);
        
        // Adding all TabSpec to TabHost
        tabHost.addTab(pantrySpec); // Adding Inbox tab
        tabHost.addTab(recipesSpec); // Adding Outbox tab
        tabHost.addTab(profileSpec); // Adding Profile tab
    }
}
