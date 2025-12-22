package com.example.Baram.domain.record.dto;

import com.example.Baram.domain.record.SubmissionMethod;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
public class RecordSubmitRequest
{
    private Long userId;            // 누가 제출했는지
    private Long sentenceId;        // 어떤 문장을 썼는지
    private Long modelVersionId;    // 어떤 모델로 채점할지
    private SubmissionMethod submissionMethod; // 카메라/갤러리/캔버스
}