CREATE TABLE member
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_member PRIMARY KEY (id)
);

CREATE TABLE form
(
    id          VARCHAR(255) NOT NULL,
    title       VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP(6) NOT NULL,
    updated_at  TIMESTAMP(6) NOT NULL,

    CONSTRAINT pk_form PRIMARY KEY (id)
);

CREATE TABLE question
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    form_id        VARCHAR(255) NOT NULL,
    text           VARCHAR(255) NOT NULL,
    type           VARCHAR(50)  NOT NULL,
    sequence       INT          NOT NULL,
    correct_answer VARCHAR(255),

    CONSTRAINT pk_question PRIMARY KEY (id),
    CONSTRAINT fk_question_form FOREIGN KEY (form_id) REFERENCES form (id)
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