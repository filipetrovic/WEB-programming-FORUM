package beans;

import java.util.Comparator;

public class ThemeLikeComparator implements Comparator<Theme> {
	public int compare(Theme t1, Theme t2) {
        
		int i = t2.getLikes() - t1.getLikes();
		if (i != 0) 
			return i;
		else 
			return t2.getBrojPoseta() - t1.getBrojPoseta();
		
			
    }
}
