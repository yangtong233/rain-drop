package org.raindrop.common.utils.timer;

import lombok.Getter;

/**
 * created by yangtong on 2024/6/12 下午12:32
 * 时间轮中的具体定时任务
 * 在每一个时间轮槽位中，Job被组织成单向链表的形式
 */
@Getter
public class Job implements Comparable<Job> {
    //代表了其需要执行的具体任务体
    final Runnable mission;
    //其期望被执行的毫秒时间戳
    long execMilli;
    //任务执行执行间隔
    final long period;
    //剩余执行次数，负数表示无限执行
    int remain;
    //在时间轮中的具体槽位，如果处于waitSet则是-1
    int pos;
    //链表中下一个节点的指针
    Job next;

    Job(Runnable runnable, long execMilli, long period, int remain) {
        this.mission = runnable;
        this.execMilli = execMilli;
        this.period = period;
        this.remain = remain;
    }

    /**
     * 该任务执行一次后，剩余次数--
     */
    public int subRemain() {
        if (remain > 0) {
            remain--;
        }
        return remain;
    }

    /**
     * 确保waitSet中的元素都是按照执行时间有序存放，在检索时，只要找到第一个不满足条件的节点，后面的节点一定是不满足条件的
     *
     * @param other the object to be compared.
     * @return 排序数字
     */
    @Override
    public int compareTo(Job other) {
        return Long.compare(execMilli, other.execMilli);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Job job && mission == job.mission;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
