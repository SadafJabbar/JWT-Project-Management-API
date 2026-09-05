package project_management__api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import project_management__api.dtos.ApiResponse;
import project_management__api.dtos.MembershipRequest;
import project_management__api.dtos.MembershipResponse;
import project_management__api.service.MembershipService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/membership")
public class MembershipController {

    private final MembershipService membershipService;
    @Autowired
    public MembershipController(MembershipService membershipService){
        this.membershipService=membershipService;
    }



    @GetMapping("/{id}")
    public ApiResponse<MembershipResponse> getMembershipDataById(@PathVariable Long id){
        return membershipService.getMembershipDataById(id);
    }


    @GetMapping
    public ApiResponse<List<MembershipResponse>> getAllMembershipData(){
        return membershipService.getAllMembershipData();
    }

    @PostMapping
    public ApiResponse<MembershipResponse> createMembership(@Valid @RequestBody MembershipRequest membershipRequest){
        return membershipService.createMembership(membershipRequest);
    }

    @PutMapping("/{id}")
    public ApiResponse<MembershipResponse> updateMembership(@PathVariable Long id,@Valid @RequestBody MembershipRequest membershipRequest){
        return membershipService.updateMembership(id,membershipRequest);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<MembershipResponse> deleteMembership(@PathVariable Long id){
        return membershipService.deleteMembership(id);
    }


}
