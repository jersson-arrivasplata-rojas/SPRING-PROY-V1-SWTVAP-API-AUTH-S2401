package com.jersson.arrivasplata.swtvap.api.auth.security;

import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtGrantedAuthoritiesConverterAdapter;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Reactive version of {@link JwtAuthenticationConverter} for converting a {@link Jwt} to
 * a {@link AbstractAuthenticationToken Mono&lt;AbstractAuthenticationToken&gt;}.
 *
 * @author Eric Deandrea
 * @author Marcus Kainth
 * @since 5.2
 */
public final class ReactiveJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverterUnAdapter = new JwtGrantedAuthoritiesConverter();

    private Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new ReactiveJwtGrantedAuthoritiesConverterAdapter(
            new JwtGrantedAuthoritiesConverter());

    private String principalClaimName = JwtClaimNames.SUB;

    @Override
    public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
        // @formatter:off
        return this.jwtGrantedAuthoritiesConverter.convert(jwt)
                .collectList()
                .map((data) -> {
                    String principalName = jwt.getClaimAsString(this.principalClaimName);

                    Collection<GrantedAuthority> authorities = Stream.concat(
                            jwtGrantedAuthoritiesConverterUnAdapter.convert(jwt).stream()
                                    .map(x -> new SimpleGrantedAuthority(x.toString().toUpperCase()))
                                    .collect(Collectors.toList()).stream(),
                            getScopes(jwt).stream()).collect(Collectors.toSet()
                    );

                    return new JwtAuthenticationToken(jwt, authorities, principalName);
                });
        // @formatter:on
    }

    /**
     * Sets the {@link Converter Converter&lt;Jwt, Flux&lt;GrantedAuthority&gt;&gt;} to
     * use. Defaults to a reactive {@link JwtGrantedAuthoritiesConverter}.
     * @param jwtGrantedAuthoritiesConverter The converter
     * @see JwtGrantedAuthoritiesConverter
     */
    public void setJwtGrantedAuthoritiesConverter(
            Converter<Jwt, Flux<GrantedAuthority>> jwtGrantedAuthoritiesConverter) {
        Assert.notNull(jwtGrantedAuthoritiesConverter, "jwtGrantedAuthoritiesConverter cannot be null");
        this.jwtGrantedAuthoritiesConverter = jwtGrantedAuthoritiesConverter;
    }

    /**
     * Sets the principal claim name. Defaults to {@link JwtClaimNames#SUB}.
     * @param principalClaimName The principal claim name
     * @since 6.1
     */
    public void setPrincipalClaimName(String principalClaimName) {
        Assert.hasText(principalClaimName, "principalClaimName cannot be empty");
        this.principalClaimName = principalClaimName;
    }



    private Collection<? extends GrantedAuthority> getScopes(Jwt jwt) {
        final String SCOPE_AUTHORITY_PREFIX = "ROLE_";
        final Collection<String> WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES = Arrays.asList("realm_access");
        Collection<GrantedAuthority> result = new ArrayList<>();
        for (String attributeName : WELL_KNOWN_SCOPE_ATTRIBUTE_NAMES) {
            LinkedTreeMap<Object,Object> realm_access = (LinkedTreeMap) jwt.getClaims().get(attributeName);

            if (Objects.isNull(realm_access)) {
                return Collections.emptyList();
            }
            ArrayList<String> roles = (ArrayList<String>) realm_access.get("roles");
            if (Objects.isNull(roles)) {
                return Collections.emptyList();
            }

            for (Object role : roles) {
                result.add(new SimpleGrantedAuthority("ROLE_" + role.toString().toUpperCase()));
            }
        }
        return result;
    }
}