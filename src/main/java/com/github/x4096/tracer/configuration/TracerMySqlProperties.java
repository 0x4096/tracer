package com.github.x4096.tracer.configuration;

/**
 * MySQL 配置
 *
 * @author 0x4096.peng@gmail.com
 * @date 2023/8/5
 */
public class TracerMySqlProperties {

    /**
     * mysql 日志输出
     */
    private boolean mySqlLogOut = true;

    /**
     * mysql 耗时输出
     */
    private boolean mySqlExecuteTimeLogOut = true;

    public boolean isMySqlLogOut() {
        return mySqlLogOut;
    }

    public void setMySqlLogOut(boolean mySqlLogOut) {
        this.mySqlLogOut = mySqlLogOut;
    }

    public boolean isMySqlExecuteTimeLogOut() {
        return mySqlExecuteTimeLogOut;
    }

    public void setMySqlExecuteTimeLogOut(boolean mySqlExecuteTimeLogOut) {
        this.mySqlExecuteTimeLogOut = mySqlExecuteTimeLogOut;
    }

    @Override
    public String toString() {
        return "TracerMySqlProperties{" +
                "mySqlLogOut=" + mySqlLogOut +
                ", mySqlExecuteTimeLogOut=" + mySqlExecuteTimeLogOut +
                '}';
    }

}
