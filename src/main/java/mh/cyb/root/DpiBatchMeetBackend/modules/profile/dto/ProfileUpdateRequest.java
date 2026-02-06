package mh.cyb.root.DpiBatchMeetBackend.modules.profile.dto;

import lombok.Data;

@Data
public class ProfileUpdateRequest {
    private String bio;
    // private String title;
    private String phoneNumber;
    private String linkedInUrl;
    private String githubUrl;
    private String portfolioUrl;
    private String locationCity;
    private String locationCountry;
    private String employmentStatus;
}
