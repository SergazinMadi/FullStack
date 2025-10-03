package org.example.fullstack.service.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ReceiveToken {

    private final JwtService jwtService;

    /**
     * Получение данных из токена текущего запроса
     *
     * @return данные из токена
     */
    public Map<String, String> tokenData() {
        HttpServletRequest request = getCurrentHttpRequest();
        String token = jwtService.extractJwtToken(request);
        return jwtService.extractTokenData(token);
    }

    /**
     * Получение текущего HTTP запроса
     *
     * @return HTTP запрос
     */
    private HttpServletRequest getCurrentHttpRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }
}