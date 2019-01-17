package fr.inra.urgi.gpds.filter;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


/**
 * Intercept HTTP request to store the HTTP Basic Authorization for later re-use
 *
 * @author gcornut
 *
 *
 */
@Component
@WebFilter("/*")
public class AuthenticationFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

	/**
     * Logs the initialization process of the filter.
     */
	@Override
    public void init(FilterConfig filterConfig) {
		if (logger.isInfoEnabled()) {
            logger.info("Initializing filter '" + filterConfig.getFilterName() + "'");
        }
	}

	/**
     * Intercept HTTP Basic Authorization
     */
	@Override
    public void doFilter(
        ServletRequest req, ServletResponse resp, FilterChain chain
    ) throws IOException, ServletException {
		// get web login
		String webUserLogin = ((HttpServletRequest) req).getRemoteUser();
		if (logger.isDebugEnabled()){
            logger.debug(
                "\n*********************************************\n" +
                " Applying user credentials for " + webUserLogin + "\n" +
                "*********************************************"
            );
        }

		final String authorization = ((HttpServletRequest) req).getHeader("Authorization");

		if (authorization != null && authorization.startsWith("Basic")) {
		    // Parse to extract the user name
			String base64Credentials = authorization.substring("Basic".length()).trim();
			String authCode = new String(BaseEncoding.base64().decode(base64Credentials), Charsets.UTF_8);
			final String userName = authCode.split(":", 2)[0];

			AuthenticationStore.setUser(userName, authCode);
		}

		chain.doFilter(req, resp);
	}

	@Override
    public void destroy() {}

}
