package project_management__api.dtos;

import lombok.Builder;
import java.util.List;

@Builder
public record MembershipRequest(

        Long projectId,
        List<UserMembershipRequest> userMembershipRequestList
) {
}
