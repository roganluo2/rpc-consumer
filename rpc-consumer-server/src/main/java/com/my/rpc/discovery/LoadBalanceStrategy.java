package com.my.rpc.discovery;

import java.util.List;

public interface LoadBalanceStrategy {
    String selectHost(List<String> hosts);


}
