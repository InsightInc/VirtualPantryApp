package com.example.virtualpantry;

import java.util.ArrayList;

public class Session {
	private static Session session = null;
	private String username;
	private String first;
	private String last;
	private int uid;
	private ArrayList<String> Allergies = null;
	private Session(){
		username = null;
		first = null;
		last = null;
		uid = 0;
	}
	
	public static Session getSession(){
		if(session == null){
			session = new Session();
		}
		return session;
	}
	public void destroySession(){
		session = null;
		username = null;
		first = null;
		last = null;
		uid = -1;
		Allergies = null;
		session = null;
	}
	
	public void setUsername(String user){
		username = user;
	}
	public String getUsername(){
		if(username == null){
			return "User";
		}
		return username;
	}
	
	public void setFirst(String user){
		first = user;
	}
	public String getFirst(){
		if(first == null){
			return "John";
		}
		return first;
	}
	
	public void setLast(String user){
		last = user;
	}
	public String getLast(){
		if(last == null){
			return "Doe";
		}
		return last;
	}
	
	public void setUid(int user){
		uid = user;
	}
	public int getUid(){
		if(uid == -1){
			return 1;
		}
		return uid;
	}
	
	public void addAllergies(String allergy){
		if (Allergies == null){
			Allergies = new ArrayList<String>();
		}
		Allergies.add(allergy);
	}
	public ArrayList<String> getAllergies(){
		if(Allergies == null){
			return new ArrayList<String>();
		}
		return Allergies;
	}
	public void clearAllergies(){
		Allergies = null;
	}
}
