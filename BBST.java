import java.util.*;
import java.io.*;


class Node{
	char c;
	int vl;
	Node left;
	Node right;
	public Node(char c, int vl){
		this.vl=vl;
		this.c=c;
		left=null;
		right=null;
	}
}


class BST{
	Node root;
	public BST(Node root){
		this.root=root;
	}
	public void insert(Node cur, int val){
		if(val<cur.vl){
			if(cur.left!=null){
				insert(cur.left,val);
			} else{
				cur.left=new Node('_',val);

			}
		} else if(val>cur.vl){
			if(cur.right!=null){
				insert(cur.right,val);
			} else {
				cur.right=new Node('_',val);
			}
		}
	}
	public void show(Node cur){
		if(cur!=null){
			System.out.println("PARENT NODE: " + cur.vl);
		}
		if(cur.left!=null){
			System.out.println("LEFT NODE: " + cur.left.vl);
			if(cur.right!=null){
				System.out.println("RIGHT NODE: " + cur.right.vl);
				show(cur.left);
				show(cur.right);
			}
	        } else {
			if(cur.right != null){
				System.out.println("RIGHT NODE: " + cur.right.vl);
				show(cur.right);
			}
		}
	}

	public void balance(int[] array, int l, int r){
		if(l>=r){return;}
		int md = l+(r-l)/2;
		insert(root,array[md]);
		balance(array,l,md);
		balance(array,md+1,r);
	}


}


class Pair{
	char c;
	int amt;
	Node pot_root;
	public Pair(char c, int amt){
		this.c=c;
		this.amt=amt;
		pot_root = null;
	}
	public Pair(char c, int amt, Node pot_root){
		this.c=c;
		this.amt=amt;
		this.pot_root=pot_root;
	}

}

class Pair_Sort implements Comparator<Pair>{
	public int compare(Pair one, Pair two){
		if(one.amt > two.amt){
			return 1;
		} else {
			return -1;
		}
	}
}

class HuffmanTree{
	String encode;
	BST btree;
	ArrayList<Pair> char_occs;
	HashMap<Character,Integer> occ;
	public HashMap<Character,String> char_compressions;
	public HashMap<String,Character> char_decoding;
	String tot_path = "";

	public HuffmanTree(String encode){
		this.encode=encode;
		btree=null;
		char_occs = new ArrayList<Pair>();
		occ=new HashMap<Character,Integer>();
		char_compressions = new HashMap<Character,String>();
		char_decoding= new HashMap<String, Character>();
	}
	public void populate(){
		for(int c = 0; c < encode.length(); c++){
			if(occ.get(encode.charAt(c))==null){
				occ.put(encode.charAt(c),1);
			} else {
				occ.put(encode.charAt(c),occ.get(encode.charAt(c))+1);
			}
		}
		Set<Character> keys = occ.keySet();
		for(Character c : keys){
			char_occs.add(new Pair(c,occ.get(c)));
		}
		Collections.sort(char_occs, new Pair_Sort());
	}
	public void build(){
		//assign the root a dummy value to avoid NPE
		btree =new BST( new Node('_',0) );
		java.util.Queue<Pair> l = new LinkedList<Pair>();
		for(Pair p : char_occs){
			l.add(p);
			System.out.println(p.c + " " + p.amt);
		}

		//in the Pair class, we maintain a tenative pot_root 
		while(l.size() > 0){
			Pair p = l.poll();
			Pair p2 = l.poll();
			if(p==null || p2 == null){break;}
			//System.out.println(p.amt + " " + p2.amt);
			btree.root= new Node('_',p.amt+p2.amt);
			if(p.c != '_'){
				p.pot_root = new Node(p.c,p.amt);
			}
			if(p2.c != '_'){
				p2.pot_root= new Node(p2.c,p2.amt);
			}
	
			if(p.amt<p2.amt){
				btree.root.left=p.pot_root;;
				btree.root.right=p2.pot_root;;
			} else {
				btree.root.left=p2.pot_root;
				btree.root.right=p.pot_root;
			}
			l.add(new Pair('_', p.amt + p2.amt, btree.root) );
		}
	}
	public void query(String path, Node cur){
		if(cur.left != null){
			query(path+'0', cur.left);	
		}
		if(cur.right != null){
			query(path+'1',cur.right);
		}
		//char_compressions.put(cur.c,path);
		String tmp = path;
		if(cur.c != '_'){
			System.out.println(cur.c + " " + path);
			char_compressions.put(cur.c,tmp);
			char_decoding.put(tmp,cur.c);
		}

	}

	public void bin_string(){
		for(int i = 0; i < encode.length(); i++){
			tot_path += char_compressions.get(encode.charAt(i));
		}

	}

	public void show_compressions(){
		Set<Character> keySet = char_compressions.keySet();
		for(Character chr : keySet){
			if(chr=='_'){continue;}
			String tmp = char_compressions.get(chr);
			System.out.println(chr + " " + tmp);
		}
	}

	public String decode(Node cur){
		String res = "";
		int idx = 0;
		int inc = 1;
		while(idx+inc<=tot_path.length()){
			//System.out.println(tot_path.substring(idx,idx+inc));
			//System.out.println(char_decoding.get(tot_path.substring(idx,idx+inc))); 
			if(char_decoding.get(tot_path.substring(idx,idx+inc)) == null){
				inc++;
			} else {
				res += char_decoding.get(tot_path.substring(idx,idx+inc));
				idx=idx+inc;
				inc=1;
			}
		}
		return res;
	}
	
}

public class BBST{
	public static void main(String[] args){

		//HUFFMAN TREE STUFF
		
		
		HuffmanTree hf = new HuffmanTree("This is a test for encoding and decoding information.");
		System.out.println("INPUT PHRASE: " + hf.encode);
		hf.populate();
		hf.build();
		//hf.btree.show(hf.btree.root);
		hf.query("",hf.btree.root);
		//hf.show_compressions();
		hf.bin_string();
		System.out.println(hf.tot_path);
		String s = hf.decode(hf.btree.root);
		System.out.println("OUTPUT PHRASE: " + s);
		
		
		

		
		/*
		
		//inserting into tree
		
		BST b = new BST(new Node('_',1));

		/*
		b.insert(b.root,5);
		b.insert(b.root,2);
		b.insert(b.root,10);
		b.insert(b.root,8);
		b.insert(b.root,6);
		b.insert(b.root,20);
		b.show(b.root);
		
		
		int[] give = new int[20];
		for(int i = 0; i < 20; i++){
			int v =  (int)(Math.random() * 30)+2;
			give[i] = v;
		}
		Arrays.sort(give);
		for(int i : give){System.out.print(i + " ");}
		System.out.println();
		b.balance(give,0,give.length);
		b.show(b.root);
		*/
		
		
	}

}




