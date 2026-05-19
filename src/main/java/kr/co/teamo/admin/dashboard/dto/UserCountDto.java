package kr.co.teamo.admin.dashboard.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCountDto {
    private int totalCount;
    private int activeCount;
    private int deactivatedCount;
}
