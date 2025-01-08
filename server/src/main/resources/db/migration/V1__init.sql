CREATE TABLE IF NOT EXISTS member
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    name        VARCHAR(255) NOT NULL,
    email       VARCHAR(255),
    avatar_url  VARCHAR(255) NOT NULL,
    social_id   VARCHAR(255) NOT NULL,
    provider    VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS exam
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    member_id   BIGINT       NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    status      VARCHAR(20)  NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    start_at    TIMESTAMP(6),
    end_at      TIMESTAMP(6),
    updated_at  TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS answer
(
    id            BIGINT GENERATED BY DEFAULT AS IDENTITY,
    question_id   BIGINT NOT NULL,
    submission_id BIGINT,
    text          VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS answer_choice
(
    answer_id          BIGINT NOT NULL,
    question_option_id BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS question
(
    id             BIGINT GENERATED BY DEFAULT AS IDENTITY,
    exam_id        BIGINT,
    text           VARCHAR(255) NOT NULL,
    correct_answer  VARCHAR(255),
    type           VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS question_option
(
    id          BIGINT GENERATED BY DEFAULT AS IDENTITY,
    question_id BIGINT,
    text        VARCHAR(255) NOT NULL,
    is_correct  BOOLEAN      NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS submission
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
    exam_id    BIGINT       NOT NULL,
    member_id  BIGINT       NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS answer
    ADD CONSTRAINT fk_submission
        FOREIGN KEY (submission_id)
            REFERENCES submission (id);

ALTER TABLE IF EXISTS answer_choice
    ADD CONSTRAINT fk_answer
        FOREIGN KEY (answer_id)
            REFERENCES answer (id);

ALTER TABLE IF EXISTS question
    ADD CONSTRAINT fk_exam
        FOREIGN KEY (exam_id)
            REFERENCES exam (id);

ALTER TABLE IF EXISTS question_option
    ADD CONSTRAINT fk_question
        FOREIGN KEY (question_id)
            REFERENCES question (id);