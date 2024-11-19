package org.raindrop.core.auth.password;

/**
 * 加密器
 * 会收集容器中该接口的实现类，然后调用encrypt方法对密码进行加密
 */
@FunctionalInterface
public interface Encryptor {
    /**
     * 输入一段密码，返回被加密后的文本
     * @param password
     * @return 加密后的文本
     */
    String encrypt(String password);
}
