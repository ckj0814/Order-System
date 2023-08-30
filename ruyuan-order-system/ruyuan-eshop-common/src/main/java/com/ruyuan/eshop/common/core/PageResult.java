package com.ruyuan.eshop.common.core;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.io.Serializable;
import java.util.List;

/**
 * 统一的分页返回对象
 *
 * @author zhonghuashishan
 * @version 1.0
 */
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private long totalElements;        //总记录数
    private List<T> content;    //结果集
    private long number;    // 第几页
    private long size;    // 每页记录数
    private long totalPages;        // 总页数
    private long numberOfElements;        // 当前页的数量 <= pageSize，该属性来自ArrayList的size属性


    public PageResult() {

    }

    /**
     * 处理List对象包装成分页信息返回
     *
     * @param list 不分页时的查询结果
     */
    public PageResult(List<T> list) {
        if (list != null) {
            this.number = 1; // 当前页, 这里要注意下，jpa分页是从第0页开始的，mybatis从第1页开始，现在这个信息前端不会用到，会在查询体现
            this.size = list.size(); // 每页的数量
            this.totalElements = list.size(); // 总记录数
            this.totalPages = 1; // 总页数
            this.content = list; // 结果集
            this.numberOfElements = list.size(); // 当前页的数量
        }
    }

    /**
     * 处理Mybatis plus分页查询结果包装成分页信息返回
     *
     * @param page 分页查询结果
     */
    public PageResult(IPage<T> page) {
        if (page != null) {
            this.number = page.getCurrent();
            this.size = page.getSize();
            this.totalElements = page.getTotal();
            this.totalPages = page.getPages();
            this.content = page.getRecords();
            if (this.content != null && !this.content.isEmpty()) {
                this.numberOfElements = this.content.size();
            } else {
                this.numberOfElements = 0L;
            }
        }
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public long getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(long numberOfElements) {
        this.numberOfElements = numberOfElements;
    }
}