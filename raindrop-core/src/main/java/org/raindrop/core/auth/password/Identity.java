package org.raindrop.core.auth.password;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户鉴权相关工具类
 */
@Component
public class Identity implements BeanFactoryAware {

    private List<Encryptor> encryptors;

    @Override
    public void setBeanFactory(@NonNull BeanFactory beanFactory) throws BeansException {
        DefaultListableBeanFactory beanFactory1 = (DefaultListableBeanFactory) beanFactory;
        //得到容器中所有加密器，如果没有加密器，则添加一个不加密的明文加密器
        this.encryptors = new ArrayList<>(beanFactory1.getBeansOfType(Encryptor.class).values());
        if (this.encryptors.isEmpty()) {
            this.encryptors.add(p -> p);
        }
    }

    /**
     * 密码匹配
     * @param recognizedPassword 请求传过来的明文密码
     * @param dbPassword    数据库存储的密文密码
     * @return 匹配成功与否
     */
    public Boolean matchPassword(String recognizedPassword, String dbPassword) {
        if (recognizedPassword == null || dbPassword == null) {
            return false;
        }
        recognizedPassword = encodePassword(recognizedPassword);
        return dbPassword.equals(recognizedPassword);
    }

    /**
     * 根据加密器，对密码进行加密
     * @param password 明文密码
     * @return 加密后的密码
     */
    public String encodePassword(String password) {
        for (Encryptor encryptor : encryptors) {
            password = encryptor.encrypt(password);
        }
        return password;
    }


}
