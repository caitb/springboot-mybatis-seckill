package com.caitb.seckill.service.impl;

import com.caitb.seckill.mapper.SuccessKilledDao;
import com.caitb.seckill.service.SuccessKilledService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @作者 cai_tb
 * @创建时间 2018/7/15
 */
@Service
public class SuccessKilledServiceImpl implements SuccessKilledService {

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public int countBySeckillId(Long seckillId) {
        return successKilledDao.countBySeckillId(seckillId);
    }

    @Override
    public int deleteBySeckillId(Long seckillId) {
        return successKilledDao.deleteBySeckillId(seckillId);
    }
}
