package abc.mem;

import java.net.InetSocketAddress;
import java.util.concurrent.Future;

import net.spy.memcached.MemcachedClient;

public class MemcachedJava {
	/**
	 * 在这里碰到Connection refused: no further information 问题。
	 * 这是因为memcache server还未启动
	 * 
	 * http://www.cnblogs.com/micua/p/installation-and-configuration-under-windows-memcached-instances.html
	 * 1)windows下安装memcache server.
	 * 		a)下载binary http://www.urielkatz.com/projects/memcached-win64/memcached-win64.zip
	 * 		b)powershell
	 * 		c)解压后cd 到解压后的目录。目录下有memchached.exe和一个dll
	 * 		d)安装:	在powershell 下用：		.\memcached.exe -d install		
	 * 				//为什么要用 .\memcached.exe 呢？如果直接用 #> memcached.exe 运行是不成功的。这是因为windows默认不信任当前目录的可执行文件。
	 * 				//Suggestion [3,General]: The command memcached was not found, but does exist in the current location. Windows PowerShell
						doesn't load commands from the current location by default. If you trust this command, instead type ".\memcached". See "
						get-help about_Command_Precedence" for more details.
			e)services.msc 查看是否有一个服务叫memcached， 查看其属性为："D:\software\src\memcached\memcached.exe" -d runservice
			f)上面那个服务并没有启动。要启动它：memcached.exe -m 1024 -d start			//-d为守护进程启动，不能指定端口 默认端口11211 -m指定内存1024m
									指定端口启动：memcached.exe –p 33000 -m 512	//注意，cmd关闭则服务停止。
	 * 	
	 * 
	 * @param args
	 */
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) {
		
		MemcachedClient mcc = null;
		try {
			 // Connecting to Memcached server on localhost
			 mcc = new MemcachedClient(new
			 InetSocketAddress("127.0.0.1", 11211));//至于是那个端口，要看memcached的服务器是开在哪个端口。11211是默认端口
			 System.out.println("Connection to server sucessful.");
			
			 // now set data into memcached server
			 Future fo = mcc.set("tutorialspoint", 900, "Free Education");
			
			 // print status of set method
			 System.out.println("set status:" + fo.get());
			
			 // retrieve and check the value from cache
			 System.out.println("tutorialspoint value in cache - " +
			 mcc.get("tutorialspoint"));
			
			 // Shutdowns the memcached client


		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			System.out.println("shutting down client");
			mcc.shutdown();
		}
	}
}