
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.io.*;
import java.awt.Font;

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

	//for specofic
	public void direct_insert(){
		char_occs.add(new Pair('y', 2));
		char_occs.add(new Pair('b', 7));
		char_occs.add(new Pair('c', 9));
		char_occs.add(new Pair('d', 18));
		char_occs.add(new Pair('w', 33));
		char_occs.add(new Pair('g', 37));
		char_occs.add(new Pair('r', 38));
		tot_path = "000000111111111100000000000011111111111111111100000001100110011010100111110000000000110100110101010011111010100000011010011001101010100111110101000000110101010100111101111011110111100000000101010101010000000000011110101111010111100000011111101011110101111110011111111010010010010111111111010110100111001001001110010111010101010010010010010010010101010";
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
		java.util.PriorityQueue<Pair> l = new PriorityQueue<Pair>(new Pair_Sort());
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
	private JFrame frame;
	public static Color[][] board = new Color[12][12];
	public BBST(){
		frame = new JFrame("GUIBoard");
        	frame.setSize(800, 800);
        	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        	frame.setPreferredSize(frame.getSize());
        	frame.add(new InnerBoard(frame.getSize()));
        	frame.pack();
        	frame.setVisible(true);
	}
	public static void main(String... argv) {
		HuffmanTree hf = new HuffmanTree("This is a test for encoding and decoding information.");
		System.out.println("INPUT PHRASE: " + hf.encode);
		//hf.populate();
		hf.direct_insert();
		hf.build();
		//hf.btree.show(hf.btree.root);
		hf.query("",hf.btree.root);
		//hf.show_compressions();
		System.out.println(hf.tot_path);
		String s = hf.decode(hf.btree.root);
		System.out.println(s);
		System.out.println("OUTPUT PHRASE: " + s);
		int rw = 0;
		int cl = 0;
		for(int i = 0; i < s.length(); i++){
			Color pt = new Color(0,0,0);
			switch(s.charAt(i)){
				case 'w':
					pt = new Color(255,255,255);
					break;
				case 'r':
					pt = new Color(255,0,0);
					break;
				case 'b':
					pt = new Color(0,0,0);
					break;
				case 'c':
					pt = new Color(165,42,42);
					break;
				case 'g':
					pt = new Color(255,223,0);
					break;
				case 'd':
					pt = new Color(0,0,255);
					break;
				case 'y':
					pt = new Color(255,255,0);
					break;
			}
			board[rw][cl] = pt;
			cl++;
			if(cl==12){
				rw++;
				cl=0;
			}

		}
        	new BBST();
    	}
    
	public static class InnerBoard extends JPanel implements Runnable, MouseListener  {

       
        private Thread animator;
        Dimension d;
        String str = "";
        int xPos = 0;
        int yPos = 0;
        
      
      
        
        public InnerBoard (Dimension dimension) {
            setSize(dimension);
            setPreferredSize(dimension);
            addMouseListener(this);
            addKeyListener(new TAdapter());
            setFocusable(true);
            d = getSize();

            //for animating the screen - you won't need to edit
            if (animator == null) {
                animator = new Thread(this);
                animator.start();
            }
            setDoubleBuffered(true);
        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.black);
            g2.fillRect(0, 0,(int)d.getWidth() , (int)d.getHeight());
            g2.setColor(Color.white);
            g2.fillRect(0, 0, d.width, d.height);
            
	    int rw = 150;
	    int cl = 150;
   		for(int i = 0; i < 12; i++){
			for(int j = 0; j < 12; j++){
				g2.setColor(board[i][j]);
				g2.fillRect(rw, cl, 50, 50);
				rw += 50;
			}
			rw = 150;
			cl += 50;
		}		
             
              //g2.fillRect(75, 75, 375, 50);
              
                
            
        }

  

        public void mousePressed(MouseEvent e) {
            xPos = e.getX();
            yPos = e.getY();
            str = xPos + " " + yPos;
            
       
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
        }

        private class TAdapter extends KeyAdapter {

            public void keyReleased(KeyEvent e) {
                int keyr = e.getKeyCode();

            }

            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
               // String c = KeyEvent.getKeyText(e.getKeyCode());
              //  c = Character.toString((char) key);

               

               
            }
        }//end of adapter

        public void run() {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 37;
            long time = System.currentTimeMillis();
            while (true) {// infinite loop
                // spriteManager.update();
                repaint();
                try {
                    time += animationDelay;
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    System.out.println(e);
                } // end catch
            } // end while loop
        }// end of run
    }//end of class
}


