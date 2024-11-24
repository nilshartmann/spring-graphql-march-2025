package nh.springgraphql.remoteservice.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Configuration
    static class WebConfig implements WebMvcConfigurer {
        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new LoggingInterceptor());
        }
    }

    private static final Logger log = LoggerFactory.getLogger(LoggingInterceptor.class);

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("startTime", System.currentTimeMillis());
//        log.info("<STARTING> {} {}\n",
//            response.getStatus(),
//            request.getRequestURL()
//        );

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        var timeTaken = (System.currentTimeMillis() - (Long) request.getAttribute("startTime"));
        var it = request.getHeaderNames().asIterator();

        StringBuilder headerBuilder = new StringBuilder();

        while (it.hasNext()) {
            var name = it.next();
            if (name.toLowerCase().startsWith("hx-")) {
                headerBuilder.append(
                    String.format("  %s: %s%n", name, request.getHeader(name))
                );
            }
        }

        log.info("<FINISHED> {} {} - {}ms\n{}",
            response.getStatus(),
            request.getRequestURL(),
            timeTaken,
            headerBuilder
        );
    }

    private static String now() {
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(formatter);
        return formattedTime;
    }
}