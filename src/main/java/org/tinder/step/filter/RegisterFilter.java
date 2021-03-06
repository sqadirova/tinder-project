package org.tinder.step.filter;


import lombok.extern.log4j.Log4j2;
import org.eclipse.jetty.http.HttpMethod;
import org.tinder.step.entity.User;
import org.tinder.step.service.UserService;
import org.tinder.step.util.TemplateEngine;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

@Log4j2
public class RegisterFilter implements HttpFilter {
    private TemplateEngine engine;
    private UserService usersService = new UserService();

    public RegisterFilter(TemplateEngine engine) {
        this.engine = engine;
    }

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doHttpFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HashMap<String, Object> data = new HashMap<>();

        if (HttpMethod.POST.name().equalsIgnoreCase(request.getMethod())) {
            try {
                String login = request.getParameter("Email");
                String password = request.getParameter("Password");
                User user = new User(login, password);

                if (usersService.checkUserByLogin(user)) {
                    log.error(new Exception("Such user already exist! Please check your info."));
                    response.sendRedirect("/signup");
                }

                chain.doFilter(request, response);

            } catch (Exception e) {
                log.error(e.getMessage());
                response.sendRedirect("/signup");
//                data.put("message", e.getMessage());
//                engine.render("error_message.ftl", data, (HttpServletResponse) servletResponse);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
