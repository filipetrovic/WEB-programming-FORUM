package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable{
	private String username;
	private String password;
	private String name;
	private String surname;
	private String phone;
	private String email;
	private Role role;
	private String date;
	
	private ArrayList<SubForum> followedSubforums = new ArrayList<SubForum>();
	private ArrayList<Theme> savedThemes = new ArrayList<Theme>();
	private ArrayList<Comment> savedComments = new ArrayList<Comment>();
	private ArrayList<Message> messages = new ArrayList<Message>();
	private ArrayList<Zalba> zalbe = new ArrayList<Zalba>();
	
	private ArrayList<Theme> glasaoTeme = new ArrayList<Theme>();
	private ArrayList<Comment> glasaoKomentare = new ArrayList<Comment>();
	
	
	
	public ArrayList<Zalba> getZalbe() {
		return zalbe;
	}

	public void setZalbe(ArrayList<Zalba> zalbe) {
		this.zalbe = zalbe;
	}
	public ArrayList<Comment> getGlasaoKomentare() {
		return glasaoKomentare;
	}

	public void setGlasaoKomentare(ArrayList<Comment> glasaoKomentare) {
		this.glasaoKomentare = glasaoKomentare;
	}

	public ArrayList<Message> getMessages() {
		return messages;
	}

	public void setMessages(ArrayList<Message> messages) {
		this.messages = messages;
	}

	public ArrayList<SubForum> getFollowedSubforums() {
		return followedSubforums;
	}

	public void setFollowedSubforums(ArrayList<SubForum> followedSubforums) {
		this.followedSubforums = followedSubforums;
	}

	public ArrayList<Theme> getSavedThemes() {
		return savedThemes;
	}

	public void setSavedThemes(ArrayList<Theme> savedStories) {
		this.savedThemes = savedStories;
	}

	public ArrayList<Theme> getGlasaoTeme() {
		return glasaoTeme;
	}

	public void setGlasaoTeme(ArrayList<Theme> glasaoTeme) {
		this.glasaoTeme = glasaoTeme;
	}

	public ArrayList<Comment> getSavedComments() {
		return savedComments;
	}

	public void setSavedComments(ArrayList<Comment> savedComments) {
		this.savedComments = savedComments;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public User(){}
	
	public User(String a,String b,String c, String d,String e, String f){
		this.username = a;
		this.password = b;
		this.name = c;
		this.surname = d;
		this.phone = e;
		this.email = f;
		System.out.println("EAE BRE U KURAC");
		
		
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
