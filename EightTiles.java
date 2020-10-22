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

class Pair{
	TileObj[][] cur_board;
	int amt;
	public Pair(TileObj[][] cur_board, int amt){
		this.cur_board = cur_board;
		this.amt = amt;
	}
}

class MapPair{
	String s;
	int amt;
	public MapPair(String s, int amt){
		this.amt=amt;
		this.s=s;
	}

}


public class EightTiles{
    private JFrame frame;
    public static TileObj[][] tile = new TileObj[3][3];
    public static int CUR_X = -1;
    public static int CUR_Y = -1;
    public static boolean[] used = new boolean[9];
    public static java.util.TreeMap<String, Boolean> mp = new TreeMap<String, Boolean>();
    //public static java.util.Map<MapPair, MapPair> backtrack = new LinkedHashMap<MapPair,MapPair>();//for backtracking
    //very inefficient backtracking, but optimal map solution is proving to be very annoying
    public static ArrayList<MapPair> mp_pair = new ArrayList<MapPair>();
    public static int idx_route = 0;
    public static boolean board_cur_solvable=false;

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
	int tmp = tile[zero_x+x_p][zero_y+y_p].vl;
	tile[zero_x+x_p][zero_y+y_p].vl = tile[zero_x][zero_y].vl;
	tile[zero_x][zero_y].vl=tmp;
	tile[zero_x+x_p][zero_y+y_p].occupied=true;
	tile[zero_x+x_p][zero_y+y_p].c = Color.BLUE;
	tile[zero_x][zero_y].occupied=false;
	tile[zero_x][zero_y].c=Color.RED;
    }
    
    public static class Tile extends JPanel implements Runnable, MouseListener  {

       
        private Thread animator;
        Dimension d;
        String str = "";
        int xPos = 0;
        int yPos = 0;
        int fontSize = 20;
        public static TileObj[][] goal = new TileObj[3][3];
	public static boolean found = false;
	public static int n_moves = 1000000000;
        Color co = new Color(255,255,255);
        Color[] coArray = {
        new Color(255,255,255), new Color(0,255,255), new Color(255,255,0),new Color(255,0,255),new Color(0,0,255)	
        };
	public static ArrayList<String> desired_route = new ArrayList<String>();

	public static int[][] deserialize(String q){
		int[][] arr = new int[3][3];
		int idx =0;
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				arr[i][j]=(int)(q.charAt(idx))-48;
				idx++;
			}
		}
		return arr;

	}
	public static String serialize(int[][] arr){
		String s = "";
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				s+=(char)(arr[i][j]+48);
			}
		}
		return s;

	}

	
   	 public boolean is_solvable(TileObj[][] board){
		int[][] exc = new int[3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				exc[i][j]=board[i][j].vl;
			}
		}
		String g = serialize(exc);
		int inversions = 0;
		for(int i = 0; i < g.length(); i++){
			for(int j = 0; j < g.length(); j++){
				if(i==j){continue;}
				if( (int)(g.charAt(j))-48==0){continue;}
				if( (int)(g.charAt(j))-48 > (int)(g.charAt(i))-48){
					inversions++;
				}
			}
		}
	   	return (inversions%2==0);
    	}

	
	void randomize(){
		idx_route=0;
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

	    TileObj[][] cp = new TileObj[3][3];
	    int[][] init_state=new int[3][3];
	    for(int i = 0; i < 3; i++){
		    for(int j = 0; j < 3; j++){
			    cp[i][j] = new TileObj(tile[i][j].c,tile[i][j].x,tile[i][j].y,tile[i][j].vl,tile[i][j].occupied);
			    init_state[i][j] = tile[i][j].vl;
		    }
	    }

	    if(is_solvable(cp)){
		board_cur_solvable=true;
	    	pathfind_two(cp);
	    	backtrack(new MapPair("012345678",n_moves),n_moves,0,0,new ArrayList<String>());
	    	System.out.println(n_moves);
            	for(int i = 0; i < 9; i++){used[i]=false;}
	    	Collections.reverse(desired_route);
	    	desired_route.add("012345678");
	    	if(desired_route.size()==1){desired_route.clear();}
	    } else {
		board_cur_solvable=false;
	    }
	


	    

	}

	public static boolean custom_contains(MapPair targ){
		for(MapPair m_p : mp_pair){
			if(m_p.s.equals(targ.s) && m_p.amt==targ.amt){
				return true;
			}
		}
		return false;
	}	
	void backtrack(MapPair init, int opt_len, int tmp_zero_x,int tmp_zero_y, ArrayList<String> past){
		MapPair seed = init;
		//System.out.println("start");
		boolean found = false;

		if(opt_len==0){
			desired_route=past;
			return;
		}

		if(tmp_zero_x+1<3){
			int[][] g = deserialize(seed.s);
			int tmp = g[tmp_zero_x+1][tmp_zero_y];
			g[tmp_zero_x+1][tmp_zero_y]=g[tmp_zero_x][tmp_zero_y];
			g[tmp_zero_x][tmp_zero_y]=tmp;
			//System.out.println(serialize(g));
			if(custom_contains(new MapPair(serialize(g),seed.amt-1))){
				seed=new MapPair(serialize(g),seed.amt-1);
				past.add(seed.s);
				backtrack(seed,opt_len-1,tmp_zero_x+1,tmp_zero_y,past);
			}
		}
		if(tmp_zero_y+1<3 && !found){
			int[][] g = deserialize(seed.s);
			int tmp = g[tmp_zero_x][tmp_zero_y+1];
			g[tmp_zero_x][tmp_zero_y+1]=g[tmp_zero_x][tmp_zero_y];
			g[tmp_zero_x][tmp_zero_y]=tmp;
			//System.out.println(serialize(g));
			if(custom_contains(new MapPair(serialize(g),seed.amt-1))){
				seed=new MapPair(serialize(g),seed.amt-1);
				past.add(seed.s);
				backtrack(seed,opt_len-1,tmp_zero_x,tmp_zero_y+1,past);
			}


		}
		if(tmp_zero_x-1>=0 && !found){
			int[][] g = deserialize(seed.s);
			int tmp = g[tmp_zero_x-1][tmp_zero_y];
			g[tmp_zero_x-1][tmp_zero_y]=g[tmp_zero_x][tmp_zero_y];
			g[tmp_zero_x][tmp_zero_y]=tmp;
			//System.out.println(serialize(g));

			if(custom_contains(new MapPair(serialize(g),seed.amt-1))){
				seed=new MapPair(serialize(g),seed.amt-1);
				past.add(seed.s);
				backtrack(seed,opt_len-1,tmp_zero_x-1,tmp_zero_y,past);
			}
		}
			
		if(tmp_zero_y-1>=0 && !found){
			int[][] g = deserialize(seed.s);
			int tmp = g[tmp_zero_x][tmp_zero_y-1];
			g[tmp_zero_x][tmp_zero_y-1]=g[tmp_zero_x][tmp_zero_y];
			g[tmp_zero_x][tmp_zero_y]=tmp;
			//System.out.println(serialize(g));

			if(custom_contains(new MapPair(serialize(g),seed.amt-1))){
				seed=new MapPair(serialize(g),seed.amt-1);
				past.add(seed.s);
				backtrack(seed,opt_len-1,tmp_zero_x,tmp_zero_y-1,past);
			}
		}
	}
	void pathfind_two(TileObj[][] board){
		mp.clear();
		java.util.Queue<Pair> e = new java.util.LinkedList<Pair>();
		e.add(new Pair(board,0));
		while(e.size() > 0){
			Pair tp = e.poll();
			int zero_x=0,zero_y=0;
			String s="";
			int amt_needed = 0;
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					if(tp.cur_board[i][j].vl == 0){
						zero_x=i;
						zero_y=j;
					}
					//System.out.print(tp.cur_board[j][i].vl + " ");
					if(tp.cur_board[j][i].vl==amt_needed){
						amt_needed++;
					}
					s += (char)(tp.cur_board[j][i].vl + 48);
				}
				//System.out.println();
			}

			if(amt_needed==9){
				//System.out.println("GOAL STATE SET! NUMBER OF MOVES: " + tp.amt);
				for(int i = 0; i < 3; i++){
					for(int j = 0; j < 3; j++){
						goal[i][j] = tp.cur_board[j][i];
					}
				}
				n_moves = Math.min(n_moves,tp.amt);
			}
			
			//System.out.println(s + " " + tp.amt);
			//System.out.println(zero_x + " " + zero_y);

			if(mp.get(s)==null){
				mp.put(s,true);
				if(zero_x+1<3){
				//	System.out.println("ONE");
					TileObj[][] nw = new TileObj[3][3];
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							nw[i][j]=new TileObj(tp.cur_board[i][j].c,tp.cur_board[i][j].x,tp.cur_board[i][j].y,tp.cur_board[i][j].vl,tp.cur_board[i][j].occupied);
						}
					}

					swap_tiles(nw,zero_x,zero_y,1,0);
					String st="";
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							st += (char)(nw[j][i].vl + 48);
						}						
					}
					//System.out.println("INSIDE: " + st + " " + (tp.amt + 1) + " " + s + " " + tp.amt );

					MapPair p1 = new MapPair(st,tp.amt+1);
					MapPair p2 = new MapPair(s,tp.amt);
					mp_pair.add(p1);
					mp_pair.add(p2);
					//backtrack.put(p1,p2);
					e.add(new Pair(nw,tp.amt+1));

				}

				if(zero_y+1<3){
					//System.out.println("TWO");
					TileObj[][] nw = new TileObj[3][3];
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							nw[i][j]=new TileObj(tp.cur_board[i][j].c,tp.cur_board[i][j].x,tp.cur_board[i][j].y,tp.cur_board[i][j].vl,tp.cur_board[i][j].occupied);
						}
					}



					//backtrack.put(new TileObj(tmp.c,tmp.x,tmp.y+1,tmp.vl,tmp.occupied),tmp);
					swap_tiles(nw,zero_x,zero_y,0,1);
					String st="";
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							st += (char)(nw[j][i].vl + 48);
						}						
					}
					//System.out.println("INSIDE: " + st + " " + (tp.amt + 1) + " " + s + " " + tp.amt );

					MapPair p1 = new MapPair(st,tp.amt+1);
					MapPair p2 = new MapPair(s,tp.amt);
					mp_pair.add(p1);
					mp_pair.add(p2);

					//backtrack.put(p1,p2);
					e.add(new Pair(nw,tp.amt+1));

				}

				if(zero_x-1>=0){
					//System.out.println("THREE");
					TileObj[][] nw = new TileObj[3][3];
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							nw[i][j]=new TileObj(tp.cur_board[i][j].c,tp.cur_board[i][j].x,tp.cur_board[i][j].y,tp.cur_board[i][j].vl,tp.cur_board[i][j].occupied);
						}
					}
					swap_tiles(nw,zero_x,zero_y,-1,0);
					String st="";
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							st += (char)(nw[j][i].vl + 48);
						}						
					}
					//System.out.println("INSIDE: " + st + " " + (tp.amt + 1) + " " + s + " " + tp.amt );

					MapPair p1 = new MapPair(st,tp.amt+1);
					MapPair p2 = new MapPair(s,tp.amt);
					mp_pair.add(p1);
					mp_pair.add(p2);

					//backtrack.put(p1,p2);
					//backtrack.put(new TileObj(tmp.c,tmp.x-1,tmp.y,tmp.vl,tmp.occupied),tmp);
					e.add(new Pair(nw,tp.amt+1));

				}
				

				if(zero_y-1>=0){
					//System.out.println("FOUR");
					TileObj[][] nw = new TileObj[3][3];
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							nw[i][j]=new TileObj(tp.cur_board[i][j].c,tp.cur_board[i][j].x,tp.cur_board[i][j].y,tp.cur_board[i][j].vl,tp.cur_board[i][j].occupied);
						}
					}

					swap_tiles(nw,zero_x,zero_y,0,-1);
					String st="";
					for(int i = 0; i < 3; i++){
						for(int j = 0; j < 3; j++){
							st += (char)(nw[j][i].vl + 48);
						}						
					}
					//System.out.println("INSIDE: " + st + " " + (tp.amt + 1) + " " + s + " " + tp.amt );

					MapPair p1 = new MapPair(st,tp.amt+1);
					MapPair p2 = new MapPair(s,tp.amt);
					mp_pair.add(p1);
					mp_pair.add(p2);

					//backtrack.put(p1,p2);
					//backtrack.put(new TileObj(tmp.c,tmp.x,tmp.y-1,tmp.vl,tmp.occupied),tmp);
					e.add(new Pair(nw,tp.amt+1));
				}

			} else {
				//System.out.println("VISITED");
			}
		}
	}
              
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
	    int mt = 0;
	    randomize();
	    
	 	    //System.out.println(CUR_X + " " + CUR_Y);
	    
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
		
	    if(!board_cur_solvable){
		    g2.setColor(Color.WHITE);
		    g2.drawString("UNSOLVABLE!",500,500);
	    } else {
	   

	    if(idx_route<desired_route.size()){
	    System.out.println(desired_route.get(idx_route));
	    int[][] d = deserialize(desired_route.get(idx_route));
	    int z_x=0,z_y=0;
	    for(int i = 0; i < 3; i++){
		for(int j = 0; j < 3; j++){
			if(d[i][j]==0){
				tile[i][j].vl=d[j][i];
				tile[i][j].c=Color.BLUE;
				tile[i][j].occupied=true;
			} else {
				tile[i][j].vl=d[j][i];
				tile[i][j].c=Color.RED;
				tile[i][j].occupied=false;
			}
		}
	     }
	    idx_route++;
	    }

	    }

	   		
    
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

	    	    //animate the backtracking


	    g2.setColor(Color.YELLOW);
	    g2.fillRect(500,200,150,50);
	    g2.setColor(Color.BLACK);
	    g2.drawString("RANDOMIZE", 530, 230);

           
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
	    if(e.getX() >= 500 && e.getX() <= 650 && e.getY() >= 200 && e.getY() <= 250){
		randomize();
	    }

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
            int animationDelay = 92;
            long time = System.currentTimeMillis();
            while (true) {// infinite loop
                // spriteManager.update();
                repaint();
                try {
                    time += animationDelay;
		    //Thread.sleep((long)(1.5));
                    Thread.sleep(Math.max(0, time - System.currentTimeMillis()));
                } catch (InterruptedException e) {
                    System.out.println(e);
                } // end catch
            } // end while loop
        }// end of run

        
       

    }//end of class
}
