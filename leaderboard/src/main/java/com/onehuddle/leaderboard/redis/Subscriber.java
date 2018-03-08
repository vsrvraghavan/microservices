package com.onehuddle.leaderboard.redis;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

/**
 * Created by vijaysy on 31/03/16.
 */
public class Subscriber {
	/*
    public static void main(String[] args) {
    		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost");

        Jedis jedis = pool.getResource();
        jedis.psubscribe(new KeyExpiredListener(), "__key*__:*");

    }
    */
}
