package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Theme implements Serializable {
	private String belongsToSubforum;
	private String name; //naslov teme je jedinstven u okviru podforuma
	private Type type; 
	private String Author;
	private ArrayList<Comment> comments;
	private String content; //odnosi se na sadrzaj teme sta god to bilo;
	private String date;
	private int likes;
	private int dislikes;
	private int brojPoseta = 0;
	private boolean vecGlasano;
	
	
	
	public Theme(Theme t){
		this.belongsToSubforum = t.belongsToSubforum;
		this.name = t.name;
		this.type = t.type;
		this.Author = t.Author;
		this.comments = t.comments;
		this.content = t.content;
		this.date = t.date;
		this.likes = t.likes;
		this.dislikes = t.dislikes;
		this.vecGlasano = t.vecGlasano;
		
	}
	
	public int getBrojPoseta() {
		return brojPoseta;
	}

	public void setBrojPoseta(int brojPoseta) {
		this.brojPoseta = brojPoseta;
	}

	public boolean isVecGlasano() {
		return vecGlasano;
	}

	public void setVecGlasano(boolean vecGlasano) {
		this.vecGlasano = vecGlasano;
	}

	public Theme(){}

	public String getBelongsToSubforum() {
		return belongsToSubforum;
	}

	public void setBelongsToSubforum(String belongsToSubforum) {
		this.belongsToSubforum = belongsToSubforum;
	}

	public String getAuthor() {
		return Author;
	}

	public void setAuthor(String author) {
		Author = author;
	}

	public String getName() {
		return name;
	}
	public void setName(String title) {
		this.name = title;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	public ArrayList<Comment> getComments() {
		return comments;
	}
	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
}
