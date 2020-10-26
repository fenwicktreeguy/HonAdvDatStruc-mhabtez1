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

	public void self_balancing(){
		
	}
}

class Pair{
	char c;
	int amt;
	public Pair(char c, int amt){
		this.c=c;
		this.amt=amt;
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
	HashMap<Character,Integer>occ;
	HashMap<Character,String> char_compressions;

	public HuffmanTree(String encode){
		this.encode=encode.toLowerCase();
		btree=null;
		char_occs = new ArrayList<Pair>();
		occ=new HashMap<Character,Integer>();
		char_compressions = new HashMap<Character,String>();
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
		//assign root of tree before to avoid NPE(and add char element of Node so we know the character and value)
		btree = new BST(new Node('_',char_occs.get(0).amt+char_occs.get(1).amt));
		if(char_occs.get(1).amt <= char_occs.get(0).amt){
			btree.root.left=new Node(char_occs.get(1).c, char_occs.get(1).amt);
			btree.root.right=new Node(char_occs.get(0).c, char_occs.get(0).amt);
		} else {
			btree.root.left=new Node(char_occs.get(0).c, char_occs.get(0).amt);
			btree.root.right=new Node(char_occs.get(1).c, char_occs.get(1).amt);
		}
	}
	public void build(){
		for(int i = 2; i < char_occs.size(); i++){
			//assign left/right nodes based on value
			if(btree.root.vl<=char_occs.get(i).amt){
				Node tmp = btree.root;
				btree.root=new Node('_', btree.root.vl+char_occs.get(i).amt);
				btree.root.left=tmp;
				btree.root.right=new Node(char_occs.get(i).c,char_occs.get(i).amt);
			} else {
				Node tmp = btree.root;
				btree.root=new Node('_', btree.root.vl+char_occs.get(i).amt);
				btree.root.right=tmp;
				btree.root.left=new Node(char_occs.get(i).c, char_occs.get(i).amt);
			}
		}
	}
	public void query(String path, Node cur){
		if(cur.left != null){
			query('0'+path, cur.left);	
		}
		if(cur.right != null){
			query('1'+path,cur.right);
		}
		char_compressions.put(cur.c,path);
	}

	public void show_compressions(){
		Set<Character> keySet = char_compressions.keySet();
		for(Character chr : keySet){
			System.out.println(chr + " " + char_compressions.get(chr));
		}
	}
	
}

public class BBST{
	public static void main(String[] args){

		//HUFFMAN TREE STUFF
		
		/*
		HuffmanTree hf = new HuffmanTree("Chicken Nugget");
		hf.populate();
		hf.build();
		hf.btree.show(hf.btree.root);
		hf.query("",hf.btree.root);
		hf.show_compressions();
		*/
		

		
		
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
		*/
		
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
		
		
		
	}

}


