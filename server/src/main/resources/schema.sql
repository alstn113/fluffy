CREATE TABLE member
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    email      VARCHAR(255),
    provider   VARCHAR(20)  NOT NULL,
    social_id  BIGINT       NOT NULL,
    name       VARCHAR(255) NOT NULL,
    avatarUrl  VARCHAR(255) NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_member PRIMARY KEY (id)
);

CREATE TABLE exam
(
    id          VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    member_id   BIGINT       NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_exam PRIMARY KEY (id),
    CONSTRAINT fk_exam_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE question
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    exam_id        VARCHAR(255) NOT NULL,
    text           VARCHAR(255) NOT NULL,
    type           VARCHAR(50)  NOT NULL,
    sequence       INT          NOT NULL,
    correct_answer VARCHAR(255),

    CONSTRAINT pk_question PRIMARY KEY (id),
    CONSTRAINT fk_question_exam FOREIGN KEY (exam_id) REFERENCES exam (id)
);

CREATE TABLE question_option
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    question_id BIGINT       NOT NULL,
    sequence    INT          NOT NULL,
    text        VARCHAR(255) NOT NULL,
    is_correct  BOOLEAN      NOT NULL,

    CONSTRAINT pk_question_option PRIMARY KEY (id),
    CONSTRAINT fk_question_options_question FOREIGN KEY (question_id) REFERENCES question (id)
);

CREATE TABLE submission
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    exam_id    VARCHAR(255) NOT NULL,
    member_id  BIGINT       NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_submission PRIMARY KEY (id),
    CONSTRAINT fk_submission_exam FOREIGN KEY (exam_id) REFERENCES exam (id),
    CONSTRAINT fk_submission_member FOREIGN KEY (member_id) REFERENCES member (id)
);

CREATE TABLE answer
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    submission_id BIGINT NOT NULL,
    question_id   BIGINT NOT NULL,
    text          VARCHAR(255),

    CONSTRAINT pk_answer PRIMARY KEY (id),
    CONSTRAINT fk_answer_submission FOREIGN KEY (submission_id) REFERENCES submission (id),
    CONSTRAINT fk_answer_question FOREIGN KEY (question_id) REFERENCES question (id)
);

CREATE TABLE answer_choice
(
    answer_id          BIGINT NOT NULL,
    question_option_id BIGINT NOT NULL,

    CONSTRAINT pk_answer_choice PRIMARY KEY (answer_id, question_option_id),
    CONSTRAINT fk_answer_choice_answer FOREIGN KEY (answer_id) REFERENCES answer (id)
);