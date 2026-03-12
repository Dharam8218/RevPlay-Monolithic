package com.revature.RevPlay.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PlaylistPrivacyUpdateRequest {

    @JsonProperty("isPublic")   // allow "isPublic"
    @JsonAlias("public")        // also allow "public"
    private boolean isPublic;
}
