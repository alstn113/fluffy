package com.fluffy.support.data;

import com.fluffy.auth.domain.Member;
import com.fluffy.auth.domain.OAuth2Provider;

public class MemberTestData {

    public static MemberBuilder defaultMember() {
        return new MemberBuilder()
                .withId(null)
                .withEmail("example@example.com")
                .withProvider(OAuth2Provider.GITHUB)
                .withSocialId(1234567890L)
                .withName("example")
                .withAvatarUrl("https://example.com/image.jpg");
    }

    public static class MemberBuilder {

        private Long id;
        private String email;
        private OAuth2Provider provider;
        private Long socialId;
        private String name;
        private String avatarUrl;

        public MemberBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder withEmail(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder withProvider(OAuth2Provider provider) {
            this.provider = provider;
            return this;
        }

        public MemberBuilder withSocialId(Long socialId) {
            this.socialId = socialId;
            return this;
        }

        public MemberBuilder withName(String name) {
            this.name = name;
            return this;
        }

        public MemberBuilder withAvatarUrl(String avatarUrl) {
            this.avatarUrl = avatarUrl;
            return this;
        }

        public Member build() {
            return new Member(id, email, provider, socialId, name, avatarUrl);
        }
    }
}
