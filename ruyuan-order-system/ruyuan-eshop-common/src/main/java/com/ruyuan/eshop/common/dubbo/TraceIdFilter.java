package com.ruyuan.eshop.common.dubbo;

import com.ruyuan.eshop.common.constants.CoreConstant;
import com.ruyuan.eshop.common.utils.MdcUtil;
import com.ruyuan.eshop.common.utils.SnowFlake;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * 自定义dubbo filter
 */
@Activate(group = {"provider", "consumer"})
public class TraceIdFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        String traceId;
        if (rpcContext.isConsumerSide()) {
            traceId = MdcUtil.getTraceId();
            if (traceId == null) {
                traceId = SnowFlake.generateIdStr();
            }
            rpcContext.setAttachment(CoreConstant.TRACE_ID, traceId);
        }

        if (rpcContext.isProviderSide()) {
            traceId = rpcContext.getAttachment(CoreConstant.TRACE_ID);
            MdcUtil.setTraceId(traceId);
        }

        return invoker.invoke(invocation);
    }
}