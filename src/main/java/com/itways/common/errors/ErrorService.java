package com.itways.common.errors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itways.common.props.RestProperties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ErrorService {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private RestProperties restProperties;

	@Autowired
	private RestTemplate restTemplate;

	@Cacheable(value = "errorCodes", key = "#root.targetClass.simpleName")
	public Map<String, ErrorMsgLocal> load(boolean withReload) {

		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

			HttpEntity<String> entity = new HttpEntity<>("", headers);

			String cmsURL = restProperties.getEndpoint("cms") + "/api/v1/error-codes/list";
			log.info("Calling Error codes from CMS:{}", cmsURL);

			cmsURL = withReload ? cmsURL + "/reload" : cmsURL;
			cmsURL = cmsURL + "?application=" + applicationContext.getApplicationName().replace("/", "");

			ResponseEntity<String> response = restTemplate.exchange(cmsURL, HttpMethod.GET, entity, String.class);

			ObjectMapper mapper = new ObjectMapper();
			Map<String, ErrorMsgLocal> map = mapper.readValue(response.getBody(),
					new TypeReference<Map<String, ErrorMsgLocal>>() {
					});

			log.info("HTTP Status: " + response.getStatusCode());
			log.info("Error Loaded Successfully: {}", map.size());

			return map;
		} catch (Exception ex) {
			log.error("Error Loading Error codes :{}" + ex.getMessage());
			return new HashMap<>();
		}
	}

	@CacheEvict(value = "errorCodes", allEntries = true)
	public Map<String, ErrorMsgLocal> reload() {
		return this.load(true);
	}
}
