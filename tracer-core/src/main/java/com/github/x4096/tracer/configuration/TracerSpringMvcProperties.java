package com.github.x4096.tracer.configuration;

/**
 * SpringMVC 追踪配置
 *
 * @author 0x4096.peng@gmail.com
 * @date 2020/7/24
 */
public class TracerSpringMvcProperties {

    /**
     * 包含 URL 默认 /** 拦截所有 多个使用 , 分割
     */
    private String includeUrls;

    /**
     * 排除 URL 多个使用 , 分割
     */
    private String excludeUrls;

    public String getIncludeUrls() {
        return includeUrls;
    }

    public void setIncludeUrls(String includeUrls) {
        this.includeUrls = includeUrls;
    }

    public String getExcludeUrls() {
        return excludeUrls;
    }

    public void setExcludeUrls(String excludeUrls) {
        this.excludeUrls = excludeUrls;
    }

    @Override
    public String toString() {
        return "TracerSpringMvcProperties{" +
                "includeUrls='" + includeUrls + '\'' +
                ", excludeUrls='" + excludeUrls + '\'' +
                '}';
    }

}
