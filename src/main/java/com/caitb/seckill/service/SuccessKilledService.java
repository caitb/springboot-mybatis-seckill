package com.caitb.seckill.service;


/**
 * @作者 cai_tb
 * @创建时间 2018/7/15
 */
public interface SuccessKilledService {

    /**
     * 统计秒杀数量
     * @param seckillId
     * @return
     */
    int countBySeckillId(Long seckillId);

    /**
     * 删除秒杀记录
     * @param seckillId
     * @return
     */
    int deleteBySeckillId(Long seckillId);
}
