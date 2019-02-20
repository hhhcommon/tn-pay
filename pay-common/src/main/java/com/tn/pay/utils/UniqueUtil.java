package com.tn.pay.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 全局唯一控制器
 */
public class UniqueUtil {

    private static final Set<String> keys = new HashSet<>();
    private static final Lock lock = new ReentrantLock();

    private UniqueUtil() {
    }//一个虚拟机保证唯一

    public static boolean exist(String key) throws Exception {
        boolean exist;
        try {
            lock.lock();
            if (!(exist = keys.contains(key))) {
                keys.add(key);
            } else {
                throw new Exception("订单:" + key + "正在处理！");
            }
        } finally {
            lock.unlock();
        }
        return exist;
    }

    public static void clean(String key) {
        try {
            lock.lock();
            if (keys.contains(key)) {
                keys.remove(key);
            }
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                if (exist("123")) {
                    return;
                }
                Thread.sleep(1000);
                System.out.println("ABC");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                clean("123");
            }
        }).start();
        new Thread(() -> {
            try {
                if (exist("123")) {
                    return;
                }
                Thread.sleep(2000);
                System.out.println("ABC");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                clean("123");
            }
        }).start();
        new Thread(() -> {
            try {
                if (exist("1235")) {
                    return;
                }
                Thread.sleep(500);
                System.out.println("ABCD");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                clean("1235");
            }
        }).start();
    }
}
