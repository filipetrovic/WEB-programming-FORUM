package beans;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Database {

	private static ArrayList<User> users;
	private static ArrayList<SubForum> subforums;
	private static SubForum SelectedSubForum;
	private static Theme SelectedTheme;
	private static String CurrentComment;
	private static Theme selectedSavedTheme;
	private static Comment selectedSavedComment;
	

	
	

	

	static void init(){
	
		//products = new ArrayList<Product>();
				//reviews = new ArrayList<Review>();
		
		
				
		
		
		
		
		
		SelectedSubForum = new SubForum();
		SelectedSubForum.setThemes(new ArrayList<Theme>());
		
	}
	
	
	
	public static Comment getSelectedSavedComment() {
		return selectedSavedComment;
	}



	public static void setSelectedSavedComment(Comment selectedSavedComment) {
		Database.selectedSavedComment = selectedSavedComment;
	}



	public static Theme getSelectedSavedTheme() {
		return selectedSavedTheme;
	}



	public static void setSelectedSavedTheme(Theme selectedSavedTheme) {
		Database.selectedSavedTheme = selectedSavedTheme;
	}



	public static String getCurrentComment() {
		return CurrentComment;
	}

	public static void setCurrentComment(String currentComment) {
		CurrentComment = currentComment;
	}

	public static Theme getSelectedTheme() {
		return SelectedTheme;
	}

	public static void setSelectedTheme(Theme selectedTheme) {
		SelectedTheme = selectedTheme;
	}

	public static SubForum getSelectedSubForum() {
		return SelectedSubForum;
	}

	public static void setSelectedSubForum(SubForum selectedSubForum) {
		SelectedSubForum = selectedSubForum;
	}

	private static Database instance = null;
	public static Database getInstance() {
      if(instance == null) {
         instance = new Database();
         init();
      }
      return instance;
    }
	
	public static ArrayList<SubForum> getSubforums() {
		return subforums;
	}

	public static void setSubforums(ArrayList<SubForum> subforums) {
		Database.subforums = subforums;
	}

	public static ArrayList<User> getUsers() {
		return users;
	}

	public static void setUsers(ArrayList<User> users) {
		Database.users = users;
	}
	
	public static boolean addUser(User user){
		
		for(User u :users){
			if(user.getUsername().equals(u.getUsername())){
				System.out.println("Vec postoji korisnik sa takvim usernameon");
				return false;
			}
		}
		
		for(User u : users){
			if (u.getEmail().equals(user.getEmail())){
				System.out.println("Email or usrname already in use");
				return false;
			}
		}
		users.add(user);
		System.out.println("User registered successefully");
		return true;
	}
	
	public static User getUser(String email){
		for(User u : users){
			if(u.getEmail().equals(email)){
				return u;
			}
		}
		System.out.println("Ne postoji korisnik sa tim imejlon");
		return null;
	}
	
	//=-----------------------------------------------SERIJALIZACIJA=========================================================
	public static <T> void serialize(ArrayList<T> toSave,String filename){
		try
	      {
	         FileOutputStream fileOut =
	         new FileOutputStream(filename);
	         ObjectOutputStream out = new ObjectOutputStream(fileOut);
	         out.writeObject(toSave);
	         out.close();
	         fileOut.close();
	         System.out.printf("\nSerialized data is saved in "+filename+"\n");
	      }catch(IOException i)
	      {	
	    	  System.out.println("ferr");
	    	  i.printStackTrace();
	          
	      }
	}
	
	public static <T> ArrayList<T> deSerialize( String filename){
		ArrayList<T> ret = null; 
		try
	      {
	         FileInputStream fileIn = new FileInputStream(filename);
	         ObjectInputStream in = new ObjectInputStream(fileIn);
	         ret = (ArrayList<T>) in.readObject();
	         in.close();
	         fileIn.close();
	      }catch(IOException i)
	      {
	         System.out.println("FNF");
	         return null;
	      }catch(ClassNotFoundException c)
	      {
	         System.out.println("Employee class not found");
	         return null;
	      }
		
		  return ret;
	}
	
}
