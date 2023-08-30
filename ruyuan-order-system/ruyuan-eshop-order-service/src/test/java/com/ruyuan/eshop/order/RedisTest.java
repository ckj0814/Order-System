package com.ruyuan.eshop.order;

import com.ruyuan.eshop.common.constants.RedisLockKeyConstants;
import com.ruyuan.eshop.common.redis.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class RedisTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisLock redisLock;

    @Test
    public void test() {
        RLock lock = redissonClient.getLock("lock");
        lock.lock();
        System.out.println("加锁成功！");
        lock.unlock();
        System.out.println("解锁成功！");
    }

    @Test
    public void testMultiLock() {
        String orderId = "123";
        String orderPayKey = RedisLockKeyConstants.ORDER_PAY_KEY + orderId;
        String cancelKey = RedisLockKeyConstants.CANCEL_KEY + orderId;
        List<String> list = new ArrayList<>();
        list.add(orderPayKey);
        list.add(cancelKey);
        boolean result = redisLock.multiLock(list);
        if (result) {
            System.out.println("加锁成功！");
            redisLock.unMultiLock(list);
            System.out.println("解锁成功！");
        }
    }
}
