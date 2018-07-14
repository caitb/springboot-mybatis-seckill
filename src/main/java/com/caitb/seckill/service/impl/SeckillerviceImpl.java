package com.caitb.seckill.service.impl;

import com.caitb.seckill.dto.Exposer;
import com.caitb.seckill.dto.SeckillExecution;
import com.caitb.seckill.entity.Seckill;
import com.caitb.seckill.entity.SuccessKilled;
import com.caitb.seckill.enums.SeckillStatEnum;
import com.caitb.seckill.exception.RepeatKillException;
import com.caitb.seckill.exception.SeckillCloseException;
import com.caitb.seckill.exception.SeckillException;
import com.caitb.seckill.mapper.SeckillDao;
import com.caitb.seckill.mapper.SeckillMapper;
import com.caitb.seckill.mapper.SuccessKilledDao;
import com.caitb.seckill.mapper.SuccessKilledMapper;
import com.caitb.seckill.service.Seckillervice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
@Service
@Slf4j
public class SeckillerviceImpl implements Seckillervice {

    // 用于混淆md5
    private final static String slat = "fjewjffjngnrelljgrlr*^*2323**&*223fjelfj00939ddl#%!felfe&*^$!fefef90002";

    @Autowired
    private SeckillMapper seckillMapper;
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledMapper successKilledMapper;
    @Autowired
    private SuccessKilledDao successKilledDao;

    /**
     * 查询所有秒杀记录
     *
     * @return 数据库中所有的秒杀记录
     */
    @Override
    public List<Seckill> listSeckill() {
        return seckillMapper.selectAll();
    }

    /**
     * 查询单个秒杀记录
     *
     * @param seckillId 秒杀记录的ID
     * @return 根据ID查询出来的记录信息
     */
    @Override
    public Seckill getSeckill(Long seckillId) {
        return seckillMapper.selectByPrimaryKey(seckillId);
    }

    /**
     * 在秒杀开启时输出秒杀接口的地址，否则输出系统时间跟秒杀地址
     *
     * @param seckillId 秒杀商品Id
     * @return 根据对应的状态返回对应的状态实体
     */
    @Override
    public Exposer exportSeckillUrl(Long seckillId) {
        Seckill seckill = seckillMapper.selectByPrimaryKey(seckillId);
        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统当前时间
        Date nowTime = new Date();
        if (nowTime .getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(Long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 执行秒杀操作，失败的，失败我们就抛出异常
     *
     * @param seckillId 秒杀的商品ID
     * @param userPhone 手机号码
     * @param md5       md5加密值
     * @return 根据不同的结果返回不同的实体信息
     */
    @Transactional
    @Override
    public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑：减库存 + 秒杀记录
        Date nowTime = new Date();

        try {
            // 记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                // 唯一：seckillId, userPhone
                if (updateCount <= 0) {
                    // 没有更新到记录，秒杀结束
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.stateOf(1), successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (Exception e) {
            throw new SeckillException("seckill inner error: " + e.getMessage());
        }

    }
}
