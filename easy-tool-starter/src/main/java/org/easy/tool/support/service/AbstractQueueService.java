package org.easy.tool.support.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.*;

/**
 * QueueService abstract class
 *
 * @param <T>
 * @author Easily
 */

@Slf4j
public abstract class AbstractQueueService<T> implements InitializingBean {

    private BlockingQueue<T> queue = new LinkedBlockingQueue<T>();

    private ExecutorService executor;

    public int getQueueSize() {
        return queue.size();
    }

    /**
     * 返回命名空间
     *
     * @return
     */
    public abstract String getNamespace();

    /**
     * 返回高级别线程数
     *
     * @return
     */
    protected abstract int getMaxPriorityThreads();

    /**
     * 返回线程叫大小
     *
     * @return
     */
    protected abstract int getPoolSize();

    /**
     * 出队
     *
     * @param t
     * @throws Exception
     */
    protected abstract void dequeue(T t) throws Exception;

    @Override
    public void afterPropertiesSet() throws Exception {

        int pz=getPoolSize();
        if (pz<1){
            pz=Runtime.getRuntime().availableProcessors();
        }
        int mp=getMaxPriorityThreads();

        final int pzF=pz;
        final int mpF=mp;

        this.executor = Executors.newFixedThreadPool(pz, new ThreadFactory() {

            @Override
            public Thread newThread(Runnable r) {
                int pz=pzF;
                int mp=mpF;
                Thread t = new Thread(r, getNamespace() + --pz);
                if (mp > 0) {
                    t.setPriority(Thread.MAX_PRIORITY);
                    mp--;
                }
                t.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable ex) {
                        log.error("error "+" "+ex.getMessage(),ex);
                        executor.execute(new DequeueTask());
                    }
                });
                log.debug("Initializing Thread:"+t.getName()+",Priority:"+t.getPriority());
                return t;
            }
        });
        log.debug("Initializing ExecutorService:"+getNamespace());


        while (pz > 0) {
            //启动线程
            executor.execute(new DequeueTask());
            pz--;
        }
    }



    //入队方法
    public boolean enqueue(T task) {
        return this.queue.offer(task);
    }

    //出队任务
    private class DequeueTask implements Runnable {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {

                    dequeue(queue.take());

                } catch (Exception e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }
}





