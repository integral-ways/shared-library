package com.itways.cache;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.itways.common.errors.ErrorMsgLocal;
import com.itways.common.errors.ErrorService;
import com.itways.utility.SyncData;

@Component
@Endpoint(id = SyncData.ERRORS_CODE)
public class ErrorCodesRefreshEndpoint {
	@Autowired
	private CacheManager cacheManager;

	@Autowired
	private ErrorService errorService;

	@ReadOperation
	public Map<String, ErrorMsgLocal> clearMap() {

		cacheManager.getCache(SyncData.ERRORS_CODE).clear();

		return errorService.reload();
	}

}
