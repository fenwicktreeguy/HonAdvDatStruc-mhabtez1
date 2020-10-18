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


class TileObj{
	Color c;
	int x;
	int y;
	int vl;
	boolean occupied;
	public TileObj(Color c, int x, int y, int vl, boolean occupied){
		this.c=c;
		this.x = x;
		this.y = y;
		this.vl=vl;
		this.occupied=false;
	}
}


public class EightTiles{
    private JFrame frame;
    public static TileObj[][] tile = new TileObj[3][3];
    public static int CUR_X = -1;
    public static int CUR_Y = -1;
    public static boolean[] used = new boolean[9];
    public static HashMap<String, Boolean> mp = new HashMap<String, Boolean>();
    public static HashMap<TileObj,TileObj> backtrack = new HashMap<TileObj,TileObj>();//for backtracking

    public EightTiles() {
        frame = new JFrame("Bubbles Program");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(frame.getSize());
        frame.add(new Tile(frame.getSize()));
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String... argv) {
        new EightTiles();
    }

    public static void swap_tiles(TileObj[][] tile, int zero_x, int zero_y, int x_p, int y_p){
	tile[zero_x+x_p][zero_y+y_p].occupied=true;
	tile[zero_x+x_p][zero_y+y_p].c = Color.BLUE;
	tile[zero_x][zero_y].occupied=false;
	tile[zero_x][zero_y].c=Color.RED;
	int tmp = tile[zero_x+x_p][zero_y+y_p].vl;
	tile[zero_x+x_p][zero_y+y_p].vl = tile[zero_x][zero_y].vl;
	tile[zero_x][zero_y].vl=tmp;
    }
    
    public static class Tile extends JPanel implements Runnable, MouseListener  {

       
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

		
	public static boolean found = false;
	public static int n_moves = 1000000000;
	/*
	void pathfind(TileObj[][] tile, int num_moves, int zero_x, int zero_y){
		

		int amt = 0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(tile[j][i].vl==amt){
					amt++;
					continue;

				} else {
					break;
				}
			}
		}
		System.out.println(amt);
		if(amt==9){
			n_moves = Math.min(n_moves,num_moves);
		}
		
		if(zero_x+1<tile[0].length){
			swap_tiles(tile,zero_x,zero_y,1,0);
			char[] parse = new char[9];
			int idx = 0;
			for(int i = 0; i < tile[0].length; i++){
				for(int j = 0; j < tile.length; j++){
					System.out.print(tile[i][j].vl + " ");
					parse[tile[i][j].vl] = idx;
					idx++;
				}
				System.out.println();
			}
			if(mp.get(String.valueOf(parse))==null){
				mp.put(tile,true);
				System.out.println("one");
				backtrack.put(tile[zero_x][zero_y],tile[zero_x+1][zero_y]);
				zero_x++;
				pathfind(tile,num_moves+1,zero_x,zero_y);
			}
		}
		if(zero_x-1>=0){
			swap_tiles(tile,zero_x,zero_y,-1,0);
			char[] parse = new char[9];
			int idx = 0;
			for(int i = 0; i < tile[0].length; i++){
				for(int j = 0; j < tile.length; j++){
					System.out.print(tile[i][j].vl + " ");
					parse[tile[i][j].vl] = (char)(idx+48);
					idx++;
				}
				System.out.println();
			}

			if(mp.get(String.valueOf(parse))==null){
				mp.put(String.valueOf(parse),true);
				System.out.println("two");
				backtrack.put(tile[zero_x][zero_y],tile[zero_x-1][zero_y]);
				zero_x--;
				pathfind(tile,num_moves+1,zero_x,zero_y);
			}
		}
		if(zero_y+1<tile.length){
			swap_tiles(tile,zero_x,zero_y,0,1);
			char[] parse = new char[9];
			int idx = 0;
			for(int i = 0; i < tile[0].length; i++){
				for(int j = 0; j < tile.length; j++){
					System.out.print(tile[i][j].vl + " ");
					parse[tile[i][j].vl] = (char)(idx+48);
					idx++;
				}
				System.out.println();
			}

			if(mp.get(String.valueOf(parse))==null){
				mp.put(String.valueOf(parse),true);
				System.out.println("three");
				backtrack.put(tile[zero_x][zero_y],tile[zero_x][zero_y+1]);
				zero_y++;
				pathfind(tile,num_moves+1,zero_x,zero_y);
			}
		}
		if(zero_y-1>=0){
			swap_tiles(tile,zero_x,zero_y,0,-1);
			char[] parse = new char[9];
			int idx = 0;
			for(int i = 0; i < tile[0].length; i++){
				for(int j = 0; j < tile.length; j++){
					System.out.print(tile[i][j].vl + " ");
					parse[tile[i][j].vl] = (char)(idx+48);
					idx++;
				}
				System.out.println();
			}

			if(mp.get(String.valueOf(parse))==null){
				mp.put(String.valueOf(parse),true);
				System.out.println("four");
				backtrack.put(tile[zero_x][zero_y],tile[zero_x][zero_y-1]);
				zero_y--;
				pathfind(tile,num_moves+1,zero_x,zero_y);
			}
		}
		System.out.println("ERROR");	
	}
    */

      
              
        public Tile(Dimension dimension) {
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
	    int rw = 100;
	    int cl = 100;
	    int wid = 50;
	    for(int i = 0; i < 3; i++){
		    for(int j = 0; j < 3; j++){
			    int rand = (int)(Math.random() * 9);
			    while(used[rand]){
				    rand=(int)(Math.random()*9);
			    }
			    used[rand]=true;
			    if(rand==0){
				    CUR_X=i; 
				    CUR_Y=j;
				    tile[CUR_X][CUR_Y]=new TileObj(Color.BLUE,rw,cl,rand,true);
			    } else {
			    	    tile[i][j] = new TileObj(Color.RED,rw,cl,rand,false);
			    }
			    cl += wid+10;
		    }
		    rw+=wid+10;
		    cl=100;
	    }
	    /*
	    TileObj[][] cp = new TileObj[3][3];
	    for(int i = 0; i < 3; i++){
		    for(int j = 0; j < 3; j++){
			    cp[i][j] = new TileObj(tile[j][i].c,tile[j][i].x,tile[j][i].y,tile[j][i].vl,tile[j][i].occupied);
		    }
	    }
	    //pathfind(cp,0,CUR_Y,CUR_X);
	    System.out.println(n_moves);
	    */
            setDoubleBuffered(true);

        }

        @Override
        public void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D)g;
            g2.setColor(Color.black);
            g2.fillRect(0, 0,(int)d.getWidth() , (int)d.getHeight());
	    
	    int row = 100;
	    int col = 100;
	    int widt = 50;
	    int amt = 1;


	   		
    
	    for(int i = 0; i < 3; i++){
		    for(int j = 0; j < 3; j++){
			    int num = (int)(Math.random() * 9);
			    if(tile[i][j].occupied){
				g2.setColor(Color.BLUE);
				g2.fillRect(tile[i][j].x,tile[i][j].y,50,50);
				g2.setColor(Color.WHITE);
			    	g2.drawString(Integer.toString(tile[i][j].vl), row+30,col+30);
			    } else {
			    	g2.setColor(tile[i][j].c);
			    	g2.fillRect(tile[i][j].x,tile[i][j].y,50,50);
			    	g2.setColor(Color.WHITE);
			    	g2.drawString(Integer.toString(tile[i][j].vl), row+30,col+30);
			    }
			    col+=60;
		    }
		    row+=60;
		    col=100;
	    }
           
            g2.setColor(co);
           
            g2.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
            //g2.drawString("String " + str,20,40);
        }
	
	//put grid in proper ordering
	

	void area(int posX, int posY){
		int row = 100;
		int col = 100;
		int widt = 50;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				if(posX >= row && posX <= row + 60 && posY >= col && posY <= col+60){
					System.out.println(i + " " + j + " "  + CUR_X + " " + CUR_Y);
					if( Math.abs(i-CUR_X)+Math.abs(j-CUR_Y)==1){
						tile[i][j].occupied=true;
						tile[i][j].c = Color.BLUE;
						tile[CUR_X][CUR_Y].occupied=false;
						tile[CUR_X][CUR_Y].c=Color.RED;
						int tmp = tile[CUR_X][CUR_Y].vl;
						tile[CUR_X][CUR_Y].vl = tile[i][j].vl;
						tile[i][j].vl=tmp;
						CUR_X=i;
						CUR_Y=j;
						break;
					}
				}
				col += widt+10;
			}
			row+=widt+10;
			col=100;
		}
	}
  

        public void mousePressed(MouseEvent e) {
            xPos = e.getX();
            yPos = e.getY();
	    System.out.println(xPos + " " + yPos);
	    area(xPos,yPos);
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
               
               //key events related to strings below. You should NOT need
               // int key = e.getKeyCode();
               //String c = KeyEvent.getKeyText(e.getKeyCode());
               // String   c = Character.toString((char) key);
               
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
