package com.alia.jmeterplugin;

import com.alia.jmeterplugin.entity.ServiceInfoEntity;
import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;


/**
 * Created by zhongyingying on 2017/3/27.
 * Dubbo 泛化引用
 */
public class AliaDoReference {

    private ServiceInfoEntity serviceInfoEntity;
//    private ApplicationConfig applicationConfig = new ApplicationConfig();
    private RegistryConfig registryConfig = new RegistryConfig();
    private volatile ReferenceConfig<GenericService> reference = new ReferenceConfig();


    public AliaDoReference(ServiceInfoEntity serviceInfoEntity) {

        /*
        //新建Dubbo配置信息，未配置时默认加载工程目录下的dubbo.properties文件
        // 加载Dubbo引用配置信息
        applicationConfig.setName("Jmeter-plugin-dubbo");
        reference.setApplication(applicationConfig);
        */

        //serviceInfoEntity:请求dubbo Service信息
        this.serviceInfoEntity = serviceInfoEntity;

    }

    public Object reference(String s, String[] strs, Object[] objects) {

        if (StringUtils.isEmpty(serviceInfoEntity.getInterfaceName()) || serviceInfoEntity.getInterfaceName().length() <= 0) {
            throw new NullPointerException("The 'interfaceName' should not be " + serviceInfoEntity.getInterfaceName() +
                    ", please make sure you have the correct 'interfaceName' passed in");
        }

        reference.setInterface(serviceInfoEntity.getInterfaceName());
        reference.setVersion(serviceInfoEntity.getVersion());
        reference.setGeneric(true);// 声明为泛化接口

        GenericService genericService = reference.get();

        Object result = genericService.$invoke(s, strs, objects);

        System.out.println(result);

        return result;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public ReferenceConfig<GenericService> getReference() {
        return reference;
    }

    public void setReference(ReferenceConfig<GenericService> reference) {
        this.reference = reference;
    }

}
