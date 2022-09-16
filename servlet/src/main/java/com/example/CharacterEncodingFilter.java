package com.example;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.sound.sampled.AudioFormat.Encoding;
import org.apache.tomcat.util.buf.Utf8Encoder;

@WebFilter("/*")
public class CharacterEncodingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.getServletContext().log("doFilter() 호출");
        chain.doFilter(request, response);
        request.getServletContext().log("doFilter() 돌아옴");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }
}
