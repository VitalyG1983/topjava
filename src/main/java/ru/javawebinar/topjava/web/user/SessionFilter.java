package ru.javawebinar.topjava.web.user;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter
public class SessionFilter implements Filter {

    public SessionFilter() {
    }

    private String contextPath;

    private String language;

    @Override
    public void init(FilterConfig config) {
        contextPath = config.getInitParameter("context.path");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String path = request.getRequestURI();

        if (path.equals(contextPath)) { //the index page
            request.getSession(true);
            chain.doFilter(request, response);
        } else {
            HttpSession session = request.getSession(false);

            if (session != null) {
                language = (String) session.getAttribute("lang");
                chain.doFilter(request, response);
            } else {
                response.sendRedirect(contextPath + "?timeout=true&lang=" + language);
            }
        }
    }

    public void destroy(){}
}
