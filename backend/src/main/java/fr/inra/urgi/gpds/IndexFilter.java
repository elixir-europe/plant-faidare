package fr.inra.urgi.gpds;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Filter that forwards all GET requests to non-static and non-api resources to index.html. This filter is necessary
 * to support deep-linking for URLs generated by the Angular router.
 *
 * @author JB Nizet
 */
@Component
@WebFilter("/*")
public class IndexFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        if (mustForward(request)) {
            request.getRequestDispatcher("/index.html").forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean mustForward(HttpServletRequest request) {
        if (!request.getMethod().equals("GET")) {
            return false;
        }

        String fullUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String uri = fullUri.substring(contextPath.length());

        return !(isApi(uri) || isStaticResource(uri));
    }

    private boolean isApi(String uri) {
        return uri.startsWith("/brapi") || uri.startsWith("/actuator") || uri.startsWith("/gnpis/v1");
    }

    private boolean isStaticResource(String uri) {
        if (uri.startsWith("/index.html")) {
            return true;
        }

        List<String> suffixes = Arrays.asList(
            ".js", ".css", ".ico", ".png", ".jpg", ".gif", ".eot", ".svg", ".woff2", ".ttf", ".woff"
        );
        for (String suffix : suffixes) {
            if (uri.endsWith(suffix)) return true;
        }
        return false;
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
