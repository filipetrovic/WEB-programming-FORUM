package beans;

public class ThemePretragaUser {
	private String theme;
	private String SubForum;
	
	public ThemePretragaUser(){}
	
	public ThemePretragaUser(String theme,String SubForum){
		this.theme = theme;
		this.SubForum = SubForum;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public String getSubForum() {
		return SubForum;
	}

	public void setSubForum(String subForum) {
		SubForum = subForum;
	}
	
	
}
