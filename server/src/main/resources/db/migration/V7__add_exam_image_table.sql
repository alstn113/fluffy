CREATE TABLE exam_image
(
    id         UUID DEFAULT gen_random_uuid(),
    member_id  BIGINT       NOT NULL,
    exam_id    BIGINT       NOT NULL,
    path       VARCHAR(255) NOT NULL,
    file_size  BIGINT       NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE exam_image
    ADD CONSTRAINT fk_member FOREIGN KEY (member_id) REFERENCES member (id);

ALTER TABLE exam_image
    ADD CONSTRAINT fk_exam FOREIGN KEY (exam_id) REFERENCES exam (id);

