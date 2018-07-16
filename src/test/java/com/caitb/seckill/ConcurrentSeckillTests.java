package com.caitb.seckill;

import com.caitb.seckill.entity.Seckill;
import com.caitb.seckill.service.Seckillervice;
import com.caitb.seckill.service.SuccessKilledService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.concurrent.*;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @作者 cai_tb
 * @创建时间 2018/7/16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class)
@Slf4j
public class ConcurrentSeckillTests {

    // 用于混淆md5
    private final static String slat = "fjewjffjngnrelljgrlr*^*2323**&*223fjelfj00939ddl#%!felfe&*^$!fefef90002";

    private static int corePoolSize = Runtime.getRuntime().availableProcessors();
    private static int concurrentCount = 1000;
    //创建线程池  调整队列数 拒绝服务
    private static ThreadPoolExecutor executor  = new ThreadPoolExecutor(corePoolSize, corePoolSize+1, 10l, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(concurrentCount));

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Autowired
    private Seckillervice seckillervice;
    @Autowired
    private SuccessKilledService successKilledService;

    @Before
    public void setUp() throws Exception {
        //       mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
    }

    @Test
    public void test1() throws Exception {
        Long seckillId = 1000L;
        Long userPhone = 15999506099L;
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(concurrentCount);

        // 重置数据
        Seckill s = new Seckill();
        s.setNumber(100);
        s.setSeckillId(seckillId);
        seckillervice.updateBySeckillId(s);
        successKilledService.deleteBySeckillId(seckillId);

        // 并发测试
        for (int i=0; i<concurrentCount; i++) {
            final long killPhone = userPhone + i;
            final String md5 = getMD5(seckillId);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 等待主线程"执行任务命令"
                        startGate.await();
                        MvcResult result = mvc.perform(post("/seckill/"+seckillId+"/"+md5+"/execution")
                                .param("seckillId", seckillId.toString())
                                .param("userPhone", killPhone+"")
                                .param("md5", md5))
                                .andExpect(status().isOk())// 模拟向testRest发送get请求
                                .andReturn();// 返回执行请求的结果

                        endGate.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        // 所有线程准备完毕，开始并发执行任务
        startGate.countDown();
        // 等待所有线程都执行完任务，关闭线程池
        endGate.await();
        executor.shutdown();
        // 查询秒杀结果
        Seckill seckill = seckillervice.getSeckill(seckillId);
        int count = successKilledService.countBySeckillId(seckillId);
        log.info("库存剩余 - " + seckill.getNumber() + " - 件商品");
        log.info("总共卖出 - " + count + " - 件商品");
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
}
