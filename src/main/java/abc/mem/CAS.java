package abc.mem;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

import net.spy.memcached.CASValue;
import net.spy.memcached.CASResponse;
import net.spy.memcached.MemcachedClient;

public class CAS {

	/**
	 * ???
	 * Memcached CAS command 'checks' and 'set' data item if and only if, no other client process has updated it since last read by this client.
	 * http://www.tutorialspoint.com/memcached/memcached_cas.htm
	 * 
	 * 解释CAS
	 * lock/mutex-free algorithms
	 * http://stackoverflow.com/a/22603392
	 * https://en.wikipedia.org/wiki/Compare-and-swap
	 * http://stackoverflow.com/a/120976
	 * http://stackoverflow.com/a/120999
	 * 
	 * CAS actually stands for check-and-set, and is a method of optimistic locking. 
		he CAS value or ID is associated with each document which is updated whenever the 
		document changes - a bit like a revision ID. 
		<p>
		The intent is that instead of 
		pessimistically locking a document (and the associated lock overhead) you 
		just read it's CAS value, and then only perform the write if the CAS matches.
		</p>
		<pre>
		The general use-case is:
		
		Read an existing document, and obtain it's current CAS (get_with_cas)
		Prepare a new value for that document, assuming no-one else has modified the document (and hence caused the CAS to change).
		Write the document using the check_and_set operation, providing the CAS value from (1).
		Step 3 will only succeed (perform the write) if the document is unchanged between (1) and (3) - i.e. no other user has modified it in the meantime. Typically if (3) does fail you would retry the whole sequence (get_with_cas, modify, check_and_set).
		
		There's a much more detailed description of check-and-set in the Couchbase Developer Guide under Check and Set (CAS).
		</pre>
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
   
      try{
   
         // Connecting to Memcached server on localhost
         MemcachedClient mcc = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
         System.out.println("Connection to server sucessful.");

         // add data to memcached server
         Future fo = mcc.set("key", 900, "value");

         // print status of set method
         		System.out.println("set status:" + fo.get());
            
         // retrieve value stored for tutorialspoint from cache
         		System.out.println("key's value in cache - " + mcc.get("key"));

         // obtain CAS token value using gets method
         CASValue casValue = mcc.gets("key");

         // display CAS token value
         		System.out.println("CAS token - " + casValue);//注意这个cas token {CasValue 61/new value} 每set一次,61这个值就+1,类似于svn revision num

         // try to update data using memcached cas method。
         //这里说try，是用cas id来try。如果这个key-value还没被我之前的其他人发动过，则set，否则失败。
         //就像svn的commit一样。 (??不确定)
         CASResponse casresp = mcc.cas("key", casValue.getCas(), 900, "new value");
         
         // display CAS Response
         		System.out.println("CAS Response - " + casresp);

         // retrieve and check the value from cache
         		System.out.println("key value in cache - " + mcc.get("key"));

         // Shutdowns the memcached client
         mcc.shutdown();
         
      }catch(Exception ex){
    	  System.out.println(ex.getMessage());
      }
   }
}
