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

class Character_Wrapper{
	char c;
	int x, y;
	Color cl;
	int font_size;
	public Character_Wrapper(char c, int x, int y, Color cl, int font_size){
		this.c=c;
		this.x=x;
		this.y=y;
		this.cl = cl;
		this.font_size = font_size;
	}
	void inc_font_size(int amt){
		font_size += amt;
	}
}



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

	public E get(int idx){
		Node<E> cur_n = head;
		int pt = 0;
		while(cur_n != null){
			if(pt==idx){break;}
			++pt;
			cur_n=cur_n.nxt;
		}
		return cur_n.vl;
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
		Node<E> vls = head;
		if(sz==1){ 
			char w = ((Character_Wrapper)(head.vl)).c;
			System.out.println("CHARACTER REMAINING: " + w);
			Node<E> tmp = head; head = null; cur_ptr = null; return tmp;
		}
		while(vls.nxt!=null){
			if(idx==sz-2){break;}
			++idx;
			vls=vls.nxt;
		}
		Node<E> tmp = vls;
		System.out.println();
		Node ret = vls.nxt;
		tmp.nxt=null;
		cur_ptr=tmp;
		--sz;
		return ret;
	}

}



class Stack<E>{
	LinkedList<E> llm = new LinkedList<E>();
	int S_SIZE =0;
	public void push(E v){
		llm.add(v);
		++S_SIZE;
	}
	public E pop(){
		--S_SIZE;
		return (llm.remove_back()).vl;
	}
	public E get(int index){
		Node<E>cr = llm.head;
	       	int idx = 0;
		while(cr.nxt!=null){
			if(idx==S_SIZE-1-index){
				break;
			}
			cr=cr.nxt;
			idx++;
		}
		return cr.vl;
	}
	public int getSize(){
		return S_SIZE;
	}
	public void inc_size_case(int font_size){
		for(int i = 0; i < llm.getSize(); i++){
			((Character_Wrapper)(llm.get(i))).inc_font_size(font_size);
		}
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




public class StackRunner{
    private JFrame frame;

    public StackRunner() {
        frame = new JFrame("Bubbles Program");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.add(new Word(frame.getSize()));
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String... argv) {
        new StackRunner();
    }
    
    public static class Word extends JPanel implements Runnable, MouseListener  {
	
	static int START_X = 20;
    	static int START_Y = 80;
	static int prev_size = 0;
	static int cur_size = 0;
       
	public static void paint_chars(Graphics g, Stack<Character_Wrapper> stk){
		Graphics2D g2 = (Graphics2D)g;
		for(int i = 0; i < stk.getSize(); i++){
			g2.setColor(stk.get(i).cl);
			g2.setFont(new Font("TimesRoman", Font.PLAIN, stk.get(i).font_size));
			g2.drawString(String.valueOf(stk.get(i).c), stk.get(i).x, stk.get(i).y);
		}
	}
	

	public static Stack<Character_Wrapper> stk = new Stack<Character_Wrapper>();


        private Thread animator;
        Dimension d;
        String str = "";
        int xPos = 0;
        int yPos = 0;
        int fontSize = 20;
       
        Color co = new Color(255,255,255);
        Color[] coArray = {
        new Color(255,255,255), new Color(0,255,255), new Color(255,255,0),new Color(255,0,255),new Color(0,0,255)	
        };
      
      
      
        
        public Word (Dimension dimension) {
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
            g2.setColor(co);
	    if(stk.getSize()>0){
            	paint_chars(g,stk);
	    }
	}

  

        public void mousePressed(MouseEvent e) {
            xPos = e.getX();
            yPos = e.getY();
            
            
       
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
             

               int kkey = e.getKeyChar();
               String   cc = Character.toString((char) kkey);
               str = " " + kkey;
	       if(kkey==8){//delete key
		       System.out.println("run");
		       stk.pop();
		       START_X -= 18;
	      } else if(e.getKeyCode()==KeyEvent.VK_UP){
		       System.out.println("up");
		       stk.inc_size_case(1);
		       for(int i = 0; i < stk.getSize(); i++){
			       System.out.print(stk.get(i).c + " " );
		       }
		       System.out.println();
	       } else if(e.getKeyCode()==KeyEvent.VK_DOWN){
		       System.out.println("down");
		       stk.inc_size_case(-1);
	       } else {
		Color pass = new Color( (int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255) );
		if(START_X >= (int)(d.getWidth()) - 30){
			START_X=20;
			START_Y += 20;
		}
	       	stk.push(new Character_Wrapper( (char)(kkey), START_X+= 18, START_Y, pass, fontSize) );
	       }

	   }


               
               //key events related to strings below. You should NOT need
               // int key = e.getKeyCode();
               //String c = KeyEvent.getKeyText(e.getKeyCode());
               // String   c = Character.toString((char) key);
        }//end of adapter

        public void run() {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 10;
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
