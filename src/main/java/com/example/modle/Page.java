package com.example.modle;

import lombok.Data;

import java.util.List;

/**
 * 通用分页类，用于封装分页查询结果
 * @param <T> 分页数据类型
 */
@Data
public class Page<T> {
    private List<T> list;           // 当前页的数据记录
    private int currentPage;        // 当前页码
    private int pageSize;           // 每页显示记录数
    private int total;             // 总记录数
    private int totalPages;         // 总页数
    private boolean hasNextPage;    // 是否有下一页
    private boolean hasPreviousPage; // 是否有上一页

    /**
     * 构造方法
     * @param list 当前页的数据记录
     * @param currentPage 当前页码
     * @param pageSize 每页显示记录数
     * @param total 总记录数
     */
    public Page(List<T> list, int currentPage, int pageSize, int total) {
        this.list = list;
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.total = total;

        // 计算总页数
        this.totalPages = (total + pageSize - 1) / pageSize;

        // 计算是否有下一页和上一页
        this.hasNextPage = currentPage < totalPages;
        this.hasPreviousPage = currentPage > 1;
    }
}