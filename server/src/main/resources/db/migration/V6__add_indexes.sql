CREATE INDEX idx_exam_status_updated_at
    ON exam (status, updated_at DESC, id);

CREATE INDEX idx_exam_member_status
    ON exam (member_id, status, updated_at DESC, id);

CREATE INDEX idx_submission_member_exam
    ON submission (member_id, exam_id, id);