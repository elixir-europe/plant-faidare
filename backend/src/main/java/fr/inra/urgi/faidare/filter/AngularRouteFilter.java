package fr.inra.urgi.faidare.filter;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Filter that intercepts all request to potential Angular routes
 * (ex: /studies/ID) to send back the Angular `index.html` file with a correct
 * base href set to the spring server context path.
 *
 * Potential angular routes are devised by process of elimination:
 * - They should be GET requests
 * - They should not end with common static file suffixes {@link AngularRouteFilter#STATIC_SUFFIXES}
 * - They should not start with API prefixes {@link AngularRouteFilter#API_PREFIXES}
 *
 * <p>
 * Adapted from data-discovery
 *
 * @author gcornut
 */
@Component
@WebFilter("/*")
public class AngularRouteFilter implements Filter {

    private static final String[] API_PREFIXES = {
        "/brapi/v1", "/faidare/v1", "/actuator", "/v2/api-docs", "/swagger-resources"
    };

    private static final String[] STATIC_SUFFIXES = {
        ".html", ".js", ".css", ".ico", ".png", ".jpg", ".gif", ".eot", ".svg",
        ".woff2", ".ttf", ".woff", ".md"
    };

    @Value("${server.servlet.context-path}")
    private String serverContextPath;

    private final ResourceLoader resourceLoader;

    @Autowired
    public AngularRouteFilter(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void doFilter(
        ServletRequest req,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        if (isAngularRoute(request)) {
            // Angular route
            InputStream inputStream = resourceLoader.getResource("classpath:static/index.html").getInputStream();

            ByteSource byteSource = new ByteSource() {
                @Override
                public InputStream openStream() {
                    return inputStream;
                }
            };

            String content = byteSource.asCharSource(Charsets.UTF_8).read();
            String replacedContent = content.replace(
                "<base href=\"./\">",
                "<base href=\"" + serverContextPath + "/\">"
            );
            response.getWriter().write(replacedContent);
            return;
        }

        // Otherwise nothing to do
        chain.doFilter(request, response);
    }

    private boolean isAngularRoute(HttpServletRequest request) {
        if (!request.getMethod().equals("GET")) {
            return false;
        }

        String fullUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = fullUri.substring(contextPath.length());

        return !isApiOrStaticResource(uri);
    }

    private boolean isApiOrStaticResource(String relativePath) {
        // Starts with API prefix
        return Arrays.stream(API_PREFIXES).anyMatch(relativePath::startsWith)
            // or has static file suffix
            || Arrays.stream(STATIC_SUFFIXES).anyMatch(relativePath::endsWith);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        // nothing to do
    }

    @Override
    public void destroy() {
        // nothing to do
    }
}
