package com.caitb.seckill.mapper;

import com.caitb.seckill.entity.Seckill;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.spring.annotation.MapperScan;

import static org.junit.Assert.*;

/**
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@MapperScan(basePackages = "com.caitb.seckill.mapper")
@Slf4j
public class SeckillMapperTest {

    @Autowired
    private SeckillMapper seckillMapper;

    @Test
    public void test() {
        Seckill seckill = seckillMapper.selectByPrimaryKey(1000L);
        log.info("{}", seckill);
    }

}