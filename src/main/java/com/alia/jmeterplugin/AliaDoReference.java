package com.alia.jmeterplugin;

import com.alia.jmeterplugin.entity.ServiceInfoEntity;
import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.rpc.service.GenericService;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongyingying on 2017/3/27.
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
        applicationConfig.setName("jmeter-plugon-dubbo");
        reference.setApplication(applicationConfig);
        */

        //serviceInfoEntity:请求dubbo Service信息
        this.serviceInfoEntity = serviceInfoEntity;

    }

    public Object reference(String s, String[] strs, Object[] objects) {

        if (StringUtils.isEmpty(serviceInfoEntity.getInterfaceName()) || serviceInfoEntity.getInterfaceName().length() <= 0) {
            throw new NullArgumentException("The 'interfaceName' should not be " + serviceInfoEntity.getInterfaceName() + ", please make sure you have the correct 'interfaceName' passed in");
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

    public static void main(String[] args) {

        /*
        ServiceInfoEntity 为封装好的Service信息，支持自定义
            参数1：Service Name，例如：com.epcc.control.basedata.facade.service.ClearInfoService
            参数2：Service Version，例如：1.0.0
            参数3：Time out配置
        */
        ServiceInfoEntity serviceInfoEntity = new ServiceInfoEntity("com.epcc.control.basedata.facade.service.ClearInfoService", "1.0.0_aliaTest", 500);

        AliaDoReference s = new AliaDoReference(serviceInfoEntity);

        //1.通过请求zk，获取dubbo接口信息
        RegistryConfig regConf = s.getRegistryConfig();
        regConf.setAddress("zookeeper://192.168.105.102:2181");
        s.getReference().setRegistry(regConf);

        //2.已知dubbo接口IP地址，直接请求
        //s.getReference().setUrl("dubbo://127.0.0.1:20880");


        s.reference("queryClrBatInfoForOth", null, null);
    }
}
