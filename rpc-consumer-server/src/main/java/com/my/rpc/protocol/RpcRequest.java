package com.my.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description TODO
 * @Date 2019/7/3 14:22
 * @Created by rogan.luo
 */
@Data
public class RpcRequest implements Serializable {

    private String className;

    private String methodName;

    private Object[] params;

}
