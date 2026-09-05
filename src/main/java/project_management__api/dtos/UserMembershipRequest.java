package project_management__api.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record UserMembershipRequest(
        Long userId,
        List<Long> taskIds
) {
}
