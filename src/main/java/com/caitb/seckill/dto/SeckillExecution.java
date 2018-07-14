package com.caitb.seckill.dto;

import com.caitb.seckill.entity.SuccessKilled;
import com.caitb.seckill.enums.SeckillStatEnum;

/**
 * 封装秒杀执行结果
 *
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
public class SeckillExecution {

    private Long seckillId;

    // 秒杀执行结果状态
    private int state;

    // 状态表示
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(Long seckillId, SeckillStatEnum SeckillStatEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = SeckillStatEnum.getState();
        this.stateInfo = SeckillStatEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(Long seckillId, SeckillStatEnum SeckillStatEnum) {
        this.seckillId = seckillId;
        this.state = SeckillStatEnum.getState();
        this.stateInfo = SeckillStatEnum.getStateInfo();
    }

    public Long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(Long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}
