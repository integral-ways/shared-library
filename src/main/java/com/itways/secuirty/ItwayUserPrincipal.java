package com.itways.secuirty;

import java.util.Map;

public class ItwayUserPrincipal {

	private final long id;
	private final Map<String, Object> claims;

	public ItwayUserPrincipal(Long id, Map<String, Object> claims) {
		this.id = id;
		this.claims = claims;
	}

	public Long getId() {
		return id;
	}

	public Map<String, Object> getClaims() {
		return claims;
	}

}
