package com.caitb.seckill.exception;

/**
 * 秒杀关闭异常
 *
 * @作者 cai_tb
 * @创建时间 2018/7/13
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }

}
