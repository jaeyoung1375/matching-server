package kr.co.teamo.admin.user.service;

import kr.co.teamo.admin.mapper.AdminMapper;
import kr.co.teamo.admin.user.dto.AdminUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {
    private final AdminMapper adminMapper;

    public List<AdminUserDto> searchUsers(String name, String email){
        List<AdminUserDto> users = adminMapper.searchUsers(name, email);

        users.forEach(user->{
            if(user.getProvider() == null){
                user.setProvider("EMAIL");
            }
        });
        return users;
    }
}
