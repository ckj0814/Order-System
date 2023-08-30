package com.ruyuan.eshop.common.web;

import com.ruyuan.eshop.common.constants.CoreConstant;
import com.ruyuan.eshop.common.utils.MdcUtil;
import com.ruyuan.eshop.common.utils.SnowFlake;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 全局的Spring mvc拦截器组件
 * </p>
 *
 * @author zhonghuashishan
 * @version 1.0
 */
@Slf4j
public class GlobalWebMvcInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String traceId = request.getHeader(CoreConstant.TRACE_ID);
        if (traceId == null || "".equals(traceId)) {
            traceId = request.getParameter(CoreConstant.TRACE_ID);
        }

        if (traceId != null && !"".equals(traceId)) {
            String _traceId = MdcUtil.getTraceId();
            if (_traceId == null || !_traceId.equals(traceId)) {
                MdcUtil.setTraceId(traceId);
            }
        } else {
            // 获取traceId
            traceId = SnowFlake.generateIdStr();
            log.info("trace_id:{}", traceId);
            MdcUtil.setUserTraceId(traceId);
        }

        // 处理其它全局参数，比如token等

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MdcUtil.clear();
    }
}
