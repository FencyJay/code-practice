package com.codebear.coderpracticebackend.model.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserSubmitAnswerRequest {

    private Long levelId;

    private List<String> selectedOptions;
}