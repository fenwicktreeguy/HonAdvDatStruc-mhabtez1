import java.io.*;
import java.util.*;

public class Custom_Checksum{ 
	public static boolean[] is_prime = new boolean[10000001];
	public static int MOD = (int)(1e9+7);
	public static void logn_sieve(int lim){
		for(int i = 0; i <= 10000000; i++){
			is_prime[i]=true;
		}
		is_prime[0]=false;
		is_prime[1]=false;
		for(int i = 2; i*i<= lim; i++){
			if(is_prime[i]){
				for(int j = i*i; j <= lim; j+= i){
					is_prime[j]=false;
				}
			}
		}
	}



	public static void main(String[] args){
		int[] prime_hash = new int[26];		
		try {
			BufferedReader file = new BufferedReader(new FileReader("jawn.txt"));
			String s;
			String f="";
			while( (s = file.readLine()) != null){
				f += s;
			
			}
			System.out.println(f.length());
			f = f.replaceAll("\\s", "");
			System.out.println(f);
			logn_sieve(1000000);
			long checksum = 0;
			int large_primes = 0;
			for(int i = 1000000; i >= 2; i--){
				//System.out.println(large_primes);
				if(large_primes==26){
					break;
				}
				if(is_prime[i]){
					prime_hash[large_primes] = i;
					System.out.println(i);
					large_primes++;
				}
			}
			
			//System.out.println(f.length());
			int sz = f.length();
			System.out.println(sz);
			for(int i = 0; i< sz; i++){
				//System.out.println(i);
				//System.out.println(f.charAt(i));
				long xor = 0;
				//System.out.println( (i%26)+1);
				if( (i%26) + 1 >= prime_hash.length){
					xor=prime_hash[i%26];
				} else {
					xor=(long)(prime_hash[(i%26)])^(long)(prime_hash[(i%26)+1]);
				}
				int chr = (int)(f.charAt(i));
				long pw = ((long)(Math.pow(chr, xor)));
				checksum %= MOD;
				checksum += (prime_hash[i%26]*pw)%MOD;
				checksum %= MOD;
				//System.out.println(checksum);
			}
			System.out.println(checksum);

		} catch(Exception e){
		}
		

	}
}
