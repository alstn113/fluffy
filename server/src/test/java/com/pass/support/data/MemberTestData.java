package com.pass.support.data;

import com.pass.auth.domain.Member;

public class MemberTestData {

    public static MemberBuilder defaultMember() {
        return new MemberBuilder()
            .withId(1L)
            .withUsername("username")
            .withPassword("password");
    }

    public static class MemberBuilder {

        private Long id;
        private String username;
        private String password;

        public MemberBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder withUsername(String username) {
            this.username = username;
            return this;
        }

        public MemberBuilder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Member build() {
            return new Member(id, username, password);
        }
    }
}
