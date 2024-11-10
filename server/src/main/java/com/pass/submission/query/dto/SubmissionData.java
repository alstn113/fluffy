package com.pass.submission.query.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "submission")
@RequiredArgsConstructor
@Getter
public class SubmissionData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
