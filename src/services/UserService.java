package services;



import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_ADDPeer;

import beans.Comment;
import beans.Database;
import beans.Entitet;
import beans.Message;
import beans.Role;
import beans.SubForum;
import beans.SubForumPretraga;
import beans.Theme;
import beans.ThemeLikeComparator;
import beans.ThemePretraga;
import beans.ThemePretragaUser;
import beans.Type;
import beans.User;
import beans.Zalba;
import javafx.scene.chart.PieChart.Data;

@Path("/userService")
public class UserService {
	private static final DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	
	@Context
	HttpServletRequest request;
	
	@GET
	@Path("/init")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean init(){
		System.out.println("Pozvao init");
		Database.getInstance();
		
		
		
		ArrayList<User> users = Database.getInstance().deSerialize(request.getServletContext().getRealPath("users.forum"));
		if (users == null){
			System.out.println("NE MORE");
			users = new ArrayList<User>();
			User u = new User();
			
			u.setPassword("admin");
			u.setUsername("admin");	
			u.setEmail("admin");
			u.setRole(Role.ADMINISTRATOR);
			users.add(u);
			
			u = new User();
			u.setPassword("mod");
			u.setEmail("mod");
			u.setUsername("mod");
			u.setRole(Role.MODERATOR);
			users.add(u);
			u = new User();
			u.setPassword("mod1");
			u.setEmail("mod1");
			u.setUsername("mod1");
			u.setRole(Role.MODERATOR);
			users.add(u); 
			Database.getInstance().setUsers(users);
		} else {
			for(User u : users){
				System.out.println(u.getEmail());
			}
			
			Database.getInstance().setUsers(users);
		}
		
		ArrayList<SubForum> podforumi =  Database.getInstance().deSerialize(request.getServletContext().getRealPath("SubForums.forum"));
		if(podforumi == null){
			podforumi = new ArrayList<SubForum>();
			Database.getInstance().setSubforums(podforumi);
		} else {
			for(SubForum s : podforumi){
				System.out.println(s.getName());
			}
			Database.getInstance().setSubforums(podforumi);
		}
		
		
		return true;
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean registerUser(User u){
		
		
		
		if (u != null) {
			if (u.getUsername() != null && u.getPassword() != null) {
				u.setRole(Role.REGUSER);
				u.setDate(sdf.format(new Date()));
				
				
				
				if (Database.getInstance().addUser(u)) {
					System.out.println(u.getName());
					request.getSession().setAttribute("user", u);
					Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
					return true;
				}
			}
		}
		return false;
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean loginUser(User u){
		try{
			User user = (User) request.getSession().getAttribute("user");
			if (user == null && u.getEmail() != null) {
				
				System.out.println("U : " + u.getEmail());
				user = Database.getInstance().getUser(u.getEmail());
				if (user == null) {
					System.out.println("Ovde");
					return false;
				} else {

					if (user.getPassword().equals(u.getPassword())) {
						request.getSession().setAttribute("user", user);
						System.out.println("Login");
						return true;
					}

				}
			}
			
			return false;
		} catch (Exception e){
			return false;
		}
		
	}
	
	@GET
	@Path("/loginAsGuest")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean loginAsGuest() {
		System.out.println("Guest");
		User u = new User();
		
		u.setRole(Role.GUEST);
		request.getSession().setAttribute("user",u );
		System.out.println("OVO PRINTA :" + ((User) request.getSession().getAttribute("user")).getRole());
		
		return true;
	}
	
	@GET
	@Path("/userLoggedIn")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean userLoggedIn() {
		try{
			System.out.println( ((User)request.getSession().getAttribute("user")).getEmail());
		} catch(Exception e){
			System.out.println("EEE");
			return false;
		}
			
		System.out.println("CHECK");
		if( (User)request.getSession().getAttribute("user") == null ){
			if(request.getSession().getAttribute("guest") == null){
				return false;
			}
		}
		
		return true;
	}
	
	
	@GET
	@Path("/logout")
	public boolean logout() {
		System.out.println("LOGOUT");
		request.getSession().invalidate();
		return true;
	}
	
	
	@POST
	@Path("/addSubForum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<SubForum> addSubForum(SubForum s){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			return null;
		}
		
		try{
			User us = (User)request.getSession().getAttribute("user");
			boolean korisnikJeModerator = false;
			boolean korisnikJeAdministrator = false;
			
			if( us.getRole() == Role.ADMINISTRATOR)
				korisnikJeAdministrator = true;
			if (us.getRole() == Role.MODERATOR){
				korisnikJeModerator = true;
			}
			
			if ( !korisnikJeAdministrator && !korisnikJeModerator){
				return null;
			}
		} catch (Exception e){
			return null;
		}
			
		
		
		
		System.out.println(s.getName());
		
		s.setMainModerator( ((User)request.getSession().getAttribute("user")).getEmail());
		s.setThemes(new ArrayList<Theme>());
		
		System.out.println("OVOVOVOVOV"+ s.getDescription());
		
		s.setModerators(new ArrayList<User>());
		
		
		
		for(String st : s.getModeratorsHelp()){
			for(User u : Database.getInstance().getUsers()){
				if (u.getEmail().equals(st.trim())){
					s.getModerators().add(u);
					
				}
			}
		} 
		
		for(SubForum sf : Database.getInstance().getSubforums()){
			if (sf.getName().equals(s.getName())){
				System.out.println("USAO U NULL");
				return null;
			}
		}
	
		Database.getInstance().getSubforums().add(s);
		
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return Database.getInstance().getSubforums();
	}
	
	@GET
	@Path("/getSubForums")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<SubForum> getSubForums() {
		ArrayList<SubForum> s = Database.getInstance().getSubforums();
		
		return s;
	}
	
	@GET
	@Path("/getAllUsers")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAllUsers() {
		
		ArrayList<User> korisniciKojiNisuAdmini = new ArrayList<User>();
		
		for(User u : Database.getInstance().getUsers()){
			if(u.getRole() != Role.ADMINISTRATOR){
				korisniciKojiNisuAdmini.add(u);
			}
		}
		
		return korisniciKojiNisuAdmini;
	}
	
	@GET
	@Path("/getAllUsers2")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getAllUsers2() {
		
		ArrayList<User> k = new ArrayList<User>();
		
		for(User u : Database.getInstance().getUsers()){
			
				k.add(u);
		
		}
		
		return k;
	}
	
	
	@GET
	@Path("/getModerators")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<User> getModerators() {
		ArrayList<User> s = new ArrayList<User>();
		
		for(User u : Database.getInstance().getUsers()){
			if(u.getRole() == Role.MODERATOR){
				s.add(u);
			}
		}
		
		return s;
	}
	
	@POST
	@Path("/goToSubForum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SubForum goToSubForum(String i){
		System.out.println( i);
		int index= Integer.parseInt(i);
		SubForum s = Database.getInstance().getSubforums().get(index);
		System.out.println("Selektovani podforum: " + s.getName() + s.getDescription());
		
		Database.getInstance().setSelectedSubForum(s);
		
		
		return s;
	}
	
	@GET
	@Path("/getSelectedSubForum")
	@Produces(MediaType.APPLICATION_JSON)
	public SubForum getSelectedSubForum() {
		SubForum s = Database.getInstance().getSelectedSubForum();
		System.out.println(s.getName());
		return s;
	}
	
	@POST
	@Path("/addTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addTheme(Theme t){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			return false;
		}
		
		System.out.println(t.getName() + t.getContent());
		
		t.setBelongsToSubforum(Database.getInstance().getSelectedSubForum().getName());
		
		if (t.getContent().contains(".jpg") || t.getContent().contains(".gif") || t.getContent().contains(".jpeg") ||t.getContent().contains(".bmp")){
			t.setType(Type.IMAGE);
		} else if (t.getContent().contains("www.") || t.getContent().contains("http")){
			t.setType(Type.LINK);
		} else {
			t.setType(Type.TEXT);
		}
		
		t.setAuthor( ((User)request.getSession().getAttribute("user")).getEmail() );
		
		t.setComments(new ArrayList<Comment>());
		
		Date date = new Date();
		
        System.out.println(sdf.format(date));
		t.setDate(sdf.format(date));
		
		t.setLikes(0);
		t.setDislikes(0);
		
		//smestis temu u selectedsubforum i u listu subforuma
		
		for(Theme them : Database.getInstance().getSelectedSubForum().getThemes()){
			if (them.getName().equals(t.getName())){
				return false;
			}
		}
		
		
		Database.getSelectedSubForum().getThemes().add(t);
		
		for(SubForum s : Database.getSubforums()){
			if (s.getName().equals(Database.getSelectedSubForum().getName())){
				s.setThemes(Database.getSelectedSubForum().getThemes());
			}
		}
		
		System.out.println("Ispis svih subforuma");
		
		for(SubForum s : Database.getSubforums()){
			System.out.println("SUBFORUM: " + s.getName());
			for(Theme tt : s.getThemes()){
				System.out.println("\tTheme: " + tt.getName());
			}
		}
		
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
	}
	
	@POST
	@Path("/editTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean editTheme(Theme t){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			return false;
		}
		
		for(Theme themes : Database.getInstance().getSelectedSubForum().getThemes()){
			if(themes.getName().equals(t.getName()) && (!themes.getName().equals(Database.getInstance().getSelectedTheme().getName()))){
				return false;
			}
		}
		
		
		for(SubForum s : Database.getInstance().getSubforums()){
			if (s.equals(Database.getSelectedSubForum())){
				for(Theme theme : Database.getSelectedSubForum().getThemes()){
					if (theme.equals(Database.getInstance().getSelectedTheme())){
						
						
						
						theme.setContent(t.getContent());
						theme.setName(t.getName());
						if (t.getContent().contains(".jpg") || t.getContent().contains(".gif") || t.getContent().contains(".jpeg") ||t.getContent().contains(".bmp")){
							theme.setType(Type.IMAGE);
						} else if (t.getContent().contains("www.") || t.getContent().contains("http")){
							theme.setType(Type.LINK);
						} else {
							theme.setType(Type.TEXT);
						}
					}
				}
			}
		}
		
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
	}
	
	@POST
	@Path("/deleteTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public SubForum deleteTheme(String i){
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			return null;
		}
		User u = (User)request.getSession().getAttribute("user");
		boolean korisnikJeAutorTeme = false;
		boolean korisnikJeOdgovorniModeratorPodforumaKojemTemaPripada = false;
		boolean korisnikJeAdministrator = false;
		
		
		//uzimam temu koju treba da obrisem
		System.out.println(i);
		int index= Integer.parseInt(i);
		Theme t =  new Theme();
		t = Database.getSelectedSubForum().getThemes().get(index);
		
		//provarea da li korisnik koji je trenutno prijavljen moze da obrise temu
		
		if (u.getEmail().equals(t.getAuthor()))
			korisnikJeAutorTeme = true;
		
		if(u.getRole() == Role.ADMINISTRATOR)
			korisnikJeAdministrator = true;
		
		if(u.getEmail().equals(Database.getSelectedSubForum().getMainModerator()))
			korisnikJeOdgovorniModeratorPodforumaKojemTemaPripada = true;
		
		for(User user : Database.getSelectedSubForum().getModerators()){
			if(user.getEmail().equals(u.getEmail()))
				korisnikJeOdgovorniModeratorPodforumaKojemTemaPripada = true;
		}	
		
		if(!korisnikJeAutorTeme && !korisnikJeOdgovorniModeratorPodforumaKojemTemaPripada && !korisnikJeAdministrator)
			return null;
		
		
		
		Database.getSelectedSubForum().getThemes().remove(index);
		Database.getInstance().setSelectedTheme(null);
		u = (User)request.getSession().getAttribute("user");
		
		for(SubForum s : u.getFollowedSubforums()){
			for(int j = 0; j< s.getThemes().size() ; j++){
				if(s.getThemes().get(j).getName().equals(t.getName())){
					s.getThemes().remove(j);
				}
			}
		}
		
		for(int j = 0; j < u.getGlasaoTeme().size() ;j++){
			if (u.getGlasaoTeme().get(j).getName().equals(t.getName())){
				u.getGlasaoTeme().remove(j);
			}
		}
		
		for(int j = 0; j < u.getSavedThemes().size() ;j++){
			if (u.getSavedThemes().get(j).getName().equals(t.getName())){
				u.getSavedThemes().remove(j);
			}
		}
		
		
		System.out.println("Obrisana tema");
		
		
		
		
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		
		
		return Database.getInstance().getSelectedSubForum();
	}
	
	@GET
	@Path("/obrisiSubForum/{id}")
	public ArrayList<SubForum> delS(@PathParam("id") String id){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			return null;
		}
		SubForum zaBrisanje = Database.getInstance().getSubforums().get(Integer.parseInt(id));
		User us = (User)request.getSession().getAttribute("user");
		
		boolean korisnikJeOdgovorniModerator = false;
		boolean korisnikJeAdministrator = false;
		
		if( us.getRole() == Role.ADMINISTRATOR)
			korisnikJeAdministrator = true;
		if (us.getEmail().equals(zaBrisanje.getMainModerator())){
			korisnikJeOdgovorniModerator = true;
		}
		
		if ( !korisnikJeAdministrator && !korisnikJeOdgovorniModerator){
			return null;
		}
		
		
		
		
		
		
		User u = (User)request.getSession().getAttribute("user");
		
		for(int i = 0; i < u.getFollowedSubforums().size();i++){
			if (zaBrisanje.getName().equals(zaBrisanje.getName())){
				u.getFollowedSubforums().remove(i);
			}
		}
		
		Database.getInstance().getSubforums().remove(Integer.parseInt(id));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return Database.getInstance().getSubforums();
		
	}
	
	@POST
	@Path("/upvote")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Theme upvote(String i){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			return null;
		}
		
		int index= Integer.parseInt(i);
		Theme s = Database.getInstance().getSelectedSubForum().getThemes().get(index);
		System.out.println("PRVA PROVERA tema koja se glasa: " + s.getName());
		User user = (User)request.getSession().getAttribute("user");
		System.out.println("DRUGA PROVERA korisnik koji glaas: " + user.getEmail());
		for(Theme t : user.getGlasaoTeme()){
			if(t.getName().equals(s.getName()) && t.getBelongsToSubforum().equals(s.getBelongsToSubforum())){ //ako je vec glasao proveri da li moze downvote ili upvote. ako nije glasao na tu temu postavi 
				System.out.println("TRECA PROVERA da li je vec glasano"+t.isVecGlasano());
				if(t.isVecGlasano()){
					System.out.println("Ne moze se glasati + dvaput");
					return null;
				} else {
					Database.getInstance().getSelectedSubForum().getThemes().get(index).setLikes(s.getLikes() + 1);
					Database.getInstance().getSelectedSubForum().getThemes().get(index).setDislikes(s.getDislikes() - 1);
					
					for(SubForum sub : Database.getSubforums()){
						if(Database.getInstance().getSelectedSubForum().getName().equals(sub.getName())) {
							
							for(Theme tt : sub.getThemes()){
								if(s.getName().equals(tt.getName())){
									
									tt.setLikes(Database.getInstance().getSelectedSubForum().getThemes().get(index).getLikes());
									tt.setDislikes(Database.getInstance().getSelectedSubForum().getThemes().get(index).getDislikes());
									tt.setVecGlasano(true);
									Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
									Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
									return Database.getInstance().getSelectedSubForum().getThemes().get(index);
								}
							}
						}	
					}
				}
			} 
		}
		
		
		
		Database.getInstance().getSelectedSubForum().getThemes().get(index).setLikes(s.getLikes() + 1);
		
		
		for(SubForum sub : Database.getSubforums()){
			if(Database.getInstance().getSelectedSubForum().getName().equals(sub.getName())) {
				
				for(Theme tt : sub.getThemes()){
					if(s.getName().equals(tt.getName())){
						
						tt.setLikes(Database.getInstance().getSelectedSubForum().getThemes().get(index).getLikes());
						
					}
				}
			}	
		}
		
		
		s.setVecGlasano(true);
		user.getGlasaoTeme().add(s);
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		
		
		return Database.getInstance().getSelectedSubForum().getThemes().get(index);
	}
	
	
	@POST
	@Path("/downvote")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Theme downvote(String i){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			return null;
		}
		
		System.out.println(i);
		int index= Integer.parseInt(i);
		Theme s = Database.getInstance().getSelectedSubForum().getThemes().get(index);
		
		System.out.println("PRVA PROVERA tema koja se glasa: " + s.getName());
		User user = (User)request.getSession().getAttribute("user");
		System.out.println("DRUGA PROVERA korisnik koji glaas: " + user.getEmail());
		for(Theme t : user.getGlasaoTeme()){
			if(t.getName().equals(s.getName())){ //ako je vec glasao proveri da li moze downvote ili upvote. ako nije glasao na tu temu postavi 
				System.out.println("TRECA PROVERA da li je vec glasano"+t.isVecGlasano());
				if(!t.isVecGlasano()){
					System.out.println("Ne moze se glasati - dvaput");
					return null;
				} else {
					Database.getInstance().getSelectedSubForum().getThemes().get(index).setDislikes(s.getDislikes() + 1);
					Database.getInstance().getSelectedSubForum().getThemes().get(index).setLikes(s.getLikes() - 1);
					
					for(SubForum sub : Database.getSubforums()){
						if(Database.getInstance().getSelectedSubForum().getName().equals(sub.getName())) {
							System.out.println("SUBFORUM: " + sub.getName());
							for(Theme tt : sub.getThemes()){
								if(s.getName().equals(tt.getName())){
									System.out.println("TEMA: " + tt.getName());
									tt.setDislikes(Database.getInstance().getSelectedSubForum().getThemes().get(index).getDislikes());
									tt.setLikes(Database.getInstance().getSelectedSubForum().getThemes().get(index).getLikes());
									tt.setVecGlasano(false);
									Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
									Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
									return Database.getInstance().getSelectedSubForum().getThemes().get(index);
								}
							}
						}	
					}
				}
			} 
		}
		
		
		Database.getInstance().getSelectedSubForum().getThemes().get(index).setDislikes(s.getDislikes() + 1);
		
		
		for(SubForum sub : Database.getSubforums()){
			if(Database.getInstance().getSelectedSubForum().getName().equals(sub.getName())) {
				System.out.println("SUBFORUM: " + sub.getName());
				for(Theme tt : sub.getThemes()){
					if(s.getName().equals(tt.getName())){
						System.out.println("TEMA: " + tt.getName());
						tt.setDislikes(Database.getInstance().getSelectedSubForum().getThemes().get(index).getDislikes());
						
					}
				}
			}	
		}
		
		s.setVecGlasano(false);
		user.getGlasaoTeme().add(s);
		
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		
		
		
		return Database.getInstance().getSelectedSubForum().getThemes().get(index);
	}
	
	@POST
	@Path("/goToComments")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Theme goToComments(String i){
		System.out.println( i);
		int index= Integer.parseInt(i);
		Theme s = Database.getInstance().getSelectedSubForum().getThemes().get(index);
		System.out.println("ime teme: " + s.getName() );
		s.setBrojPoseta(s.getBrojPoseta() + 1);
		Database.getInstance().setSelectedTheme(s);
		
		
		return s;
	}
	
	@GET
	@Path("/getSelectedTheme")
	@Produces(MediaType.APPLICATION_JSON)
	public Theme getSelectedTheme() {
		
		Theme s = Database.getInstance().getSelectedTheme();
		s.setBrojPoseta(s.getBrojPoseta()+1);
		System.out.println(s.getName());
		return s;
	}
	
	@POST
	@Path("/addComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addComment(Comment c){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println(c.getText());
		c.setBelongsToTheme(Database.getSelectedTheme().getName());
		c.setAuthor( ((User)request.getSession().getAttribute("user")).getEmail());
		
		Date date = new Date();
		c.setDate(sdf.format(date));
		c.setParentComment(null);
		c.setChildrenComments(new ArrayList<Comment>());
		c.setLikes(0);
		c.setDislikes(0);
		c.setEdited(false);
		c.setLogickiObrisan(false);
		
		c.setIdKomentara(Integer.toString(Database.getInstance().getSelectedTheme().getComments().size()));
		
		for (SubForum s : Database.getSubforums()){
			if (s.getName().equals(Database.getInstance().getSelectedSubForum().getName())){
				for(Theme t : s.getThemes()){
					if (t.getName().equals(c.getBelongsToTheme())){
						t.getComments().add(c);
						
					}
				}
			}
		}
		//Database.getSelectedTheme().getComments().add(c);
		
		System.out.println("ID KOMENTARA: " + c.getIdKomentara());
		
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		return true;
	}
	
	@POST
	@Path("/addSubComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean addSubComment(Comment c){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println(c.getText());
		c.setBelongsToTheme(Database.getSelectedTheme().getName());
		c.setAuthor(((User)request.getSession().getAttribute("user")).getEmail());
		
		Date date = new Date();
		c.setDate(sdf.format(date));
		System.out.println("PROVERA9: " + Database.getCurrentComment() );
		String[] idKomentaraSplit = Database.getCurrentComment().split("-");
		System.out.println("PRVA PROVERA SPLIT: " + idKomentaraSplit[0]);
		//uzimam komentar koji komentarisem:
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		
		for(int i = 1; i < idKomentaraSplit.length ; i++){
			tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
		}
		
		
		
		c.setParentComment(tmp.getIdKomentara());
		System.out.println("DRUGA PROVERA ID PARENTA: " + tmp.getIdKomentara());
		
		c.setChildrenComments(new ArrayList<Comment>());
		c.setLikes(0);
		c.setDislikes(0);
		c.setEdited(false);
		c.setLogickiObrisan(false);
		
		c.setIdKomentara(tmp.getIdKomentara() + "-" + tmp.getChildrenComments().size());
		tmp.getChildrenComments().add(c);
		for(Comment com : tmp.getChildrenComments()){
			System.out.println("PROVERA TRI ISPIS DECE KOMENTARA: " + com.getIdKomentara() + "     " + com.getText() );
		}
		
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		
		
		return true;
	}  
	
	
	@GET
	@Path("/saveCommentedCommentToDatabase/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean memorisiKomentarKojiSeKomentarise(@PathParam("id") String id){
		
		System.out.println("CURRENT COMMNT ID :" +id);
		
		Database.getInstance().setCurrentComment(id);
		
		
		
		return true;
	}
	
	@POST
	@Path("/upvoteComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Comment upvoteComment(String id){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return null;
		}
		
		
		if (id.indexOf("-") != -1){
			id = id.substring(1, id.length()-1);
		}
		

		System.out.println("PROVERA 0 CURRENT COMMENT: " + id);
		String[] idKomentaraSplit = id.split("-");
		
		//uzimam komentar koji komentarisem:
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		
		for(int i = 1; i < idKomentaraSplit.length ; i++){
			tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
		}
		
		User u = (User)request.getSession().getAttribute("user");
		
		for(Comment c : u.getGlasaoKomentare()){
			if(c.getIdKomentara().equals(id)){
				System.out.println("vec je glasao");
				if(c.isVecGlasano()){
					System.out.println("ne moze opet downvote");
					return null;
				} else {
					c.setLikes(c.getLikes()+1);
					c.setDislikes(c.getDislikes()-1);
					c.setVecGlasano(true);
					Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
					Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
					return c;
				}
			}
		}
		
		System.out.println("nije jos glasao za ovaj komentar");
		
		tmp.setLikes(tmp.getLikes()+1);
		tmp.setVecGlasano(true);
		u.getGlasaoKomentare().add(tmp);
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return tmp;
			
		
		
	}
	
	@POST
	@Path("/downvoteComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Comment downvoteComment(String id){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return null;
		}
		System.out.println("DOWNVOTE");
		
		if (id.indexOf("-") != -1){
			id = id.substring(1, id.length()-1);
		}
		
		
		System.out.println("PROVERA 0 CURRENT COMMENT: " + id);
		String[] idKomentaraSplit = id.split("-");
		
		
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		
		for(int i = 1; i < idKomentaraSplit.length ; i++){
			tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
		}
		
		User u = (User)request.getSession().getAttribute("user");
		
		for(Comment c : u.getGlasaoKomentare()){
			if(c.getIdKomentara().equals(id)){
				System.out.println("vec je glasao");
				if(!c.isVecGlasano()){
					System.out.println("ne moze opet downvote");
					return null;
				} else {
					c.setLikes(c.getLikes()-1);
					c.setDislikes(c.getDislikes()+1);
					c.setVecGlasano(false);
					Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
					Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
					return c;
				}
			}
		}
		
		System.out.println("nije jos glasao za ovaj komentar");
		
		tmp.setDislikes(tmp.getDislikes()+1);
		tmp.setVecGlasano(false);
		u.getGlasaoKomentare().add(tmp);
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		return tmp;
			
		}
		
	
	
	@POST
	@Path("/saveCommentToEditToDatabase")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean memorisiKomentarKojiSeMenja(String id){
		
		if (id.indexOf("-") != -1){
			id = id.substring(1, id.length()-1);
		}
		
		System.out.println("CURRENT COMMNT ID :" +id);
		
		Database.getInstance().setCurrentComment(id);
		
		return true;
		
		
		
	} 
	
	@POST
	@Path("/editComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean editComment(Comment c){
		//naci selektovani komentar i izmeniti ga u modelu
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		
		//uzmamo komentar kojihocemo da promenimo
		String[] idKomentaraSplit = Database.getInstance().getCurrentComment().split("-");
		
		//uzimam komentar koji komentarisem:
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		
		for(int i = 1; i < idKomentaraSplit.length ; i++){
			tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
		}
		
		boolean korisnikJeAutor = false;
		boolean korisnikJeOdgovorniModerator = false;
		
		User u = (User)request.getSession().getAttribute("user");
		
		if(u.getEmail().equals(tmp.getAuthor()))
			korisnikJeAutor = true;
		if(Database.getInstance().getSelectedSubForum().getMainModerator().equals(u.getEmail()))
			korisnikJeOdgovorniModerator = true;
		
		for(User s : Database.getSelectedSubForum().getModerators()){
			if (u.getEmail().equals(s.getEmail())){
				korisnikJeOdgovorniModerator = true;
			}
		}
		
		if(!korisnikJeAutor && !korisnikJeOdgovorniModerator){
			return false;
		}
		
		tmp.setText(c.getText());
		if(korisnikJeAutor && !korisnikJeOdgovorniModerator){
			tmp.setEdited(true);
		}
			
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
	} 
	
	@POST
	@Path("/saveThemeEditTheme")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Theme saveThemeEditTheme(String i){
		System.out.println( i);
		int index= Integer.parseInt(i);
		Theme s = Database.getInstance().getSelectedSubForum().getThemes().get(index);
		System.out.println("ime teme: " + s.getName() );
		Database.getInstance().setSelectedTheme(s);
		
		
		
		return s;
	}
	
	
	@GET
	@Path("/promoteUser/{id}")
	public ArrayList<User> promoteUser(@PathParam("id") String id){
		
		System.out.println(id);
		
		User us = (User)request.getSession().getAttribute("user");
		
		if (us.getRole() != Role.ADMINISTRATOR){
			return null;
		}
		
		switch(Database.getInstance().getUser(id).getRole()){
			case REGUSER : 
				Database.getInstance().getUser(id).setRole(Role.MODERATOR);
				break;
			case MODERATOR : 
				Database.getInstance().getUser(id).setRole(Role.ADMINISTRATOR);
				break;
			default: 
				return null;	
		}
		
		
		ArrayList<User> korisniciKojiNisuAdmini = new ArrayList<User>();
		
		for(User u : Database.getInstance().getUsers()){
			if(u.getRole() != Role.ADMINISTRATOR){
				korisniciKojiNisuAdmini.add(u);
			}
		}
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return korisniciKojiNisuAdmini;
	
		
		
		
	}
	
	@GET
	@Path("/promoteUser2/{id}")
	public ArrayList<User> promoteUser2(@PathParam("id") String id){
		
		User us = (User)request.getSession().getAttribute("user");
		
		if (us.getRole() != Role.ADMINISTRATOR){
			return null;
		}
		
		switch(Database.getInstance().getUser(id).getRole()){
			case REGUSER : 
				Database.getInstance().getUser(id).setRole(Role.MODERATOR);
				break;
			case MODERATOR : 
				Database.getInstance().getUser(id).setRole(Role.ADMINISTRATOR);
				break;
			default: 
				return null;	
		}
		
		
		ArrayList<User> k = new ArrayList<User>();
		
		for(User u : Database.getInstance().getUsers()){
			
				k.add(u);
			
		}
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return k;
	
		
		
		
	}
	
	@GET
	@Path("/demoteUser/{id}")
	public ArrayList<User> demoteUser(@PathParam("id") String id){
		
		System.out.println(id);
		
		User us = (User)request.getSession().getAttribute("user");
		
		if (us.getRole() != Role.ADMINISTRATOR){
			return null;
		}
		switch(Database.getInstance().getUser(id).getRole()){
			case REGUSER : 
				return null;
				
			case MODERATOR : 
				Database.getInstance().getUser(id).setRole(Role.REGUSER);
				break;
			default: 
				Database.getInstance().getUser(id).setRole(Role.REGUSER);
		}
		
		
		ArrayList<User> korisniciKojiNisuAdmini = new ArrayList<User>();
		
		for(User u : Database.getInstance().getUsers()){
			if(u.getRole() != Role.ADMINISTRATOR){
				korisniciKojiNisuAdmini.add(u);
			}
		}
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return korisniciKojiNisuAdmini;
	
		
		
		
	}
	
	@GET
	@Path("/demoteUser2/{id}")
	public ArrayList<User> demoteUser2(@PathParam("id") String id){
		System.out.println("USAOOOOO U DEMOTEEEEEEEEE");
		System.out.println(id);
		User us = (User)request.getSession().getAttribute("user");
		
		if (us.getRole() != Role.ADMINISTRATOR){
			return null;
		}
		
		switch(Database.getInstance().getUser(id).getRole()){
			case REGUSER : 
				return null;
				
			case MODERATOR : 
				Database.getInstance().getUser(id).setRole(Role.REGUSER);
				break;
			default: 
				return null;
		}
		
		
		ArrayList<User> k = new ArrayList<User>();
		
		for(User u : Database.getInstance().getUsers()){
			
				k.add(u);
			
		}
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return k;
	
		
		
		
	}
	@GET
	@Path("/obrisiKomentar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> obrisiKomentar(@PathParam("id") String id){
		
		
		//proveravam da li je gost
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return null;
		}
		
		boolean korisnikJeAutor = false;
		boolean korisnikJeModerator = false;
		boolean korisnikJeAdministrator = false;
		//uzimam komentar koji treba da se logicki obrise
		System.out.println("ID KOMENTARA KOJI CEMO OBRISATI JE: " + id);
		String[] idKomentaraSplit = id.split("-");
		
		for(int i = 0; i < idKomentaraSplit.length; i++){
			System.out.println("\t" + idKomentaraSplit[i]);
		}
		
		
		//uzimam komentar koji komentarisem:
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		
		for(int i = 1; i < idKomentaraSplit.length ; i++){
			System.out.println("USO U FOR");
			tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
		}
		
		User u = (User)request.getSession().getAttribute("user");
		
		if(u.getRole() == Role.ADMINISTRATOR)
			korisnikJeAdministrator = true;
		if(u.getRole() == Role.MODERATOR)
			korisnikJeModerator = true;
		if(u.getEmail().equals(tmp.getAuthor()))
			korisnikJeAutor = true;
		
		if( !korisnikJeAdministrator && !korisnikJeModerator && !korisnikJeAutor){
			return null;
		}
		
		
		
		System.out.println("KOMENTAR KOJI CE SE OBRISATI JE: " + tmp.getIdKomentara());
		tmp.setLogickiObrisan(true);
		tmp.setText("");
		tmp.setAuthor("");
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		return Database.getInstance().getSelectedTheme().getComments();
		
		
	}
	
	@GET
	@Path("/getCurrentUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getCurrentUser(){
		try{
			return (User)request.getSession().getAttribute("user");
		} catch (Exception e) {
			return null;
		}
		
		
	}
	
	@POST
	@Path("/sendMessage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean sendMessage(Message m){
		
		m.setSender( ((User)request.getSession().getAttribute("user")).getEmail() );
		m.setRead(false);
		
		boolean postoji = false;
		for(User u : Database.getInstance().getUsers()){
			if(m.getReciever().equals(u.getEmail())){
				postoji = true;
				break;
			}
		}
		
		if(!postoji){
			return false;
		}
		
		for(User u : Database.getInstance().getUsers()){
			if(m.getReciever().equals(u.getEmail())){
				u.getMessages().add(m);
				System.out.println("poruka poslata");
			}
		}
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return true;
	}
	
	@GET
	@Path("/getUserMessages")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Message> getUserMessages(){

		try{
			User u =  (User)request.getSession().getAttribute("user");
			return u.getMessages();
		} catch (Exception e) {
			return null;
		}
		
		
		
		
	}
	
	@GET
	@Path("/followSubForum/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean followSubForum(@PathParam("id") String id){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println(id);
		
		int index= Integer.parseInt(id);
		SubForum s = Database.getInstance().getSubforums().get(index);
		
		System.out.println(s.getName());
		
		User user = (User)request.getSession().getAttribute("user");
		
		for(User u : Database.getInstance().getUsers()){
			if(user.getEmail().equals(u.getEmail())){
				
				for(SubForum sf : u.getFollowedSubforums()){
					if(sf.getName().equals(s.getName())){
						System.out.println("Vec postoji takav zapracen podforum");
						return false;
					}
				}
				
				u.getFollowedSubforums().add(s);
				System.out.println("zapracen subforum");
			}
		}
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return true;	
		}
		
	
	@GET
	@Path("/getFollowedSubForums")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<SubForum> getFollowedSubForums(){

		try{
			User u = (User)request.getSession().getAttribute("user");
			return u.getFollowedSubforums();
			
		} catch(Exception e){
			return null;
		}
			
	}
		
		
	@GET
	@Path("/sacuvajTemu/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean sacuvajTemu(@PathParam("id") String id){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println(id);
		
		int index= Integer.parseInt(id);
		Theme t = Database.getInstance().getSelectedSubForum().getThemes().get(index);
		
		User user = (User)request.getSession().getAttribute("user");

		
		for(User u : Database.getInstance().getUsers()){
			if(user.getEmail().equals(u.getEmail())){
				
				for(Theme sf : u.getSavedThemes()){
					if(sf.getName().equals(t.getName())){
						System.out.println("Vec postoji tema");
						return false;
					}
				}
				
				u.getSavedThemes().add(new Theme(t));
				System.out.println("zapracen tema");
			}
		}
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
	
		return true;
		
		
		
	}
	
	@GET
	@Path("/getSavedThemes")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> getSavedThemes(){
		try{
			User u = (User)request.getSession().getAttribute("user");
			return u.getSavedThemes();
		}catch(Exception e){
			return null;
		}
			
	
	}
	
	@GET
	@Path("/getVotedThemes")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> getVotedThemes(){
		try{
			User u = (User)request.getSession().getAttribute("user");
			return u.getGlasaoTeme();
		}catch(Exception e){
			return null;
		}
		
		
		
		
	
	}
	
	@GET
	@Path("/snimiKomentar/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean snimiKomentar(@PathParam("id") String id){
		
		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println("ID KOMENTARA KOJI CEMO SNIMITI JE: " + id);
		String[] idKomentaraSplit = id.split("-");
		
		for(int i = 0; i < idKomentaraSplit.length; i++){
			System.out.println("\t" + idKomentaraSplit[i]);
		}
		
		
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		
		for(int i = 1; i < idKomentaraSplit.length ; i++){
			
			tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
		}
		
		System.out.println("KOMENTAR KOJI CE SE SNIMITI JE: " + tmp.getIdKomentara());
		
		User u = (User)request.getSession().getAttribute("user");
		
		
		
		u.getSavedComments().add(new Comment(tmp));
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
	
		return true;
		
	
		
		
		
	}
	
	@GET
	@Path("/getUserComments")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> getUserComments(){
		try{
			User u = (User)request.getSession().getAttribute("user");
			return u.getSavedComments();
		}catch(Exception e){
			return null;
		}
		
		
		
		
	}
	
	@GET
	@Path("/getVotedComments")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Comment> getVotedComments(){

		try{
			User u = (User)request.getSession().getAttribute("user");
			return u.getGlasaoKomentare();
		}catch(Exception e){
			return null;
		}
		
		
		
		
	
	}
	
	@GET
	@Path("/readMessage/{id}")
	
	@Produces(MediaType.APPLICATION_JSON)
	public boolean readMessage(@PathParam("id") String id){

		User u = (User)request.getSession().getAttribute("user");
		
		int index = Integer.parseInt(id);
		
		u.getMessages().get(index).setRead(true);
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		
		return true;
	
	}
	
	@GET
	@Path("/getThemesFromFollowedSubForums")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> getThemesFromFollowedSubForums() {
		User u = (User)request.getSession().getAttribute("user");
		ArrayList<Theme> zaVratiti = new ArrayList<Theme>();
		
		for(SubForum followed : u.getFollowedSubforums())
			for(SubForum s : Database.getInstance().getSubforums()){
				if(s.getName().equals(followed.getName())){
					for(Theme t : s.getThemes()){
						zaVratiti.add(t);
					}
				}
			}
		
		for(Theme t : zaVratiti){
			System.out.println(t.getName());
		}
		
		return zaVratiti;
	}
	
	@GET
	@Path("/setSelectedSubForumAndTheme/{id}/{id2}")
	@Produces(MediaType.APPLICATION_JSON)
	public int setSelectedSubForumAndTheme(@PathParam("id") String id,@PathParam("id2") String id2) {
		System.out.println(id);
		System.out.println(id2);
		
		for(SubForum s : Database.getInstance().getSubforums()){
			if (s.getName().equals(id2)){
				Database.setSelectedSubForum(s);
			}
		}
		
		for(Theme t : Database.getSelectedSubForum().getThemes()){
			if(t.getName().equals(id)){
				Database.getInstance().setSelectedTheme(t);
			}
		}
		
		for(int i = 0; i < Database.getSelectedSubForum().getThemes().size() ; i++){
			if(Database.getSelectedSubForum().getThemes().get(i).getName().equals(id)){
				Database.getInstance().setSelectedTheme(Database.getSelectedSubForum().getThemes().get(i));
				return i;
			}
		}
		
		
		return -1;
	}
	
	@POST
	@Path("/pretraziPodforume")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Integer> pretraziPodforume(SubForumPretraga pretraga){
		ArrayList<Integer> listaLosihIndexa = new ArrayList<Integer>();
		boolean naslov = true;
		boolean opis = true;
		boolean moderator = true;
		
		if(pretraga.getName().equals("")){
			naslov = false;
		} 
		
		if(pretraga.getDescription().equals("")){
			opis = false;
		} 
		
		if(pretraga.getMainModerator().equals("")){
			moderator = false;
		} 
		
		
		
		for(int i = 0; i < Database.getInstance().getSubforums().size();i++){
			SubForum s = Database.getInstance().getSubforums().get(i);
			
			if(naslov && !s.getName().contains((CharSequence)(pretraga.getName()))){
					listaLosihIndexa.add(i);
					
			}else if(opis && !s.getDescription().contains((CharSequence)(pretraga.getDescription())) ){
			
					listaLosihIndexa.add(i);
			}else if(moderator && !s.getMainModerator().contains((CharSequence)(pretraga.getMainModerator()))){
				
					listaLosihIndexa.add(i);
					
				
			}
		}
		
		System.out.println("PROVERA LOSIH INDEXA: ");
		for (Integer i : listaLosihIndexa){
			System.out.println("Index : " + i);
		}
		
		return listaLosihIndexa;
	}
	
	@POST
	@Path("/pretraziTemePodforuma")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Integer> pretraziTemePodforuma(ThemePretraga pretraga){
		ArrayList<Integer> listaLosihIndexa = new ArrayList<Integer>();
		boolean naslov = true;
		boolean sadrzaj = true;
		boolean Autor = true;
		boolean podforum = true;
		
		if(pretraga.getName().equals("")){
			naslov = false;
		} 
		
		if(pretraga.getContent().equals("")){
			sadrzaj = false;
		} 
		
		if(pretraga.getAuthor().equals("")){
			Autor = false;
		} 
		
		if(pretraga.getBelongsToSubforum().equals("")){
			podforum = false;
		} 
		
		
		
		for(int i = 0; i < Database.getInstance().getSelectedSubForum().getThemes().size();i++){
			Theme t = Database.getInstance().getSelectedSubForum().getThemes().get(i);
			
			if(naslov && !t.getName().contains((CharSequence)(pretraga.getName()))){
				
				
					listaLosihIndexa.add(i);
					
			
			}else if(sadrzaj && !t.getContent().contains((CharSequence)(pretraga.getContent())) ){
				
					listaLosihIndexa.add(i);

			}else if(podforum && !t.getBelongsToSubforum().contains((CharSequence)(pretraga.getBelongsToSubforum()))){
				
					listaLosihIndexa.add(i);

			}else if(Autor && !t.getAuthor().contains((CharSequence)(pretraga.getAuthor()))){
				
					listaLosihIndexa.add(i);
					
				
			}
		}
		
		System.out.println("PROVERA LOSIH INDEXA: ");
		for (Integer i : listaLosihIndexa){
			System.out.println("Index : " + i);
		}
		
		return listaLosihIndexa;
	}
	
	@POST
	@Path("/pretraziTemePodforumaKojeKorisnikPrati")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<ThemePretragaUser> pretraziTemePodforumaKojeKorisnikPrati(ThemePretraga pretraga){
		ArrayList<ThemePretragaUser> listaLosihTema = new ArrayList<ThemePretragaUser>();
		boolean naslov = true;
		boolean sadrzaj = true;
		boolean Autor = true;
		boolean podforum = true;
		
		if(pretraga.getName().equals("")){
			naslov = false;
		} 
		
		if(pretraga.getContent().equals("")){
			sadrzaj = false;
		} 
		
		if(pretraga.getAuthor().equals("")){
			Autor = false;
		} 
		
		if(pretraga.getBelongsToSubforum().equals("")){
			podforum = false;
		} 
		
		
		
		for(int i = 0; i < Database.getInstance().getSelectedSubForum().getThemes().size();i++){
			Theme t = Database.getInstance().getSelectedSubForum().getThemes().get(i);
			
			if(naslov && !t.getName().contains((CharSequence)(pretraga.getName()))){

					listaLosihTema.add( new ThemePretragaUser(t.getName(),t.getBelongsToSubforum()) );

				
			}else if(sadrzaj && !t.getContent().contains((CharSequence)(pretraga.getContent())) ){
				
					listaLosihTema.add( new ThemePretragaUser(t.getName(),t.getBelongsToSubforum()) );

			}else if(podforum && !t.getBelongsToSubforum().contains((CharSequence)(pretraga.getBelongsToSubforum())) ){
				
					listaLosihTema.add( new ThemePretragaUser(t.getName(),t.getBelongsToSubforum()) );
					
				
			}else if(Autor && !t.getAuthor().contains((CharSequence)(pretraga.getAuthor()))){
				
					listaLosihTema.add( new ThemePretragaUser(t.getName(),t.getBelongsToSubforum()) );
					
				
			}
		}
		
		System.out.println("PROVERA LOSIH TEMA: ");
		for (ThemePretragaUser i : listaLosihTema){
			System.out.println(i.getTheme() + "\t" + i.getSubForum());
		}
		
		return listaLosihTema;
	}
	
	@POST
	@Path("/pretraziKorisnike")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Integer> pretraziKorisnike(String pretraga){
		pretraga = pretraga.substring(1,pretraga.length()-1);
		System.out.println("USERNAME: " + pretraga);
		ArrayList<Integer> listaLosihIndexa = new ArrayList<Integer>();
		
		for(int i = 0; i < Database.getUsers().size(); i++){
			if(!pretraga.contains(Database.getInstance().getUsers().get(i).getUsername())){
				listaLosihIndexa.add(i);
			}
		}
		
		System.out.println("PROVERA LOSIH indexa: ");
		for (Integer i : listaLosihIndexa){
			System.out.println(i);
		}
		
		return listaLosihIndexa;
	}
	
	@GET
	@Path("/getZalbe")
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Zalba> getZalbe() {
		
		try{
			User u = (User)request.getSession().getAttribute("user");
			return u.getZalbe();
		}catch(Exception e){
			return null;
		}
		
	}
	
	@POST
	@Path("/kreirajZalbuPodforum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean kreirajZalbu(Zalba z){
		

		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println(z.getContent());
		
		z.setAuthor( ((User)request.getSession().getAttribute("user")).getEmail() );
		z.setDate(sdf.format(new Date()));
		z.setEntitet(Entitet.SUBFORUM);
		z.setSubForumZaZalbu(Database.getInstance().getSelectedSubForum());
		
		
		for(User u : Database.getInstance().getUsers()){
			if (u.getRole() == Role.ADMINISTRATOR)
					u.getZalbe().add(z);
		}
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
	}
	
	
	
	@GET
	@Path("/resiZalbuBrisanjemPodforuma/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuBrisanjemPodforuma(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Podforum za zalbu " + z.getSubForumZaZalbu().getName());
		boolean nasaoPodforum = false;
		for(int i = 0; i < Database.getInstance().getSubforums().size(); i++){
			if (Database.getInstance().getSubforums().get(i).getName().equals(z.getSubForumZaZalbu().getName())){
				SubForum zaBrisanje = Database.getInstance().getSubforums().get(i);

				for(User user : Database.getUsers()){
					if(user.getEmail().equals(zaBrisanje.getMainModerator())){
						Message zaPoslatiAutoruPodforuma = new Message();
						zaPoslatiAutoruPodforuma.setContent("Podforum "+zaBrisanje.getName() +" koji ste napravili je obrisan zbog zalbe");
						zaPoslatiAutoruPodforuma.setSender("FORUM");
						user.getMessages().add(zaPoslatiAutoruPodforuma);
						
					}
					
					if(user.getEmail().equals(z.getAuthor())){
						Message zaPoslatiAutoruZalbe = new Message();
						zaPoslatiAutoruZalbe.setContent("Podforum "+zaBrisanje.getName() +" na koji ste se zalili je obrisan");
						zaPoslatiAutoruZalbe.setSender("FORUM");
						user.getMessages().add(zaPoslatiAutoruZalbe);
					}
				}
				
				Database.getInstance().getSubforums().remove(i);
				nasaoPodforum = true;
			}
		}
		
		if(!nasaoPodforum){
			System.out.println("Podforum vec izbrisan");
			return false;
		}
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		
		return true;
		
	}
	
	@GET
	@Path("/resiZalbuUpozorenjemPodforuma/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuUpozorenjemPodforuma(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Podforum za zalbu " + z.getSubForumZaZalbu().getName());
		boolean nasaoPodforum = false;
		for(int i = 0; i < Database.getInstance().getSubforums().size(); i++){
			if (Database.getInstance().getSubforums().get(i).getName().equals(z.getSubForumZaZalbu().getName())){
				SubForum zaBrisanje = Database.getInstance().getSubforums().get(i);

				for(User user : Database.getUsers()){
					if(user.getEmail().equals(zaBrisanje.getMainModerator())){
						Message zaPoslatiAutoruPodforuma = new Message();
						zaPoslatiAutoruPodforuma.setContent("Upozorenje za podforum "+zaBrisanje.getName() +" koji ste napravili");
						zaPoslatiAutoruPodforuma.setSender("FORUM");
						user.getMessages().add(zaPoslatiAutoruPodforuma);
						
					}
					
					if(user.getEmail().equals(z.getAuthor())){
						Message zaPoslatiAutoruZalbe = new Message();
						zaPoslatiAutoruZalbe.setContent("Podforum "+zaBrisanje.getName() +" je dobio upozorenje");
						zaPoslatiAutoruZalbe.setSender("FORUM");
						user.getMessages().add(zaPoslatiAutoruZalbe);
					}
				}
				
				
				nasaoPodforum = true;
			}
		}
		
		if(!nasaoPodforum){
			System.out.println("Podforum vec izbrisan");
			return false;
		}
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		
		return true;
		
	}
	
	@GET
	@Path("/resiZalbuUpozorenjemZalbePodforuma/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuUpozorenjemZalbePodforuma(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Podforum za zalbu " + z.getSubForumZaZalbu().getName());
		boolean nasaoPodforum = false;
		for(int i = 0; i < Database.getInstance().getSubforums().size(); i++){
			if (Database.getInstance().getSubforums().get(i).getName().equals(z.getSubForumZaZalbu().getName())){
				SubForum zaBrisanje = Database.getInstance().getSubforums().get(i);

				for(User user : Database.getUsers()){
					
					
					if(user.getEmail().equals(z.getAuthor())){
						Message zaPoslatiAutoruZalbe = new Message();
						zaPoslatiAutoruZalbe.setContent("Podforum "+zaBrisanje.getName() +" na koji ste se zalili je sasvim OK");
						zaPoslatiAutoruZalbe.setSender("FORUM");
						user.getMessages().add(zaPoslatiAutoruZalbe);
					}
				}
				
				
				nasaoPodforum = true;
			}
		}
		
		if(!nasaoPodforum){
			System.out.println("Podforum vec izbrisan");
			return false;
		}
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		
		return true;
		
	}
	
	
	@POST
	@Path("/kreirajZalbuTema")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean kreirajZalbuTema(Zalba z){
		

		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println(z.getContent());
		
		z.setAuthor( ((User)request.getSession().getAttribute("user")).getEmail() );
		z.setDate(sdf.format(new Date()));
		z.setEntitet(Entitet.THEME);
		z.setTemaZaZalbu(Database.getInstance().getSelectedTheme());
		
		
		for(User u : Database.getInstance().getUsers()){
			if (u.getRole() == Role.ADMINISTRATOR)
					u.getZalbe().add(z);
			
			if (u.getEmail().equals(Database.getInstance().getSelectedSubForum().getMainModerator())){
					u.getZalbe().add(z);
			}
		}
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
	}
	
	@GET
	@Path("/resiZalbuBrisanjemTeme/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuBrisanjemTeme(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		Theme t = z.getTemaZaZalbu();
		System.out.println("Tema za zalbu " + z.getTemaZaZalbu().getName());
		boolean nasaoTemu = false;
		
		for(SubForum s : Database.getSubforums()){
			if(s.getName().equals(z.getTemaZaZalbu().getBelongsToSubforum()))
				Database.setSelectedSubForum(s);
		}
		
		
			for(int i = 0; i < Database.getInstance().getSelectedSubForum().getThemes().size(); i++){
				if (Database.getInstance().getSelectedSubForum().getThemes().get(i).getName().equals(z.getTemaZaZalbu().getName())){
					Theme zaBrisanje = Database.getInstance().getSelectedSubForum().getThemes().get(i);
	
					for(User user : Database.getUsers()){
						if(user.getEmail().equals(zaBrisanje.getAuthor())){
							Message zaPoslatiAutoruPodforuma = new Message();
							zaPoslatiAutoruPodforuma.setContent("Tema "+zaBrisanje.getName() +" koji ste napravili je obrisan zbog zalbe");
							zaPoslatiAutoruPodforuma.setSender("FORUM");
							user.getMessages().add(zaPoslatiAutoruPodforuma);
							
						}
						
						if(user.getEmail().equals(z.getAuthor())){
							Message zaPoslatiAutoruZalbe = new Message();
							zaPoslatiAutoruZalbe.setContent("Tema "+zaBrisanje.getName() +" na koji ste se zalili je obrisan");
							zaPoslatiAutoruZalbe.setSender("FORUM");
							user.getMessages().add(zaPoslatiAutoruZalbe);
						}
					}
					
					Database.getInstance().getSelectedSubForum().getThemes().remove(i);
					nasaoTemu = true;
				}
			}
		
	
		if(!nasaoTemu){
			System.out.println("Tema vec izbrisan");
			return false;
		}
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		
		return true;
		
	}
	
	@GET
	@Path("/resiZalbuUpozorenjemTeme/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuUpozorenjemTeme(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Tema za zalbu " + z.getTemaZaZalbu().getName());
		boolean nasaoTemu = false;
		
		for(SubForum s : Database.getSubforums()){
			if(s.getName().equals(z.getTemaZaZalbu().getBelongsToSubforum()))
				Database.setSelectedSubForum(s);
		}
		
			for(int i = 0; i < Database.getInstance().getSelectedSubForum().getThemes().size(); i++){
				if (Database.getInstance().getSelectedSubForum().getThemes().get(i).getName().equals(z.getTemaZaZalbu().getName())){
					Theme zaBrisanje = Database.getInstance().getSelectedSubForum().getThemes().get(i);
	
					for(User user : Database.getUsers()){
						if(user.getEmail().equals(zaBrisanje.getAuthor())){
							Message zaPoslatiAutoruPodforuma = new Message();
							zaPoslatiAutoruPodforuma.setContent("Tema "+zaBrisanje.getName() +" koji ste napravili je upozoren zbog zalbe");
							zaPoslatiAutoruPodforuma.setSender("FORUM");
							user.getMessages().add(zaPoslatiAutoruPodforuma);
							
						}
						
						if(user.getEmail().equals(z.getAuthor())){
							Message zaPoslatiAutoruZalbe = new Message();
							zaPoslatiAutoruZalbe.setContent("Tema "+zaBrisanje.getName() +" na koji ste se zalili je upozoren");
							zaPoslatiAutoruZalbe.setSender("FORUM");
							user.getMessages().add(zaPoslatiAutoruZalbe);
						}
					}
					
					
					nasaoTemu = true;
				}
			}
		
	
		if(!nasaoTemu){
			System.out.println("Tema vec izbrisan");
			return false;
		}
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
		
	}
	
	@GET
	@Path("/resiZalbuUpozorenjemZalbeTeme/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuUpozorenjemZalbeTeme(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Tema za zalbu AAAAAAA" + z.getTemaZaZalbu().getName());
		boolean nasaoTemu = false;
		
		for(SubForum s : Database.getSubforums()){
			if(s.getName().equals(z.getTemaZaZalbu().getBelongsToSubforum()))
				Database.setSelectedSubForum(s);
		}
		
			for(int i = 0; i < Database.getInstance().getSelectedSubForum().getThemes().size(); i++){
				if (Database.getInstance().getSelectedSubForum().getThemes().get(i).getName().equals(z.getTemaZaZalbu().getName())){
					Theme zaBrisanje = Database.getInstance().getSelectedSubForum().getThemes().get(i);
	
					for(User user : Database.getUsers()){
						
						if(user.getEmail().equals(z.getAuthor())){
							Message zaPoslatiAutoruZalbe = new Message();
							zaPoslatiAutoruZalbe.setContent("Tema "+zaBrisanje.getName() +" na koji ste se zalili je OK");
							zaPoslatiAutoruZalbe.setSender("FORUM");
							user.getMessages().add(zaPoslatiAutoruZalbe);
						}
					}
					
					
					nasaoTemu = true;
				}
			}
		
	
		if(!nasaoTemu){
			System.out.println("Tema vec izbrisan");
			return false;
		}
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
		
	}
	
	@POST
	@Path("/setSelectedComment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setSelectedComment(String id){
		
		if (id.indexOf("-") != -1){
			id = id.substring(1, id.length()-1);
		}
		
		System.out.println("CURRENT COMMNT ID :" +id);
		
		Database.getInstance().setCurrentComment(id);
		
		return true;
		
		
		
	} 
	
	@POST
	@Path("/kreirajZalbuKomentar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean kreirajZalbuKomentar(Zalba z){
		

		if( ((User)request.getSession().getAttribute("user")).getRole() == Role.GUEST){
			System.out.println("VRATIO FALSE");
			return false;
		}
		
		System.out.println(z.getContent());
		
		z.setAuthor( ((User)request.getSession().getAttribute("user")).getEmail() );
		z.setDate(sdf.format(new Date()));
		z.setEntitet(Entitet.COMMENT);
		z.setKomentarZaZalbu(Database.getInstance().getCurrentComment());
		z.setTemaZaZalbu(Database.getInstance().getSelectedTheme());
		z.setSubForumZaZalbu(Database.getInstance().getSelectedSubForum());
		
		
		for(User u : Database.getInstance().getUsers()){
			if (u.getRole() == Role.ADMINISTRATOR)
					u.getZalbe().add(z);
			
			if (u.getEmail().equals(Database.getInstance().getSelectedSubForum().getMainModerator())){
					u.getZalbe().add(z);
			}
		}
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
		
		return true;
	}
	
	@GET
	@Path("/resiZalbuBrisanjemKomentara/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuBrisanjemKomentara(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Kometar  za zalbu " + z.getKomentarZaZalbu());
		
		
		
		///////////////////////////ITERIRANJE KROZ KOMENTAR
		String[] idKomentaraSplit = z.getKomentarZaZalbu().split("-");
		
		for(int i = 0; i < idKomentaraSplit.length; i++){
			System.out.println("\t" + idKomentaraSplit[i]);
		}

		//uzimam komentar koji komentarisem:
		
		for(SubForum s : Database.getSubforums()){
			if(s.getName().equals(z.getSubForumZaZalbu().getName())){
				Database.getInstance().setSelectedSubForum(s);
				for(Theme t: Database.getInstance().getSelectedSubForum().getThemes()){
					if(t.getName().equals(z.getTemaZaZalbu().getName())){
						Database.getInstance().setSelectedTheme(t);
					}
				}
			}	
		}
		
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		 try{
			 for(int i = 1; i < idKomentaraSplit.length ; i++){
					System.out.println("USO U FOR");
					tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
				}
			 
		 } catch (Exception e) {
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
			 
		 }
		 
		 if(!tmp.getIdKomentara().equals(z.getKomentarZaZalbu())){
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
		 }
		 
		 if(tmp.isLogickiObrisan())
		 {
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
		 }
		
		
		
		
		for(User user : Database.getUsers()){
			if(user.getEmail().equals(tmp.getAuthor())){
				Message zaPoslatiAutoruPodforuma = new Message();
				zaPoslatiAutoruPodforuma.setContent("Komentar"+tmp.getIdKomentara() +" koji ste napravili je logicki obrisan zbog zalbe");
				zaPoslatiAutoruPodforuma.setSender("FORUM");
				user.getMessages().add(zaPoslatiAutoruPodforuma);
				
			}
			
			if(user.getEmail().equals(z.getAuthor())){
				Message zaPoslatiAutoruZalbe = new Message();
				zaPoslatiAutoruZalbe.setContent("Komentar "+tmp.getIdKomentara() +" na koji ste se zalili je obrisan");
				zaPoslatiAutoruZalbe.setSender("FORUM");
				user.getMessages().add(zaPoslatiAutoruZalbe);
			}
		}
		
		System.out.println("KOMENTAR KOJI CE SE OBRISATI JE: " + tmp.getIdKomentara());
		tmp.setLogickiObrisan(true);
		tmp.setText("");
		tmp.setAuthor("");
		
		////////////////////////////////////////////////////////ITERIRANEJ KORZ KOMENTARZAVESIESNO
		
			
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		
		return true;
		
	}
	
	@GET
	@Path("/resiZalbuUpozorenjemKomentara/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuUpozorenjemKomentara(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Kometar  za zalbu " + z.getKomentarZaZalbu());
		
		
		
		///////////////////////////ITERIRANJE KROZ KOMENTAR
		String[] idKomentaraSplit = z.getKomentarZaZalbu().split("-");
		
		for(int i = 0; i < idKomentaraSplit.length; i++){
			System.out.println("\t" + idKomentaraSplit[i]);
		}

		//uzimam komentar koji komentarisem:
		
		for(SubForum s : Database.getSubforums()){
			if(s.getName().equals(z.getSubForumZaZalbu().getName())){
				Database.getInstance().setSelectedSubForum(s);
				for(Theme t: Database.getInstance().getSelectedSubForum().getThemes()){
					if(t.getName().equals(z.getTemaZaZalbu().getName())){
						Database.getInstance().setSelectedTheme(t);
					}
				}
			}	
		}
		
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		 try{
			 for(int i = 1; i < idKomentaraSplit.length ; i++){
					System.out.println("USO U FOR");
					tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
				}
			 
		 } catch (Exception e) {
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
			 
		 }
		 
		 if(!tmp.getIdKomentara().equals(z.getKomentarZaZalbu())){
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
		 }
		 
		 if(tmp.isLogickiObrisan())
		 {
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
		 }
		
		
		
		
		for(User user : Database.getUsers()){
			if(user.getEmail().equals(tmp.getAuthor())){
				Message zaPoslatiAutoruPodforuma = new Message();
				zaPoslatiAutoruPodforuma.setContent("Komentar"+tmp.getIdKomentara() +" koji ste napravili je upozoren zbog zalbe");
				zaPoslatiAutoruPodforuma.setSender("FORUM");
				user.getMessages().add(zaPoslatiAutoruPodforuma);
				
			}
			
			if(user.getEmail().equals(z.getAuthor())){
				Message zaPoslatiAutoruZalbe = new Message();
				zaPoslatiAutoruZalbe.setContent("Komentar "+tmp.getIdKomentara() +" na koji ste se zalili je upozoren");
				zaPoslatiAutoruZalbe.setSender("FORUM");
				user.getMessages().add(zaPoslatiAutoruZalbe);
			}
		}
		
		
	
		////////////////////////////////////////////////////////ITERIRANEJ KORZ KOMENTARZAVESIESNO
		
			
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		
		return true;
		
	}
	
	@GET
	@Path("/resiZalbuOdbijanjemZalbeKomentara/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean resiZalbuOdbijanjemZalbeKomentara(@PathParam("id") String id){
		
		System.out.println(id);
		int index = Integer.parseInt(id);
		
		User u = (User)request.getSession().getAttribute("user");
		
		Zalba z = u.getZalbe().get(index);
		System.out.println("Kometar  za zalbu " + z.getKomentarZaZalbu());
		
		
		
		///////////////////////////ITERIRANJE KROZ KOMENTAR
		String[] idKomentaraSplit = z.getKomentarZaZalbu().split("-");
		
		for(int i = 0; i < idKomentaraSplit.length; i++){
			System.out.println("\t" + idKomentaraSplit[i]);
		}

		//uzimam komentar koji komentarisem:
		
		for(SubForum s : Database.getSubforums()){
			if(s.getName().equals(z.getSubForumZaZalbu().getName())){
				Database.getInstance().setSelectedSubForum(s);
				for(Theme t: Database.getInstance().getSelectedSubForum().getThemes()){
					if(t.getName().equals(z.getTemaZaZalbu().getName())){
						Database.getInstance().setSelectedTheme(t);
					}
				}
			}	
		}
		
		
		Comment tmp = Database.getInstance().getSelectedTheme().getComments().get(Integer.parseInt(idKomentaraSplit[0]));
		 try{
			 for(int i = 1; i < idKomentaraSplit.length ; i++){
					System.out.println("USO U FOR");
					tmp = tmp.getChildrenComments().get(Integer.parseInt(idKomentaraSplit[i]));
				}
			 
		 } catch (Exception e) {
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
			 
		 }
		 
		 if(!tmp.getIdKomentara().equals(z.getKomentarZaZalbu())){
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
		 }
		 
		 if(tmp.isLogickiObrisan())
		 {
			 System.out.println("komentar vec obrisan");
			 u.getZalbe().remove(index);
			 return false;
		 }
		
		
		
		
		for(User user : Database.getUsers()){
			
			
			if(user.getEmail().equals(z.getAuthor())){
				Message zaPoslatiAutoruZalbe = new Message();
				zaPoslatiAutoruZalbe.setContent("Komentar "+tmp.getIdKomentara() +" na koji ste se zalili je OK");
				zaPoslatiAutoruZalbe.setSender("FORUM");
				user.getMessages().add(zaPoslatiAutoruZalbe);
			}
		}
		
		
	
		////////////////////////////////////////////////////////ITERIRANEJ KORZ KOMENTARZAVESIESNO
		
			
		
		u.getZalbe().remove(index);
		//TODO ovde mozda treba u pravog usera da udjes da izbrises zalbu.
		
		
		Database.getInstance().serialize(Database.getInstance().getUsers(), request.getServletContext().getRealPath("users.forum"));
		Database.getInstance().serialize(Database.getInstance().getSubforums(), request.getServletContext().getRealPath("SubForums.forum"));
	
		
		return true;
		
	}
	
	@GET
	@Path("/getPreporuceneTeme")
	
	@Produces(MediaType.APPLICATION_JSON)
	public ArrayList<Theme> getPreporuceneTeme(){
		
		ArrayList<Theme> preporucene = new ArrayList<Theme>();
		
		for(SubForum s: Database.getInstance().getSubforums()){
			for(Theme t : s.getThemes()){
				preporucene.add(t);
			}
		}
		
		Collections.sort(preporucene,new ThemeLikeComparator());
		 
		for(Theme t : preporucene){
			System.out.println(t.getName() + "\t" + t.getLikes());
		}
		
		ArrayList<Theme> zaReturn = new ArrayList<Theme>();
		
		if (preporucene.size()<5) {
			for(int i = 0; i < preporucene.size() ; i++){
				zaReturn.add(preporucene.get(i));
			}
		} else {
			for(int i = 0; i < 5 ; i++){
				zaReturn.add(preporucene.get(i));
			}
		}
		
		
		return zaReturn;
	}
	
	
	@GET
	@Path("/getUser")
	@Produces(MediaType.APPLICATION_JSON)
	public Role getUser() {
		try{
			System.out.println( ((User)request.getSession().getAttribute("user")).getRole());
		} catch(Exception e){
			System.out.println("EEE");
			return null;
		}
			
		System.out.println("CHECK");
		return ((User)request.getSession().getAttribute("user")).getRole();

	}
	
	@GET
	@Path("/setSelectedSavedTheme/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setSelectedSavedTheme(@PathParam("id") String id){
		
		
		
		System.out.println(id);
		
		int index= Integer.parseInt(id);
		User u = (User)request.getSession().getAttribute("user");
		Theme t = u.getSavedThemes().get(index);
		
		Database.getInstance().setSelectedSavedTheme(t);
	
		return true;
		
		
		
	}
	
	@GET
	@Path("/getSelectedSavedTheme")
	@Produces(MediaType.APPLICATION_JSON)
	public Theme getSelectedSavedTheme(){
		
	
	
		return Database.getInstance().getSelectedSavedTheme();
		
		
		
	}
	
	@GET
	@Path("/setSelectedSavedComment/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setSelectedSavedComment(@PathParam("id") String id){
		
		
		
		System.out.println(id);
		
		int index= Integer.parseInt(id);
		User u = (User)request.getSession().getAttribute("user");
		Comment t = u.getSavedComments().get(index);
		
		Database.getInstance().setSelectedSavedComment(t);
	
		return true;
		
		
		
	}
	
	@GET
	@Path("/getSelectedSavedComment")
	@Produces(MediaType.APPLICATION_JSON)
	public Comment getSelectedSavedComment(){
		
	
	
		return Database.getInstance().getSelectedSavedComment();
		
		
		
	}
	@POST
	@Path("/setSelectedSubForum")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public boolean setSelectedSubForum(String i){
		System.out.println( i);
		for(SubForum s : Database.getInstance().getSubforums()){
			if (s.getName().equals(i)){
				Database.setSelectedSubForum(s);
			}
		}
	
		
		
		return true;
	}
}

