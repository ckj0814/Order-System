package com.ruyuan.eshop.order.schedule;

import com.ruyuan.eshop.order.OrderApplication;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@SpringBootTest(classes = OrderApplication.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class AutoCancelExpiredOrderTaskTest  {

    @Autowired
    private AutoCancelExpiredOrderTask autoCancelExpiredOrderTask;

    @Test
    public void test() throws Exception {
        autoCancelExpiredOrderTask.execute();
    }

}
