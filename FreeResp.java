import java.util.*;

class Element{
	String val;
	int amt;
	public Element(String val, int amt){
		this.val=val;
		this.amt=amt;
	}
}

class ElementComparable implements Comparator<Element>{
	public int compare(Element e1, Element e2){
		if(e1.amt > e2.amt){
			return -1;
		} else if(e1.amt == e2.amt){
			return 0;
		} else {
			return 1;
		}
	}
}



public class FreeResp{
	public static TreeMap<String,Integer> artistCount = new TreeMap<String,Integer>();
	public static void main(String[] args){

String[] artists = {"Ariana Grande", "Ariana Grande", "Ariana Grande","Halsey","Halsey","Post Malone","Post Malone","Post Malone","Ariana Grande","Ariana Grande","Ariana Grande","Ariana Grande","Lady Gaga","Lady Gaga","Lady Gaga","Lady Gaga","Jonas Brothers","Jonas Brothers","Jonas Brothers","Jonas Brothers","Jonas Brothers","Jonas Brothers","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Lil Nas X","Billie Eilish","Billie Eilish","Billie Eilish","Billie Eilish","Billie Eilish","Billie Eilish","Shawn Mendes","Lizzo","Lizzo","Lizzo","Lizzo","Lizzo","Lizzo","Travis Scott","Selena Gomez","Selena Gomez","Post Malone","Post Malone","Post Malone","Post Malone","The Weeknd","Mariah Carey","Mariah Carey"};

	for(String str : artists){
		if(artistCount.get(str)==null){
			artistCount.put(str,1);
		} else {
			artistCount.put(str, artistCount.get(str)+1);
		}
	}

	Set<String> keys = artistCount.keySet();
	ArrayList<Element> artiste = new ArrayList<Element>();
	for(String k : keys){
		artiste.add(new Element(k,artistCount.get(k)));
	}

	Collections.sort(artiste, new ElementComparable() );
	for(Element e : artiste){
		System.out.println(e.val + " " + e.amt);
	}
      
    }
}
