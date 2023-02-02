package com.example.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "샘플 컨트롤러", description = "샘플 컨트롤러")
public class SampleController {

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public void login(String info1, String info2) {
        log.info("여기는 작동하는게 아닙니다. 그냥 스웨거를 위한 껍데기 입니다." +
                "Security config에서 loginProcessingUrl에 /login을 설정했으므로 " +
                "UsernamePasswordAuthenticationFilter가 /login 요청시 대신 처리합니다.");
    }

    @Operation(summary = "관리자용 API")
    @GetMapping("/info/admin-only")
    public Map<String, Object> getInfoAdminOnly() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "admin-only");

        return map;
    }

    @Operation(summary = "일반용 API")
    @GetMapping("/info/everyone")
    public Map<String, Object> getInfoEveryone() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "everyone");

        return map;
    }
}
