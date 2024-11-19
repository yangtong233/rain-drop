package org.raindrop.common.utils.digit;

/**
 * 十进制 -> 其他进制
 */
public class DecimalToOther {
    /**
     * 把2进制数据转成16进制
     * @param data 10进制数据(字节形式)
     * @return 16进制字符串
     */
    public String to16(byte[] data) {
        StringBuilder builder = new StringBuilder("0x");
        if (data == null || data.length == 0) {
            return "";
        }

        for (int i = 0; i < data.length; i++) {
            int v = data[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                builder.append(0);
            }
            hv = hv.toUpperCase();
            builder.append(hv);
        }
        return builder.toString();
    }
}
