/**
 * 
 */
package com.onehuddle.leaderboard.redis;


import java.util.Random;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author ragha
 *
 */
public class LeaderboardRedisClient {

	private static JedisPool pool = null;
	
	
	public  LeaderboardRedisClient() {
		
		if(pool == null) {
			System.out.println("pool is null");
			pool = new JedisPool(new JedisPoolConfig(), "localhost");
		}else {
			System.out.println("pool is not null");
		}
		
		
	}
	
	
	public void redisCheck() {
		/*
		Jedis jedis = pool.getResource();
		
		
		int lowerBound = 500;
		int upperBound = 5000;		
		Random random = new Random();
		Integer randomNumber = 0;
		HashMap<Double, String> scores = new HashMap<Double, String>();
		
		for(int i=0;i<1000;i++) {			
//			randomNumber = random.nextInt(upperBound - lowerBound) + lowerBound;
//			Double score = (double) randomNumber;
			//scores.put(key, ""+randomNumber);
			//System.out.println("User : " +i+"  Score : " + randomNumber);
			jedis.zadd("company1game1", score, ""+i);
			
		}
		
		jedis.del("company1game1company1game1");
		pool.returnResource(jedis);
		
		*/
		int lowerBound = 500;
		int upperBound = 50000;	
		Random random = new Random();
		Integer randomNumber = 0;
		
		
		Leaderboard lb = new Leaderboard("company1_game1");
		for(int i=0;i<100000;i++) {	
			randomNumber = random.nextInt(upperBound - lowerBound) + lowerBound;
			Double score = (double) randomNumber;
			lb.rankMemberIn("company1_game1", "user_"+i, score);
			System.out.println(lb.rankForIn("company1_game1", "user_"+i, true));
		}
		
	}

			
}


