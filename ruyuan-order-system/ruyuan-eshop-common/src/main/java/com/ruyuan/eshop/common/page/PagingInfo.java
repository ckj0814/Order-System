package com.ruyuan.eshop.common.page;

import lombok.Data;

import java.util.List;

/**
 * <p>
 * 分页查询结果
 * </p>
 *
 * @author zhonghuashishan
 */
@Data
public class PagingInfo<T> {

    /**
     * 页码
     */
    private int pageNo = 1;
    /**
     * 每页条数
     */
    private int pageSize = 10;
    /**
     * 总条数
     */
    private Long total;

    /**
     * 起始位置
     */
    private Integer startPos = (pageNo - 1) * pageSize;

    private List<T> list;

    public PagingInfo() {

    }

    public PagingInfo(Long total, List<T> list) {
        this.total = total;
        this.list = list;
    }

    public static <T> PagingInfo toResponse(List<T> data, Long total, Integer currentPageNo, Integer currentPageSize) {
        PagingInfo<T> pagingObj = new PagingInfo<>();
        pagingObj.setTotal(total);
        pagingObj.setList(data);
        pagingObj.setPageNo(currentPageNo);
        pagingObj.setPageSize(currentPageSize);
        return pagingObj;
    }

}
