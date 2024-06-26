package com.fd.rookie.spring.boot.config.datasource;

/**
 * 数据源类型
 */
public enum DatabaseType {
    master("write"), slave("read");

    DatabaseType(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DatabaseType{" +
                "name='" + name + '\'' +
                '}';
    }
}
