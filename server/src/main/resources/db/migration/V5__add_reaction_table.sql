CREATE TABLE reaction
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY,
    targetType VARCHAR(20)  NOT NULL,
    targetId   BIGINT       NOT NULL,
    memberId   BIGINT       NOT NULL,
    type       VARCHAR(20)  NOT NULL,
    status     VARCHAR(20)  NOT NULL,
    created_at TIMESTAMP(6) NOT NULL,
    updated_at TIMESTAMP(6) NOT NULL,
    PRIMARY KEY (id)
);

ALTER TABLE reaction
    ADD CONSTRAINT fk_member FOREIGN KEY (memberId) REFERENCES member (id);

ALTER TABLE reaction
    ADD CONSTRAINT unique_reaction UNIQUE (targetType, targetId, memberId, type);
