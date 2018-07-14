package com.caitb.seckill.exception;

/**
 * 秒杀异常
 *
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }

}
