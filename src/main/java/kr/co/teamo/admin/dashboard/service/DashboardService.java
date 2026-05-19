package kr.co.teamo.admin.dashboard.service;

import kr.co.teamo.admin.dashboard.dto.NewUserCountDto;
import kr.co.teamo.admin.dashboard.dto.PostCountDto;
import kr.co.teamo.admin.dashboard.dto.UserCountDto;
import kr.co.teamo.admin.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class DashboardService {

    private final AdminMapper adminMapper;

    public UserCountDto getUserCount() {
        return adminMapper.selectUserCount();
    }

    public NewUserCountDto getNewUserCount() {
        return adminMapper.selectNewUserCount();
    }

    public PostCountDto getPostCount() {
        return adminMapper.selectPostCount();
    }
}
