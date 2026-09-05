package project_management__api.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record UserMembershipResponse(
        Long userId,
        String username,
        Role role,
        List<Long> taskIds
) {
}
