//package org.raindrop.common.utils.natives;
//
//import java.lang.foreign.Arena;
//import java.lang.foreign.MemorySegment;
//import java.lang.foreign.ValueLayout;
//import java.nio.charset.StandardCharsets;
//
///**
// * created by yangtong on 2024/6/4 下午12:34
// *
// * @Description: 堆外内存读写工具类，从堆外内存读取数据成Java中的byte、short、int、long、String等数据
// * MemorySegment表示堆外的内存段
// */
//public class Natives {
//
//    private Natives() {
//        throw new UnsupportedOperationException();
//    }
//
//    /**
//     * JAVA语言对于C语言中指针类型的抽象
//     *
//     * @param memorySegment 指针对象，指针地址为0说明是一个无效地址，也就是空指针
//     * @return 是否空指针
//     */
//    public static boolean checkNullPointer(MemorySegment memorySegment) {
//        return memorySegment == null || memorySegment.address() == 0L;
//    }
//
//    /**
//     * 以下代码都是按照指定的内存布局操作MemorySegment的数据
//     */
//    public static byte getByte(MemorySegment memorySegment, long index) {
//        return memorySegment.getAtIndex(ValueLayout.JAVA_BYTE, index);
//    }
//
//    public static void setByte(MemorySegment memorySegment, long index, byte value) {
//        memorySegment.setAtIndex(ValueLayout.JAVA_BYTE, index, value);
//    }
//
//    public static char getChar(MemorySegment memorySegment, long index) {
//        return memorySegment.getAtIndex(ValueLayout.JAVA_CHAR, index);
//    }
//
//    public static void setChar(MemorySegment memorySegment, long index, char value) {
//        memorySegment.setAtIndex(ValueLayout.JAVA_CHAR, index, value);
//    }
//
//    public static short getShort(MemorySegment memorySegment, long index) {
//        return memorySegment.getAtIndex(ValueLayout.JAVA_SHORT, index);
//    }
//
//    public static void setShort(MemorySegment memorySegment, long index, short value) {
//        memorySegment.setAtIndex(ValueLayout.JAVA_SHORT, index, value);
//    }
//
//    public static int getInt(MemorySegment memorySegment, long index) {
//        return memorySegment.getAtIndex(ValueLayout.JAVA_INT, index);
//    }
//
//    public static void setInt(MemorySegment memorySegment, long index, int value) {
//        memorySegment.setAtIndex(ValueLayout.JAVA_INT, index, value);
//    }
//
//    public static long getLong(MemorySegment memorySegment, long index) {
//        return memorySegment.getAtIndex(ValueLayout.JAVA_LONG, index);
//    }
//
//    public static void setLong(MemorySegment memorySegment, long index, long value) {
//        memorySegment.setAtIndex(ValueLayout.JAVA_LONG, index, value);
//    }
//
//    public static String getStr(MemorySegment memorySegment) {
//        return getStr(memorySegment, 0);
//    }
//
//    public static String getStr(MemorySegment memorySegment, long index) {
//        int i = 0;
//        StringBuilder sb = new StringBuilder();
//        char currentChar;
//        while (true) {
//            currentChar = memorySegment.getAtIndex(ValueLayout.JAVA_CHAR, index + i);
//            if (currentChar == Character.MIN_VALUE) {
//                break;
//            }
//            sb.append(currentChar);
//            i++;
//        }
//        return sb.toString();
//    }
//
//    public static void setStr(MemorySegment memorySegment, long index, String str) {
//        for (int i = 0; i < str.length(); i++) {
//            //将str字符串11个字符写入到之前分配的内存段中
//            memorySegment.setAtIndex(ValueLayout.JAVA_CHAR, i + index, str.charAt(i));
//        }
//        memorySegment.setAtIndex(ValueLayout.JAVA_CHAR, str.length(), Character.MIN_VALUE);
//    }
//
//    public static MemorySegment allocateStr(Arena arena, String str) {
//        return arena.allocateFrom(str);
//    }
//
//    /**
//     * 从指针读取字符串
//     * 对于已知内存长度的字符串，可以提前分配好byte数组，逐个进行拷贝
//     * 对于未知内存长度的字符串，一直读取直到\0为止，然后调用MemorySegment.copy()的方法进行数组拷贝
//     *
//     * @param ptr       指针
//     * @param maxLength 字符串的字节长度
//     * @return
//     */
//    public static String getStr(MemorySegment ptr, int maxLength) {
//        if (maxLength > 0) {
//            byte[] bytes = new byte[maxLength];
//            for (int i = 0; i < maxLength; i++) {
//                byte b = getByte(ptr, i);
//                //C语言中，字符串可以认为是一个以\0结尾的Java中的byte数组，所以遇到\0就返回字符串
//                if (b == Constants.NUT) {
//                    return new String(bytes, 0, i, StandardCharsets.UTF_8);
//                } else {
//                    bytes[i] = b;
//                }
//            }
//        } else {
//            for (int i = 0; i < Integer.MAX_VALUE; i++) {
//                char b = getChar(ptr, i);
//                if (b == Character.MIN_VALUE) {
//                    char[] bytes = new char[i];
//                    MemorySegment.copy(ptr, ValueLayout.JAVA_CHAR, 0, bytes, 0, i);
//                    return new String(bytes);
//                }
//            }
//        }
//        throw new FrameworkException(ExceptionType.NATIVE, Constants.UNREACHED);
//    }
//
//    /**
//     * 向堆外内存写入字符串
//     *
//     * @return
//     */
//    public static MemorySegment allocateStr(Arena arena, String str, int len) {
//        MemorySegment strSegment = MemorySegment.ofArray(str.getBytes(StandardCharsets.UTF_8));
//        long size = strSegment.byteSize();
//        if (len < size + 1) {
//            throw new RuntimeException("String out of range");
//        }
//        MemorySegment memorySegment = arena.allocate(ValueLayout.JAVA_BYTE, len);
//        MemorySegment.copy(strSegment, 0, memorySegment, 0, size);
//        setByte(memorySegment, size, Constants.NUT);
//        return memorySegment;
//    }
//
//    /**
//     * 对字节数组进行匹配，从offset索引位置开始匹配MemorySegment中的内容是否与bytes数组中指定的分隔符相一致
//     * 此类字符串匹配时，可以使用BM或KMP等特定算法来进行性能优化，但这里的bytes不会很长，所以直接暴力匹配
//     */
//    public static boolean matches(MemorySegment m, long offset, byte[] bytes) {
//        for (int index = 0; index < bytes.length; index++) {
//            if (getByte(m, offset + index) != bytes[index]) {
//                return false;
//            }
//        }
//        return true;
//    }
//}
