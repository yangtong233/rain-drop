package org.raindrop.common.utils.natives;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;

/**
 * created by yangtong on 2024/6/7 下午4:53
 *
 * @Description: Natives工具类测试
 */
@Slf4j
public class NativesTest {
    /**
     * 测试Natives使用堆外内存读写字符串
     */
    @Test
    public void testInt() {
        try(Arena arena = Arena.ofConfined()) {
            MemorySegment memorySegment1 = arena.allocateFrom("Hello!!!Native!!!");
            System.out.println(memorySegment1.byteSize());
            //分配一个int类型的内存段
            MemorySegment memorySegment = arena.allocate(1024);
            //在堆外内存的0位置写入字符串
            Natives.setStr(memorySegment, 0L, "Hello!!!Native!!!");
            //从堆外内存的0位置读取字符串
            System.out.println(Natives.getStr(memorySegment, 4L));
            System.out.println(Natives.getStr(memorySegment));
        }
    }
}
