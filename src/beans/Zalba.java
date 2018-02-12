package beans;

import java.io.Serializable;

public class Zalba implements Serializable {
	private String content;
	private String date;
	private Entitet entitet;
	private String author;
	
	private String komentarZaZalbu = null;
	private Theme temaZaZalbu = null;
	private SubForum SubForumZaZalbu = null;
	
	public String getKomentarZaZalbu() {
		return komentarZaZalbu;
	}

	public void setKomentarZaZalbu(String komentarZaZalbu) {
		this.komentarZaZalbu = komentarZaZalbu;
	}

	public Theme getTemaZaZalbu() {
		return temaZaZalbu;
	}

	public void setTemaZaZalbu(Theme temaZaZalbu) {
		this.temaZaZalbu = temaZaZalbu;
	}

	public SubForum getSubForumZaZalbu() {
		return SubForumZaZalbu;
	}

	public void setSubForumZaZalbu(SubForum subForumZaZalbu) {
		SubForumZaZalbu = subForumZaZalbu;
	}

	public Zalba(){}

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

	public Entitet getEntitet() {
		return entitet;
	}

	public void setEntitet(Entitet entitet) {
		this.entitet = entitet;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}
	
	
	
}
