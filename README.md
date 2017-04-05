## Jmeter-Plugin 之 dubbo 引用

### Author: Alia Zhong(aliluyahoho@163.com)
### Version: 1.0.0

___
##  Usage of this plugin tool

**Step 1** generate executable file 

enter maven commond as follows:

	mvn clean compile package
	mvn assembly:assembly
	
**Step 2** add jar file to Jmeter

Jmeter plugin directory：%JMETER_HOME%/lib/ext

Note:restart Jmeter after operation

**Step 3** Generate Jmeter test Plan

	1. Add Thread Group
	2. Create BeanShell Script
	3. Add Response Assertion
	4. Add View result tree 


**Step 4** Write BeanShell to reference dubbo service

***Solution 1*** Get dubbo-service address from zookeeper

	//BeanShell code as follows
	import com.alia.jmeterplugin.AliaDoReference;
	import com.alia.jmeterplugin.entity.ServiceInfoEntity;
	import com.alibaba.dubbo.config.RegistryConfig;

	ServiceInfoEntity serviceInfoEntity = new ServiceInfoEntity("com.epcc.control.basedata.facade.service.ClearInfoService", "1.0.0_aliaTest", 500);
	AliaDoReference s = new AliaDoReference(serviceInfoEntity);

	RegistryConfig regConf = s.getRegistryConfig();
	regConf.setAddress("zookeeper://10.23.2.32:2181");
	System.out.println(regConf.getAddress());
	s.getReference().setRegistry(regConf);

	s.getReference().setUrl("dubbo://10.23.0.74:20880");
	s.reference("queryClrBatInfoForOth", null, null);
	
***Solution 2*** Get dubbo-service address from Service IP
	
	//BeanShell code as follows
	import com.alia.jmeterplugin.AliaDoReference;
	import com.alia.jmeterplugin.entity.ServiceInfoEntity;
	import com.alibaba.dubbo.config.RegistryConfig;

	ServiceInfoEntity serviceInfoEntity = new ServiceInfoEntity("com.epcc.control.basedata.facade.service.ClearInfoService", "1.0.0_aliaTest", 500);
	AliaDoReference s = new AliaDoReference(serviceInfoEntity);

	s.getReference().setUrl("dubbo://10.23.0.74:20880");
	s.reference("queryClrBatInfoForOth", null, null);

**Step 5** 支持不同参数类型

基本类型以及Date,List,Map等不需要转换，直接调用

	s.reference("methodName", new String[]{"java.lang.String"}, new Object[]{"B201703250001"});
        
用Map表示POJO参数，如果返回值为POJO也将自动转成Map

    Map<String, Object> person = new HashMap<String, Object>();
    person.put("name", "xxx");
    person.put("password", "yyy");
    Object result = genericService.$invoke("findPerson", new String[]{"com.xxx.Person"}, new Object[]{person});