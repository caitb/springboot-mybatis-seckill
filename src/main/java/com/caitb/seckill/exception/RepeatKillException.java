package com.caitb.seckill.exception;

/**
 * 重复秒杀异常
 *
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }

}
