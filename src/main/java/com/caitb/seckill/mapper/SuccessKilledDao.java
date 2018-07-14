package com.caitb.seckill.mapper;

import com.caitb.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细,可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId")Long seckillId, @Param("userPhone")Long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId")Long seckillId, @Param("userPhone")Long userPhone);

}
