package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Comment implements Serializable {
	private String belongsToTheme;
	private String idKomentara = "";
	private String Author;
	private String date;
	private String parentComment;
	private boolean logickiObrisan = false;
	private boolean vecGlasano = false;
	private String text;
	private int likes;
	private int dislikes;
	private boolean edited;
	private ArrayList<Comment> childrenComments;
	
	public Comment(Comment c){
		this.belongsToTheme = c.belongsToTheme;
		this.idKomentara = c.idKomentara;
		this.Author = c.Author;
		this.date = c.date;
		this.parentComment = c.parentComment;
		this.logickiObrisan = c.logickiObrisan;
		this.vecGlasano = c.vecGlasano;
		this.text = c.text;
		this.likes = c.likes;
		this.dislikes = c.dislikes;
		this.edited = c.edited;
		this.childrenComments = c.childrenComments;
	}
	
	
	public boolean isVecGlasano() {
		return vecGlasano;
	}
	public void setVecGlasano(boolean vecGlasano) {
		this.vecGlasano = vecGlasano;
	}
	public String getIdKomentara() {
		return idKomentara;
	}
	public void setIdKomentara(String idKomentara) {
		this.idKomentara = idKomentara;
	}
	public boolean isLogickiObrisan() {
		return logickiObrisan;
	}
	public void setLogickiObrisan(boolean logickiObrisan) {
		this.logickiObrisan = logickiObrisan;
	}
	
	
	public Comment(){}
	public String getBelongsToTheme() {
		return belongsToTheme;
	}
	public void setBelongsToTheme(String belongsToStory) {
		this.belongsToTheme = belongsToStory;
	}
	
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		this.Author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getParentComment() {
		return parentComment;
	}
	public void setParentComment(String parentComment) {
		this.parentComment = parentComment;
	}
	public ArrayList<Comment> getChildrenComments() {
		return childrenComments;
	}
	public void setChildrenComments(ArrayList<Comment> childrenComments) {
		this.childrenComments = childrenComments;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
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
	public boolean isEdited() {
		return edited;
	}
	public void setEdited(boolean edited) {
		this.edited = edited;
	}
	
}
