package com.ict.token.spring.boot.autoconfigure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public final class TokenUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);
    /**
     * token有效时间，单位：毫秒
     */
    private static final long EXPIRE_IN = 7200000;
    /**
     * token每日生成次数限制
     */
    private static final int LIMITED_TIMES = 100;

    private static final Map<String, Map<String, AtomicInteger>> cachedTokens = new ConcurrentHashMap<>();

    /**
     * 生成token
     * @param apiKey
     * @return token
     */
    public static String generateToken(String apiKey) {
        String message = ApiKeyGenerator.generateOrResolve(apiKey, ApiKeyGenerator.Mode.RESOLVE, ApiKeyGenerator.Type.API_KEY);
        if (message == null)
            return JSON.toJSONString(new TokenResponse(ResponseCode.ILLEGAL_ARGS.getName(), "", "", ResponseCode.ILLEGAL_ARGS.getValue()));
        if (addToken(apiKey)) {
            TokenBody tokenBody = new TokenBody();
            tokenBody.setInfo(message);
            tokenBody.setTimestamp(System.currentTimeMillis());
            String token = ApiKeyGenerator.generateOrResolve(JSON.toJSONString(tokenBody), ApiKeyGenerator.Mode.GENERATE, ApiKeyGenerator.Type.TOKEN);
            return JSON.toJSONString(new TokenResponse(ResponseCode.SUCCESS.getName(), token, String.valueOf(EXPIRE_IN), ResponseCode.SUCCESS.getValue()));
        } else
            return JSON.toJSONString(new TokenResponse(ResponseCode.CALL_LIMITED.getName(), "", "", ResponseCode.CALL_LIMITED.getValue()));
    }

    private static boolean addToken(String apiKey) {
        String yesterday = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now().minusDays(1));
        String today = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        Map<String, AtomicInteger> map = cachedTokens.get(apiKey);
        if (map == null) {
            Map<String, AtomicInteger> map1 = new ConcurrentHashMap<>(1);
            map1.put(today, new AtomicInteger(1));
            cachedTokens.put(apiKey, map1);
        } else {
            AtomicInteger yesterdayCount = map.get(yesterday);
            if (yesterdayCount != null)
                map.remove(yesterday);
            AtomicInteger todayCount = map.get(today);
            if (todayCount == null)
                map.put(today, new AtomicInteger(1));
            else if (todayCount.get() >= LIMITED_TIMES)
                return false;
        }
        return true;

    }

    /**
     * 校验token的正确性和是否过期
     * @param token
     */
    public static String checkToken(String token) {
        try {
            String resolvedToken = ApiKeyGenerator.generateOrResolve(token, ApiKeyGenerator.Mode.RESOLVE, ApiKeyGenerator.Type.TOKEN);
            if (resolvedToken == null)
                return JSON.toJSONString(new TokenResponse(ResponseCode.ILLEGAL_TOKEN.getName(), token, "", ResponseCode.ILLEGAL_TOKEN.getValue()));
            TokenBody tokenBody = JSONObject.parseObject(resolvedToken, TokenBody.class);
            Long timestamp = tokenBody.getTimestamp();
            if (timestamp == null)
                return JSON.toJSONString(new TokenResponse(ResponseCode.ILLEGAL_TOKEN.getName(), token, "", ResponseCode.ILLEGAL_TOKEN.getValue()));
            if (System.currentTimeMillis() - timestamp < EXPIRE_IN)
                return JSON.toJSONString(new TokenResponse(ResponseCode.SUCCESS.getName(), token, "", ResponseCode.SUCCESS.getValue()));
            else
                return JSON.toJSONString(new TokenResponse(ResponseCode.TOKEN_EXPIRED.getName(), token, "", ResponseCode.TOKEN_EXPIRED.getValue()));
        } catch (Exception e) {
            LOGGER.error("校验token异常", e);
        }
        return JSON.toJSONString(new TokenResponse(ResponseCode.UNKNOWN.getName(), token, "", ResponseCode.UNKNOWN.getValue()));
    }

    public static void main(String[] args) {
        System.out.println(checkToken("c002f1af3ab204a7d58a4420360867638896e6f40e46c962af679df88f0772d5bb0139c0334f33bb5cc4d152b762c338"));
    }
    /**
     * token格式
     */
    public static class TokenBody {
        /**
         * token附带信息
         */
        private String info;
        /**
         * token的生成时间戳
         */
        private Long timestamp;

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Long timestamp) {
            this.timestamp = timestamp;
        }
    }

    public static class TokenResponse {
        private String code;
        private String token;
        private String expireIn;
        private String message;

        public TokenResponse() {
        }

        public TokenResponse(String code, String token, String expireIn, String message) {
            this.code = code;
            this.token = token;
            this.expireIn = expireIn;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getExpireIn() {
            return expireIn;
        }

        public void setExpireIn(String expireIn) {
            this.expireIn = expireIn;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
