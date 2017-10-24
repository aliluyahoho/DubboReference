package com.alia.jmeterplugin.entity;

public class ServiceInfoEntity {

    private String interfaceName;
    private String version;
    private Integer timeout;

    public ServiceInfoEntity(String interfaceName, String version, Integer timeout){
        this.interfaceName = interfaceName;
        this.version = version;
        this.timeout = timeout;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public String getVersion() {
        return version;
    }

}
