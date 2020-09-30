import java.io.*;
import java.util.*;
import java.net.*;


class LinkedList<E>{
	Node head;
	Node cur_ptr;//will by default be the same as the head
	int sz = 0;
	
	public void add(E e){
		if(sz==0){
			Node<E> tmp = new Node(e,null);
			head=tmp;
			cur_ptr=tmp;
			++sz;
		} else {
			Node nw = new Node(e,null);
			cur_ptr.nxt = nw;
			cur_ptr= nw;
			++sz;
		}
	}
	//sets value at some given Node to be another value (functional)
	public void set(int index, E e){
		try{
			int c_idx = 0;
			Node cur_vl = head;
			while(cur_vl != null){
				if(c_idx == index - 1){
					break;
				}
				++c_idx;
				cur_vl = cur_vl.nxt; 
			}	
			Node new_one = new Node(e, (cur_vl.nxt).nxt );
			cur_vl.nxt = new_one;
			cur_ptr = cur_vl;
		} catch(Exception exp){
			exp.printStackTrace();
		}
	}
	//adds to front (functional)
	public void addToFront(E e){
		try{
			Node nw = new Node(e, head);
			nw.nxt = head;
			cur_ptr=head;
			head=nw;
			sz++;
		} catch(Exception exp){
			exp.printStackTrace();
		}
	}

	//removes front node(functional)
	public Node<E> removeFront(){
		Node store = head;
		head = head.nxt;
		--sz;
		return store;
	}
	//determines if a value is contained in our linked list(functional)
	public boolean contains(E e){
		Node see = head;
		while(see.nxt != null){
			if(see.vl==e){return true;}
			see = see.nxt;
		}
		return false;
	}
	//adds to some arbitrary index in our linked list (functional)
	public void add(int index, E e){
		try{
			Node cr = head;
			int c_idx = 0;
			while(cr.nxt != null){
				if(c_idx == index-1){
					break;
				}
				++c_idx;
				cr=cr.nxt;
			}	
			Node cr_two = new Node(e, (cr.nxt));
			//System.out.println( ((cr.nxt).nxt).vl );
			cr.nxt = cr_two;
			cur_ptr = cr;
		} catch(Exception exp){
			exp.printStackTrace();
		}
	}
	//prints content of linked list for testing
	public void printContents(){
		Node cur_ptr = head;
		while(cur_ptr != null){
			System.out.print(cur_ptr.vl + " ");
			cur_ptr = cur_ptr.nxt;
		}
		System.out.println();
	}

	public Node<E> remove(int index){
		Node cur_n = head;
		int idx = 0;
		while(cur_n != null){
			if(idx==index-1){break;}
			++idx;
			cur_n = cur_n.nxt;
		}
		Node tmp = cur_n;
		cur_n.nxt = (cur_n.nxt).nxt;
		cur_ptr = cur_n;
		return (tmp.nxt);
	}
	public int getSize(){return sz;}
	public Node<E> remove_back(){
		int idx = 0;
		Node vls = head;
		while(vls.nxt!=null){
			//System.out.println(idx);
			if(idx==sz-2){break;}
			++idx;
			vls=vls.nxt;
		}
		Node tmp = vls;
		tmp.nxt=null;
		cur_ptr=tmp;
		--sz;
		return tmp;
	}
	public double ticker_contains(String company){
		Node vls = head;
		while(vls.nxt != null){
			Ticker t = (Ticker)(vls.vl);
			if(t.comp.equals(company)){
				return t.stock;
			}
			vls=vls.nxt;
		}
		return -1;
	}

}

class Node<E>{
	public E vl;
	public Node nxt;
	public Node(E e, Node nxt){
		vl=e;
		this.nxt=nxt;
	}
}


class HashTable<E>{
	int sz;
	LinkedList[] tbl;
	public HashTable(int sz){
		this.sz = sz;
		tbl = new LinkedList[sz];
	}
	public void init(){
		System.out.println(sz);
		for(int i = 0; i < sz; i++){
			tbl[i] = new LinkedList<E>();
			//System.out.println(i);
		}
	}
	public int hashCode(E enc){
		//System.out.println(sz);
		int sm = 0;
		for(int i = 0; i < ((Ticker)(enc)).comp.length(); i++){
			sm +=  (int)( (((Ticker)(enc)).comp).charAt(i)); 
		}
		LinkedList sub_tbl = tbl[sm%sz];
		System.out.println(sm%sz);
		sub_tbl.add(enc);
		tbl[sm%sz]=sub_tbl;
		return (sm % sz);
	}

	public void print_cont(){
		for(LinkedList lm : tbl){
			lm.printContents();
		}
	}

	public double find_price(E val){
		for(int idx = 0; idx < tbl.length; idx++){
			if(tbl[idx].getSize()==0){continue;}
			String prs = ((Ticker)(val)).comp;
			double stk_price = tbl[idx].ticker_contains(prs);
			if(stk_price != -1){return stk_price;}
		}
		return -1;
	}

}


class HashNode<K,V>{
	K num;
	V vl;
	public HashNode(K num, V vl){
		this.num=num;
		this.vl=vl;
	}
}

class Ticker{
	String comp;
	double stock;
	public Ticker(String comp, double stock){
		this.comp=comp;
		this.stock=stock;
	}
}


public class Hashtable_Tester{
	 public static String getQuote(String sym) { 
        
        String csvString = "";
        URL url = null;
        URLConnection urlConn = null;
        InputStreamReader inStream = null;
        BufferedReader buff = null;
        try{
            url = new URL("https://financialmodelingprep.com/api/v3/quote/"+ sym + "?apikey=48202bd3a7899a349eaf3e34b270ee95" );
            urlConn = url.openConnection();
            inStream = new InputStreamReader(urlConn.getInputStream());
            buff= new BufferedReader(inStream);
            // get the quote as a csv string
            String s;
            while ((s=buff.readLine())!=null){ 
               csvString += buff.readLine();

            }
            //System.out.println("END: " + csvString);
	    return csvString;
        } catch(Exception e){
            System.out.println("ERROR, no info");

        }
	return "";

    }

	public static void main(String[] args){
		HashTable<Ticker> hsh = new HashTable<Ticker>(20);
		hsh.init();
		String[] tickers = new String[20];
		tickers[0] = "AAPL";
		tickers[1] = "MSFT";
		tickers[2] = "AMZN";
		tickers[3] = "GOOG";
		tickers[4] = "FB";
		tickers[5] = "CMCSA";
		tickers[6] = "V";
		tickers[7] = "WMT";
		tickers[8] = "JNJ";
		tickers[9] = "TSLA";
		tickers[10] = "PEP";
		tickers[11] = "COST";
		tickers[12] = "NVDA";
		tickers[13] = "HD";
		tickers[14] = "UNH";
		tickers[15] = "JPM";
		tickers[16] = "VZ";
		tickers[17] = "T";
		tickers[18] = "NFLX";
		tickers[19] = "XOM";
		for(int i = 0; i < 20; i++){
			String s = getQuote(tickers[i]);
			int idx = s.indexOf(":");
			int idx2 = s.indexOf(",");
			String symbol = s.substring(idx+3,idx2-1);
			s=s.substring(idx2+1);
			int idx3 = s.indexOf(":");
			int idx4 = s.indexOf(",");
			String price = s.substring(idx3+1,idx4);
			System.out.println(symbol + " " + price);
			hsh.hashCode(new Ticker(symbol,Double.parseDouble(price)));
		}

		hsh.find_price(new Ticker("NFLX",0));




		
		
	}

}
