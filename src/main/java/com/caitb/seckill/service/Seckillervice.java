package com.caitb.seckill.service;

import com.caitb.seckill.dto.Exposer;
import com.caitb.seckill.dto.SeckillExecution;
import com.caitb.seckill.entity.Seckill;
import com.caitb.seckill.exception.RepeatKillException;
import com.caitb.seckill.exception.SeckillCloseException;
import com.caitb.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在"使用者"的角度去设计接口，而不是实现
 * (初学者常犯的错误，总是去关注细节，关注这个接口、方法怎么去实现，这样设计的接口往往非常冗余)
 * 怎么站在"使用者"角度设计接口？
 * 可以从三个方面着手：
 * 1.方法定义的粒度：接口功能要明确，比如这个描述业务，肯定要有个接口叫"执行秒杀"，传入执行秒杀所需要的参数，而不应该去关注秒杀怎么减库存，怎么添加用户购买行为
 * 2.方法参数：参数越简练越直接传递越好
 * 3.返回类型(return 类型/异常)：返回类型要友好，而不是return一个Map，然后随便放一些key-value;或者return Entity，其实Entity的数据不一定够
 *
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
public interface Seckillervice {

    /**
     * 查询所有秒杀记录
     *
     * @return
     */
    List<Seckill> listSeckill();

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId
     * @return
     */
    Seckill getSeckill(Long seckillId);

    /**
     * 秒杀开启是输出秒杀接口地址
     * 否则输出系统时间和秒杀时间
     *
     * @param seckillId
     */
    Exposer exportSeckillUrl(Long seckillId);

    /**
     * 执行秒杀操作
     *  @param seckillId
     * @param userPhone
     * @param md5
     */
    SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException;
}
