package com.fintech.insurance.gateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.FixedAuthoritiesExtractor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.BaseOAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.util.*;

public class GatewayUserInfoTokenService implements ResourceServerTokenServices {

    private static final Logger logger = LoggerFactory.getLogger(GatewayUserInfoTokenService.class);

    private final String userInfoEndpointUrl;

    private final String clientId;

    private OAuth2RestOperations restTemplate;

    private String tokenType = DefaultOAuth2AccessToken.BEARER_TYPE;

    private AuthoritiesExtractor authoritiesExtractor = new FixedAuthoritiesExtractor();

    public GatewayUserInfoTokenService(String userInfoEndpointUrl, String clientId) {
        this.userInfoEndpointUrl = userInfoEndpointUrl;
        this.clientId = clientId;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public void setRestTemplate(OAuth2RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setAuthoritiesExtractor(AuthoritiesExtractor authoritiesExtractor) {
        this.authoritiesExtractor = authoritiesExtractor;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        Map<String, Object> userInfoMap = this.getOauthUserInfoAsMap(this.userInfoEndpointUrl, accessToken);
        Object principal = userInfoMap.get("principal");
        OAuth2Request request = getRequest(userInfoMap);
        List<GrantedAuthority> authorities = this.authoritiesExtractor.extractAuthorities(userInfoMap);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                principal, "N/A", authorities);
        return new OAuth2Authentication(request, token);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        throw new UnsupportedOperationException("Not supported: read access token");
    }

    private OAuth2Request getRequest(Map<String, Object> map) {
        Map<String, Object> request = (Map<String, Object>) map.get("oauth2Request");

        String clientId = (String) request.get("clientId");
        Set<String> scope = new LinkedHashSet<>(request.containsKey("scope") ?
                (Collection<String>) request.get("scope") : Collections.<String>emptySet());

        return new OAuth2Request(null, clientId, null, true, new HashSet<>(scope),
                null, null, null, null);
    }

    private Map<String, Object> getOauthUserInfoAsMap(String path, String accessToken) throws InvalidTokenException {
        logger.debug("Getting user info from: " + path);

        try {
            if (this.restTemplate == null) {
                BaseOAuth2ProtectedResourceDetails resource = new BaseOAuth2ProtectedResourceDetails();
                resource.setClientId(this.clientId);
                this.restTemplate = new OAuth2RestTemplate(resource);
            }
            OAuth2AccessToken existingToken = this.restTemplate.getOAuth2ClientContext().getAccessToken();
            if (existingToken == null || !accessToken.equals(existingToken.getValue())) {
                DefaultOAuth2AccessToken token = new DefaultOAuth2AccessToken(accessToken);
                token.setTokenType(this.tokenType);
                this.restTemplate.getOAuth2ClientContext().setAccessToken(token);
            }
            return restTemplate.getForEntity(path, Map.class).getBody();
        } catch (Exception e) {
            logger.error("Could not fetch user details", e);
            throw new InvalidTokenException(e.getMessage(), e);
        }
    }
}
