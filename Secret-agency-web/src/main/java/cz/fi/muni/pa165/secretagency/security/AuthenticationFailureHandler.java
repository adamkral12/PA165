package cz.fi.muni.pa165.secretagency.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                   AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().print(exception.getMessage());
    }
}
