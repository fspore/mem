package abc.mem;

import java.net.InetSocketAddress;

import net.spy.memcached.MemcachedClient;

/**
 * Hello world!
 *
 */
public class App {
	
	public static void main(String[] args) {
	      try{
	         // Connecting to Memcached server on localhost
	         MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
	         System.out.println("Connection to server sucessful.");
	         
	         // Shutdowns the memcached client
	         mcc.shutdown();
	         
	      }catch(Exception ex){
	         System.out.println( ex.getMessage() );
	      }
	   }
}
