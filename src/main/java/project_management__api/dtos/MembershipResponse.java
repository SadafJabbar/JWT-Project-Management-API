package project_management__api.dtos;

import lombok.Builder;
import java.util.List;

@Builder
public record MembershipResponse(
        Long membershipId,
    ProjectResponse projectResponse,
    List<UserMembershipResponse> userMembershipResponseList,
    int totalMembers,
    int totalTasks
){
}
