package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class SubForum implements Serializable {
	private String name;
	private String description;
	private String image;
	private String rules; //spisak pravila sta god to bilo
	private String mainModerator;
	private ArrayList<User> moderators;
	private ArrayList<String> moderatorsHelp;
	private ArrayList<Theme> themes;
	
	public ArrayList<Theme> getThemes() {
		return themes;
	}

	public void setThemes(ArrayList<Theme> themes) {
		this.themes = themes;
	}

	public ArrayList<String> getModeratorsHelp() {
		return moderatorsHelp;
	}

	public void setModeratorsHelp(ArrayList<String> moderatorsHelp) {
		this.moderatorsHelp = moderatorsHelp;
	}

	public SubForum(String a, String b, String c){
		this.name=a;
		this.description=b;
		this.image=c;
		
	}
	
	public SubForum(){}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	
	public String getRules() {
		return rules;
	}
	public void setRules(String rules) {
		this.rules = rules;
	}
	
	public String getMainModerator() {
		return mainModerator;
	}

	public void setMainModerator(String mainModerator) {
		this.mainModerator = mainModerator;
	}

	public ArrayList<User> getModerators() {
		return moderators;
	}
	public void setModerators(ArrayList<User> moderators) {
		this.moderators = moderators;
	}
}
