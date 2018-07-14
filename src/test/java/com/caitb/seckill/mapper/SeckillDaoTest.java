package com.caitb.seckill.mapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Date;

/**
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
@MapperScan(basePackages = "com.caitb.seckill.mapper")
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SeckillDaoTest {

    @Autowired
    private SeckillDao seckillDao;

    @Test
    public void reduceNumber() {
        int updateCount = seckillDao.reduceNumber(1000L, new Date());
        log.info("updateCount: {}", updateCount);
    }

    @Test
    public void queryById() {
    }

    @Test
    public void queryAll() {
    }
}