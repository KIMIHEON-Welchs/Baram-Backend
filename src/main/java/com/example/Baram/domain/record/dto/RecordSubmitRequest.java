package com.example.Baram.domain.record.dto;

import com.example.Baram.domain.record.SubmissionMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecordSubmitRequest
{
    private String font;
    private String sentence;
}