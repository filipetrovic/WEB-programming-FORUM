package beans;

public class ThemePretraga {
	private String name;
	private String content;
	private String author;
	private String belongsToSubforum;
	
	public ThemePretraga(){}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getBelongsToSubforum() {
		return belongsToSubforum;
	}

	public void setBelongsToSubforum(String belongsToSubforum) {
		this.belongsToSubforum = belongsToSubforum;
	}
	
}
