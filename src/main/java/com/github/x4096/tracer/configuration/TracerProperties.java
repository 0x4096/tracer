package com.github.x4096.tracer.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

/**
 * @author 0x4096.peng@gmail.com
 * @project tracer
 * @datetime 2020/3/3 00:28
 * @description tracer 配置信息
 * @readme
 */
@Configuration
@ConfigurationProperties(prefix = "com.github.x4096.tracer")
public class TracerProperties {

    /**
     * 全局日志输出, 启用当前配置下方配置才有效
     */
    private boolean globalLogOut = true;

    /**
     * MVC 日志输出
     */
    private boolean mvcLogOut = true;

    /**
     * RPC 日志输出
     */
    private boolean rpcLogOut = true;

    /**
     * MVC 请求日志输出
     */
    private boolean mvcRequestLogOut = true;

    /**
     * RPC 请求日志输出
     */
    private boolean rpcRequestLogOut = true;

    /**
     * MVC 响应日志输出
     */
    private boolean mvcResponseLogOut = true;

    /**
     * RPC 响应日志输出
     */
    private boolean rpcResponseLogOut = true;

    /**
     * MVC 响应时间日志输出
     */
    private boolean mvcResponseTimeLogOut = true;

    /**
     * RPC 响应时间日志输出
     */
    private boolean rpcResponseTimeLogOut = true;

    /**
     * SpringMVC 配置
     */
    @NestedConfigurationProperty
    private TracerSpringMvcProperties springMvcProperties;

    public boolean isGlobalLogOut() {
        return globalLogOut;
    }

    public void setGlobalLogOut(boolean globalLogOut) {
        this.globalLogOut = globalLogOut;
    }

    public boolean isMvcLogOut() {
        return mvcLogOut;
    }

    public void setMvcLogOut(boolean mvcLogOut) {
        this.mvcLogOut = mvcLogOut;
    }

    public boolean isRpcLogOut() {
        return rpcLogOut;
    }

    public void setRpcLogOut(boolean rpcLogOut) {
        this.rpcLogOut = rpcLogOut;
    }

    public boolean isMvcRequestLogOut() {
        return mvcRequestLogOut;
    }

    public void setMvcRequestLogOut(boolean mvcRequestLogOut) {
        this.mvcRequestLogOut = mvcRequestLogOut;
    }

    public boolean isRpcRequestLogOut() {
        return rpcRequestLogOut;
    }

    public void setRpcRequestLogOut(boolean rpcRequestLogOut) {
        this.rpcRequestLogOut = rpcRequestLogOut;
    }

    public boolean isMvcResponseLogOut() {
        return mvcResponseLogOut;
    }

    public void setMvcResponseLogOut(boolean mvcResponseLogOut) {
        this.mvcResponseLogOut = mvcResponseLogOut;
    }

    public boolean isRpcResponseLogOut() {
        return rpcResponseLogOut;
    }

    public void setRpcResponseLogOut(boolean rpcResponseLogOut) {
        this.rpcResponseLogOut = rpcResponseLogOut;
    }

    public boolean isMvcResponseTimeLogOut() {
        return mvcResponseTimeLogOut;
    }

    public void setMvcResponseTimeLogOut(boolean mvcResponseTimeLogOut) {
        this.mvcResponseTimeLogOut = mvcResponseTimeLogOut;
    }

    public boolean isRpcResponseTimeLogOut() {
        return rpcResponseTimeLogOut;
    }

    public void setRpcResponseTimeLogOut(boolean rpcResponseTimeLogOut) {
        this.rpcResponseTimeLogOut = rpcResponseTimeLogOut;
    }

    public TracerSpringMvcProperties getSpringMvcProperties() {
        return springMvcProperties;
    }

    public void setSpringMvcProperties(TracerSpringMvcProperties springMvcProperties) {
        this.springMvcProperties = springMvcProperties;
    }

    @Override
    public String toString() {
        return "TracerProperties{" +
                "globalLogOut=" + globalLogOut +
                ", mvcLogOut=" + mvcLogOut +
                ", rpcLogOut=" + rpcLogOut +
                ", mvcRequestLogOut=" + mvcRequestLogOut +
                ", rpcRequestLogOut=" + rpcRequestLogOut +
                ", mvcResponseLogOut=" + mvcResponseLogOut +
                ", rpcResponseLogOut=" + rpcResponseLogOut +
                ", mvcResponseTimeLogOut=" + mvcResponseTimeLogOut +
                ", rpcResponseTimeLogOut=" + rpcResponseTimeLogOut +
                ", springMvcProperties=" + springMvcProperties +
                '}';
    }

}
