package kr.co.teamo.admin.service;

import kr.co.teamo.admin.dto.UserCountDto;
import kr.co.teamo.admin.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;

    public UserCountDto getUserCount() {
        return adminMapper.selectUserCount();
    }
}
