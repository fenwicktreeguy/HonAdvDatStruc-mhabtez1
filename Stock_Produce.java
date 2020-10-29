import java.io.*;
import java.util.*;
import java.net.*;

class Pair{
	double money;
	int buy;
	public Pair(double money, int buy){
		this.money=money;
		this.buy=buy;
	}
}

public class Stock_Produce{
	public static HashMap<String,Double> min_indicator = new HashMap<String,Double>();
	public static HashMap<String,Double> max_indicator = new HashMap<String,Double>();

	public static HashMap<String,Double> past_buys = new HashMap<String,Double>();
	public static HashMap<String, ArrayList<Double> > cur_cmp_wts = new HashMap<String,ArrayList<Double> >();
	public static HashMap<String,Boolean> already_bought = new HashMap<String,Boolean>();

	public static String[] indicator = new String[11];

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
	

    //normalizes each value from [0,1] so data is consistent
    public static double normalized_value(double x, String indicator){
	    double ret = (double)(x-min_indicator.get(indicator))/(double)(max_indicator.get(indicator)-min_indicator.get(indicator));
	    //System.out.println("NORMALIZED: " + ret);
	    return ret;
    }

    public static boolean buy_or_not(double open, double high, double low, double close){
	    boolean diff = Math.abs( open - (high+low)/2)<=2;
	    boolean diff2 = Math.abs(open - close)<=2;
	    return (diff&&diff2);
	
    }

    public static void buy_or_sell(String company, double param){
	    if(param>=1.9 && (already_bought.get(company)==null||already_bought.get(company)==false)){
		already_bought.put(company,true);
	    } else if(param <= 1.6 && already_bought.get(company)==true){
		already_bought.put(company,false):
	    }
    }

   public static Pair fitness_function(HashMap<String,Double> genes_two){
	   double net_val = 0;
	   int successful_buy=0;
	   double money=0;
	   double parsed=0,vel=0;
	   double open=0,high=0,low=0,close=0;

	   try{
		String t = "";
	        BufferedReader ht = new BufferedReader(new FileReader("historical.txt"));
	   	while( (t=ht.readLine())!=null){
			t=t.replaceAll("\\s","");
			//System.out.println(t);
			if(t.equals("},{")){
				System.out.println("VEL: " + net_val);
				boolean b = buy_or_not(open,high,low,close);
				if(b&&net_val>=1.6){
				  	successful_buy++;
					money += (Math.random()*low)+high;			
				}
				open=0;
				close=0;
				high=0;
				low=0;
				net_val=0;
			}

			if(t.indexOf(':')==-1){continue;}
			String pref = t.substring(1,t.indexOf(':')-1);
			String suff = t.substring(t.indexOf(':')+1);
			if(suff.charAt(suff.length()-1)==','){
				suff=suff.substring(0,suff.length()-1);
			}
			switch(pref){
				case "open":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					open=parsed;
					vel = normalized_value(parsed,"open");
					net_val += genes_two.get("open")*vel;
					break;
				case "high":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					high=parsed;
					vel = normalized_value(parsed,"high");
					net_val += genes_two.get("high")*vel;
					break;
				case "low":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					low=parsed;
					vel = normalized_value(parsed,"low");
					net_val += genes_two.get("low")*vel;
					break;
				case "close":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					close=parsed;
					vel = normalized_value(parsed,"close");
					net_val += genes_two.get("close")*vel;
					break;
				case "adjClose":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					vel = normalized_value(parsed,"adjClose");
					net_val += genes_two.get("adjClose")*vel;
					break;
				case "volume":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					vel = normalized_value(parsed,"volume");
					net_val += genes_two.get("volume")*vel;
					break;
				case "unadjustedVolume":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					vel = normalized_value(parsed,"unadjustedVolume");
					net_val += genes_two.get("unadjustedVolume")*vel;
					break;
				case "change":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					vel = normalized_value(parsed,"change");
					net_val += genes_two.get("change")*vel;
					break;
				case "changePercent":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					vel = normalized_value(parsed,"changePercent");
					net_val += genes_two.get("changePercent")*vel;
					break;
				case "vwap" :
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					vel = normalized_value(parsed,"vwap");
					net_val += genes_two.get("vwap")*vel;
					break;
				case "changeOverTime":
					parsed= Double.parseDouble(suff);
					//System.out.println(parsed);
					vel = normalized_value(parsed,"changeOverTime");
					net_val += genes_two.get("changeOverTime")*vel;
					break;
			}
		
		}
	 } catch(Exception e){
		e.printStackTrace();
	 }
	 
	return new Pair(money,successful_buy);
   }

	
   public static void genetic_algorithm(HashMap<String,Double> genes, HashMap<String,Double> genes_two, int generations){	
	if(generations>100){return;}
	/*
	System.out.println("MALE GENES: ");
	Set<String> m = genes.keySet();
	Set<String> n = genes_two.keySet();
	for(String sm : m){
		System.out.println(genes.get(sm));
	}
	System.out.println("FEMALE GENES: ");
	for(String sm : n){
		System.out.println(genes_two.get(sm));
	}
	System.out.println();
	*/
	
	//for crossover event
	boolean[] vis = new boolean[28];
	ArrayList<String> gene_switches = new ArrayList<String>();
	int num_crossover = (int)(Math.random()*6);
	for(int i = 0; i < num_crossover; i++){
		int rand = (int)(Math.random()*11);
		while(vis[rand]){
			rand=(int)(Math.random()*11);
		}
		vis[rand]=true;
		gene_switches.add(indicator[rand]);
	}

	 Pair male_res = fitness_function(genes);
	 Pair female_res= fitness_function(genes_two);
	
	 //selection event
	 if(male_res.money/male_res.buy > female_res.money/female_res.buy){
		 System.out.println("MALE CHOSEN");
		 for(String s : gene_switches){
			 System.out.println(s);
			 double arbiter = Math.random();
			 //for mutation event
			 if(arbiter < 0.1){
			 	genes.put(s,genes_two.get(s) + (Math.random()) * (1-genes_two.get(s))) ;
			 }
		 }
		 genetic_algorithm(genes,genes_two,generations+1);
	 } else {
		 System.out.println("FEMALE CHOSEN");
		 for(String s : gene_switches){
			 System.out.println(s);
			 double arbiter= Math.random();
			 if(arbiter < 0.1){
			 	genes_two.put(s,genes.get(s) + (Math.random())*(1-genes_two.get(s)) );
			 }
		 }
		 genetic_algorithm(genes,genes_two,generations+1);
	 }
   }

   //theoretically, the best next approach from here is to try to remap the weights from the 11 features
   //before to the 28 features the API gives us by some querying of the API(never mind this doesnt matter)

   public static void weight_precomputation(){
	   	try{
			BufferedReader bf = new BufferedReader(new FileReader("companies.txt"));
			FileWriter wt = new FileWriter("weights.txt",true);
			BufferedReader ht = new BufferedReader(new FileReader("historical.txt"));
			//System.out.println(mp);

			String s ="";
			String t= "";
			while( (s=bf.readLine())!=null){
				String json_string = (getQuote(s));
				//System.out.println(json_string);
			}
	
			indicator[0]="open";
			indicator[1]="high";
			indicator[2]="low";
			indicator[3]="close";
			indicator[4]="adjClose";
			indicator[5]="volume";
			indicator[6]="unadjustedVolume";
			indicator[7]="change";
			indicator[8]="changePercent";
			indicator[9]="vwap";
			indicator[10]="changeOverTime";
			HashMap<String,Double> init_genes = new HashMap<String,Double>();
			HashMap<String, Double> init_two = new HashMap<String,Double>();
			for(int i = 0; i < 11; i++){
				double pt = Math.random();
				double pt_2 = Math.random();
				System.out.print(pt + " ");
				init_genes.put(indicator[i],pt);
				init_two.put(indicator[i],pt_2);

				//for normalizing our data between 0-1 (makes our algo work better)
				min_indicator.put(indicator[i],(double)(100000000));
				max_indicator.put(indicator[i],(double)(-100000000));
			}
			System.out.println();
			genetic_algorithm(init_genes,init_two,0);
			System.out.println("FINAL MALE GENES:");
			String write_one = "";
			for(String ind: indicator){
				System.out.print(init_genes.get(ind) + " ");
				write_one += init_genes.get(ind);
				write_one += " ";
			}
			String write_two = "";
			ArrayList<Double> cur_wts = new ArrayList<Double>();
			System.out.println();
			System.out.println("FINAL FEMALE GENES:");
			for(String ind: indicator){
				System.out.print(init_two.get(ind) + " ");
				write_two += init_two.get(ind);
				cur_wts.add(init_two.get(ind));
				write_two += " ";
			}
			while( (s=bf.readLine())!=null){
				cur_cmp_wts.put(s,cur_wts);
			}

			
			if(new File("weights.txt").length() == 0){
				for(int i = 0; i < indicator.length-1; i++){
					wt.write(write_one);
					wt.write("\n");
				}
				wt.write("\n");
				wt.write("\n");
				for(int i = 0; i < indicator.length-1; i++){
					wt.write(write_one);
					wt.write("\n");
				}
			}

			wt.close();


		} catch(Exception e){
			e.printStackTrace();
		}

   }


   public ArrayList<Double> remap_wts(ArrayList<Double> eleven_wts){
	return new ArrayList<Double>();
   }
   public static void main(String[] args){
		weight_precomputation();
		//with computed weights, query API and use weighs to determine buys and sells
		try{
			BufferedReader cmp = new BufferedReader(new FileReader("companies.txt"));
			String cmp_str="";
			while( (cmp_str=cmp.readLine())!=null){
				String json_string = getQuote(cmp_str);
				System.out.println();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

     				
   }
}
