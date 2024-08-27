package com.sparta.schedulemanagement.common.filter;

import com.sparta.schedulemanagement.common.util.JwtUtils;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;




    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(JwtUtils.AUTHORIZATION_HEADER);

        if(authHeader != null && authHeader.startsWith(JwtUtils.BEARER_PREFIX)) {
            String token = authHeader
                    .substring(JwtUtils.BEARER_PREFIX.length());
            try {
                // 토큰 유효성 검사
                if(jwtUtils.validateToken(token)) {
                    // 토큰에서 사용자 정보 추출
                    String email = jwtUtils.getUserEmailFromToken(token);
                    request.setAttribute("AuthenticatedUser", email);
                }
                else {
                    // 요청 속성에 사용자 정보 추가 (필요시 SecurityContextHolder에 Authentication 객체를 저장할 수도 있음)
                    throw new JwtException("유효하지 않거나 이미 만료된 토큰입니다.");
                }
            }
            catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter()
                        .write("JWT 인증 실패: " + e.getMessage());
                return;

            }
        }
        else if(!isExcludedPath(request.getRequestURI())){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter()
                    .write("토큰이 누락 되었습니다.");
            return;

        }
        //필터 체인에 다음 필터를 호출 합니다.
        filterChain.doFilter(request, response);
    }

    private boolean isExcludedPath(String path) {
        return "/api/v1/users/login".equalsIgnoreCase(path) || "/api/v1/users".equalsIgnoreCase(path);
    }
}
