package com.caitb.seckill.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @作者 cai_tb
 * @创建时间 2018/7/15
 */
@Component
@Scope
@Aspect
@Order(1)
public class LockAspect {

    /**
     * 思考：为什么不用synchronized
     * service 默认是单例的，并发下lock只有一个实例
     */
    private static Lock lock = new ReentrantLock(true);//互斥锁 参数默认false，不公平锁

    //Service层切点     用于记录错误日志
    @Pointcut("@annotation(com.caitb.seckill.aop.Servicelock)")
    public void lockAspect() {

    }

    @Around("lockAspect()")
    public  Object around(ProceedingJoinPoint joinPoint) {
        lock.lock();
        Object obj = null;
        try {
            obj = joinPoint.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        } finally{
            lock.unlock();
        }
        return obj;
    }

}
