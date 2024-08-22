package org.example.catch_line.filter;

import jakarta.servlet.*;

import java.io.IOException;

public interface Filter extends jakarta.servlet.Filter {

    public default void init(FilterConfig config) throws ServletException {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException;


    public default void destroy() {
    }
}
