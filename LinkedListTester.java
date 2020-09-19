import java.util.*;
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


class Bubble{
	public int x, y;
	public int rad;
	public Color col;
	public int vx, vy;
	public Bubble(int x,int y, Color col, int vx, int vy, int rad){
		this.x=x;
		this.y=y;
		this.col=col;
		this.rad=rad;
		this.vx = vx;
		this.vy = vy;
	}
	public void inc(){
		x += vx;
		y += vy;
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

}

class Node<E>{
	public E vl;
	public Node nxt;
	public Node(E e, Node nxt){
		vl=e;
		this.nxt=nxt;
	}
}


class Queue<E>{
	LinkedList<E> llm = new LinkedList();
	int M_SIZE;
	int c_size;
	public Queue(int M_SIZE){
		this.M_SIZE = M_SIZE;
		c_size = 0;
	}
	public void insert(E e){
		while(c_size>=M_SIZE){
			llm.remove_back();
			--c_size;
		}
		llm.addToFront(e);
		++c_size;
	}
	public void add(E e){
		llm.addToFront(e);
		++c_size;
		
	}
	public void remove(E e){
		llm.removeFront();
		--c_size;
	}
	public E get(int idx){
		Node<E> cur = llm.head;
		int c_idx = 0;
		while(cur.nxt != null){
			if(c_idx==idx){break;}
			++c_idx;
			cur=cur.nxt;
		}
		return cur.vl;
	}
	public boolean isFull(){return c_size==M_SIZE;}
	public void printContents(){llm.printContents();}
	public boolean contains(E e){return llm.contains(e);}
	public int getSize(){return c_size;}
	
	//specific to handling bubbles onwards
	//
	public void iterative_add(){
		Node<E> cur = llm.head;
		while(cur.nxt != null){
			((Bubble)((cur.vl))).inc();
			det_collision(cur.vl);
			cur=cur.nxt;
		}
	}
	public void iterative_paint(Graphics2D g2){
		if(g2==null){return;}
		Node<Bubble> cur = llm.head;
		while(cur.nxt!=null){
			Bubble bub = (Bubble)(cur.vl);
			g2.setColor(bub.col);
			System.out.println("set color");
			g2.fillOval(bub.x,bub.y,bub.rad,bub.rad );
			cur=cur.nxt;
		}
	}

	public void det_collision(E e){
		Node<E> cur = llm.head;
		while(cur.nxt!=null){
			collides(cur.vl);
			cur=cur.nxt;
		}
	}

	public boolean collides(E e){
		Bubble bub_2 = ((Bubble)(e));
		if( (bub_2.x <= bub_2.rad) ){
			bub_2.vx *= -1;
			return true;
		} else if(bub_2.x >= 1400-bub_2.rad){
			bub_2.vx *= -1;
			return true;
		} else if(bub_2.y <= bub_2.rad){
			bub_2.vy *= -1;
			return true;
		} else if(bub_2.y >= 800-bub_2.rad){
			bub_2.vy *= -1;
			return true;
		}
		return false;
	}


}


public class LinkedListTester{
	private JFrame frame;
	static boolean c_v = false;
	public static Queue<Bubble> q = new Queue<Bubble>(10);//maintaining relative order for removal
	public LinkedListTester() {
        	frame = new JFrame("Bubbles Program");
        	frame.setSize(1400, 800);
        	frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        	frame.setPreferredSize(frame.getSize());
        	frame.add(new InnerProgram(frame.getSize()));
        	frame.pack();
        	frame.setVisible(true);
    	}

    	public static void main(String... argv) {
        	new LinkedListTester();
    	}
    
    	public static class InnerProgram extends JPanel implements Runnable, MouseListener  {

       
        	private Thread animator;
        	Dimension d;
        	String str = "";
        	int xPos = 0;
        	int yPos = 0;
        
      
      
        
        	public InnerProgram (Dimension dimension) {
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
			//System.out.println(q.getSize());
            		Graphics2D g2 = (Graphics2D)g;
            		g2.setColor(Color.black);
            		g2.fillRect(0, 0,(int)d.getWidth() , (int)d.getHeight());
            		Color co = new Color(255,0,255);
			g2.setColor(co);
            		int fontSize = 10;
            		g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
            		g2.drawString("String " + str,20,40);
			if(q.getSize()>0){
				q.iterative_paint(g2);
				q.iterative_add();
			}
                    	//g2.fillOval(xPos,yPos,100,100);

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
			int randR = (int)(Math.random() * 255);
			int randG = (int)(Math.random() * 255);
			int randB = (int)(Math.random() * 255);
			int unsigned_vx = (int)(Math.random() * 3)+1;
			int unsigned_vy = (int)(Math.random() * 3)+1;
			int radii = (int)(Math.random()*150);
			int r1 = (int)(Math.random()*2)+1;
			int r2 = (int)(Math.random()*2)+1;
			System.out.println(r1 + " " + r2);
			if(r1%2==0){
				unsigned_vx *= -1;
			}
			if(r2%2==0){
				unsigned_vy *= -1;
			}
			q.insert(new Bubble(e.getX(),e.getY(),new Color(randR,randG,randB),unsigned_vx,unsigned_vy,radii));
        	}
        	private class TAdapter extends KeyAdapter {

            		public void keyReleased(KeyEvent e) {
                		int keyr = e.getKeyCode();
            		}

            		public void keyPressed(KeyEvent e) {
                		int key = e.getKeyCode();
            		}
        	}

       	 public void run() {
            long beforeTime, timeDiff, sleep;
            beforeTime = System.currentTimeMillis();
            int animationDelay = 5;
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
        
       
	}
	
}

