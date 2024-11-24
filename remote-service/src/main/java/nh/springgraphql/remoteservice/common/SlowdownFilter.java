package nh.springgraphql.remoteservice.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SlowdownFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger( SlowdownFilter.class );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var slowdownString = request.getParameter("slowdown");

        toLong(slowdownString).ifPresent(Utils::sleep);

        filterChain.doFilter(request, response);
    }

    Optional<Long> toLong(String s) {
        if (!StringUtils.hasText(s)) { return Optional.empty(); };

        try {
            long l = Long.parseLong(s);
            return Optional.of(l);
        } catch (Exception ex) {
            log.warn("Error: {}", ex, ex);
        }
        return Optional.empty();
    }
}
