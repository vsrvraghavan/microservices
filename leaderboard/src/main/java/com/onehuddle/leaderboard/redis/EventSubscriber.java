/**
 * 
 */
package com.onehuddle.leaderboard.redis;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

/**
 * @author ragha
 *
 */

@Component
public class EventSubscriber implements DisposableBean, Runnable {
	private Thread thread;
	private Jedis jedis;
	
	EventSubscriber(){		
		//jedis = new Jedis("192.168.0.102",6379);//new Jedis(Leaderboard.DEFAULT_REDIS_HOST);
		System.out.println("Redis Host in : EventSubscriber " + Leaderboard.DEFAULT_REDIS_HOST + ":" + Leaderboard.DEFAULT_REDIS_PORT);
        this.thread = new Thread(this);
    }
	//private static final Logger LOGGER = LoggerFactory.getLogger(EventSubscriber.class);
	@Override
    public  void run() {
        /*  JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");
            Jedis jedis1 = pool.getResource();
            jedis1.psubscribe(new NotificationListener(), "__key*__:RT*");  */
    	
    		//LOGGER.info("Called from thread");
		System.out.println("Called from thread");
		System.out.println("Redis Host in : EventSubscriber run " + Leaderboard.DEFAULT_REDIS_HOST + ":" + Leaderboard.DEFAULT_REDIS_PORT);
		jedis = new Jedis(Leaderboard.DEFAULT_REDIS_HOST,Leaderboard.DEFAULT_REDIS_PORT);
        jedis.psubscribe(new JedisPubSub() {
            @Override
            public void onPSubscribe(String pattern, int subscribedChannels) {
                System.out.println("onPSubscribe " + pattern + " " + subscribedChannels);
            }

            @Override
            public void onPMessage(String pattern, String channel, String message) {
                System.out.print("[Pattern:" + pattern + "]");
                System.out.print("[Channel: " + channel + "]");
                System.out.println("[Message: " + message + "]");
            }
        }, "__key*__:*");

    }
	
	@Override
    public void destroy(){
        jedis.disconnect();
        jedis = null; 
    }
	
	
}



