package kr.co.teamo.admin.user.service;

import kr.co.teamo.admin.user.dto.AdminUserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AdminUserServiceTest {
    @Test
    @DisplayName("provider가 null 인 회원은 EMAIL로 바뀐다.")
    void provider_null_to_email(){
        // given
        AdminUserDto dto = new AdminUserDto();
        dto.setProvider(null);
        List<AdminUserDto> users = List.of(dto);

        // when
        users.forEach(u->{
            if(u.getProvider()==null)
                u.setProvider("EMAIL");
        });

        // then
        assert(users.get(0).getProvider().equals("EMAIL"));
    }

    @Test
    @DisplayName("provider가 kakao인 회원은 바뀌지 않는다.")
    void provider_kakao_to_email(){
        AdminUserDto dto = new AdminUserDto();
        dto.setProvider("KAKAO");
        List<AdminUserDto> users = List.of(dto);

        //when
        users.forEach(u->{
            if(u.getProvider()==null){
                u.setProvider("EMAIL");
            }
        });

        // then
        assert(users.get(0).getProvider().equals("KAKAO"));
    }

    @Test
    @DisplayName("여러 회원 중 null인 회원만 email로 바뀐다")
    void provider_mixed_list(){
        AdminUserDto dto1 = new AdminUserDto();
        AdminUserDto dto2 = new AdminUserDto();
        AdminUserDto dto3 = new AdminUserDto();

        dto1.setProvider(null);
        dto2.setProvider("KAKAO");
        dto3.setProvider("GITHUB");

        // when
        List<AdminUserDto> users = List.of(dto1,dto2,dto3);
        users.forEach(u->{
            if(u.getProvider()==null){
                u.setProvider("EMAIL");
            }
        });

        // then
        assert(users.get(0).getProvider().equals("EMAIL"));
        assert(users.get(1).getProvider().equals("KAKAO"));
        assert(users.get(2).getProvider().equals("GITHUB"));
    }
}
