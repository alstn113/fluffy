package com.pass;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.auth.domain.OAuth2Provider;
import com.pass.exam.domain.Exam;
import com.pass.exam.domain.ExamRepository;
import com.pass.exam.domain.ExamStatus;
import com.pass.exam.domain.Question;
import com.pass.exam.domain.QuestionOption;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@Profile("local")
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final MemberRepository memberRepository;
    private final ExamRepository examRepository;

    @Override
    public void run(ApplicationArguments args) {
        init();
    }

    private void init() {
        Member member1 = memberRepository.save(new Member(
                "alice@gmail.com",
                OAuth2Provider.GITHUB,
                111111L,
                "Alice",
                "https://cdn-icons-png.flaticon.com/128/4472/4472552.png"
        ));

        Member member2 = memberRepository.save(new Member(
                "bob@gmail.com",
                OAuth2Provider.GITHUB,
                222222L,
                "Bob",
                "https://cdn-icons-png.flaticon.com/128/4472/4472525.png"
        ));

        Member member3 = memberRepository.save(new Member(
                "charlie@gmail.com",
                OAuth2Provider.GITHUB,
                333333L,
                "Charlie",
                "https://cdn-icons-png.flaticon.com/128/4472/4472516.png"
        ));

        Exam exam1 = new Exam("동물 시험", "동물과 관련된 시험입니다.", ExamStatus.PUBLISHED, member1.getId(), List.of());
        exam1.addQuestions(List.of(
                Question.shortAnswer("세계에서 가장 큰 육상 동물은 무엇인가요?", "코끼리", exam1),
                Question.longAnswer("고래의 종류와 특징에 대해 설명하세요.", exam1),
                Question.singleChoice("다음 중 가장 빠른 육상 동물은?", exam1, List.of(
                        new QuestionOption("사슴", false),
                        new QuestionOption("치타", true),
                        new QuestionOption("코끼리", false),
                        new QuestionOption("호랑이", false)
                )),
                Question.multipleChoice("다음 중 해양 생물은?", exam1, List.of(
                        new QuestionOption("상어", true),
                        new QuestionOption("물고기", true),
                        new QuestionOption("코끼리", false),
                        new QuestionOption("타조", false)
                )),
                Question.trueOrFalse("모든 포유류는 젖을 먹인다.", exam1, true),
                Question.shortAnswer("가장 큰 새는 무엇인가요?", "타조", exam1),
                Question.longAnswer("사자의 사회 구조에 대해 설명하세요.", exam1),
                Question.singleChoice("다음 중 가장 큰 바다 동물은?", exam1, List.of(
                        new QuestionOption("상어", false),
                        new QuestionOption("고래", true),
                        new QuestionOption("돌고래", false),
                        new QuestionOption("물범", false)
                )),
                Question.multipleChoice("다음 중 포유류가 아닌 것은?", exam1, List.of(
                        new QuestionOption("고양이", false),
                        new QuestionOption("개", false),
                        new QuestionOption("독수리", true),
                        new QuestionOption("코알라", false)
                )),
                Question.trueOrFalse("펭귄은 날 수 있는 새이다.", exam1, false)
        ));
        examRepository.save(exam1);

        Exam exam2 = new Exam("역사 시험", "역사와 관련된 시험입니다.", ExamStatus.PUBLISHED, member2.getId(), List.of());
        exam2.addQuestions(List.of(
                Question.shortAnswer("고대 이집트의 주요 강은 무엇인가요?", "나일 강", exam2),
                Question.longAnswer("중세 유럽의 봉건 제도에 대해 설명하세요.", exam2),
                Question.singleChoice("제2차 세계대전이 시작된 연도는?", exam2, List.of(
                        new QuestionOption("1939", true),
                        new QuestionOption("1941", false),
                        new QuestionOption("1945", false),
                        new QuestionOption("1938", false)
                )),
                Question.multipleChoice("다음 중 미국 독립전쟁의 원인은?", exam2, List.of(
                        new QuestionOption("세금 부과", true),
                        new QuestionOption("식민지의 자치권 요구", true),
                        new QuestionOption("영국의 군사적 압박", true),
                        new QuestionOption("프랑스의 지원", false)
                )),
                Question.trueOrFalse("아테네는 민주주의를 처음으로 시행한 도시국가이다.", exam2, true),
                Question.shortAnswer("제1차 세계대전이 끝난 조약의 이름은 무엇인가요?", "베르사유 조약", exam2),
                Question.longAnswer("로마 제국의 팽창과 쇠퇴에 대해 설명하세요.", exam2),
                Question.singleChoice("산업혁명이 시작된 나라는?", exam2, List.of(
                        new QuestionOption("영국", true),
                        new QuestionOption("프랑스", false),
                        new QuestionOption("독일", false),
                        new QuestionOption("미국", false)
                )),
                Question.multipleChoice("냉전 시대의 주요 사건은?", exam2, List.of(
                        new QuestionOption("베를린 장벽 건설", true),
                        new QuestionOption("한국 전쟁", true),
                        new QuestionOption("베트남 전쟁", true),
                        new QuestionOption("프랑스 혁명", false)
                )),
                Question.trueOrFalse("마틴 루터 킹 주니어는 인권 운동가였다.", exam2, true),
                Question.shortAnswer("로제타 스톤의 중요성은 무엇인가요?", "고대 이집트 문자의 해독", exam2),
                Question.longAnswer("콜럼버스의 발견과 그 영향에 대해 설명하세요.", exam2)
        ));
        examRepository.save(exam2);

        Exam exam3 = new Exam("영어 시험", "영어와 관련된 시험입니다.", ExamStatus.DRAFT, member3.getId(), List.of());
        exam3.addQuestions(List.of(
                Question.shortAnswer("다음 문장에서 주어는 무엇인가요? 'The cat is sleeping.'", "The cat", exam3),
                Question.longAnswer("‘happy’와 유의어인 단어를 3개 적으세요.", exam3),
                Question.singleChoice("Which sentence is grammatically correct?", exam3, List.of(
                        new QuestionOption("She go to school every day.", false),
                        new QuestionOption("She goes to school every day.", true),
                        new QuestionOption("She gone to school every day.", false),
                        new QuestionOption("She going to school every day.", false)
                )),
                Question.multipleChoice(
                        "What is the main idea of the following sentence? 'The sun rises in the east and sets in the west.'",
                        exam3, List.of(
                                new QuestionOption("The sun is very bright.", false),
                                new QuestionOption("The sun moves across the sky.", true),
                                new QuestionOption("The sun is important for life.", false),
                                new QuestionOption("The sun is a star.", false)
                        )),
                Question.trueOrFalse("‘I am going to the store’는 현재 진행형이다.", exam3, true),
                Question.shortAnswer("‘difficult’의 반의어는 무엇인가요?", "easy", exam3),
                Question.longAnswer("‘If I had known, I would have...’ 문장에서 사용된 문법 구조를 설명하세요.", exam3),
                Question.singleChoice("Which word means 'to make something better'?", exam3, List.of(
                        new QuestionOption("improve", true),
                        new QuestionOption("destroy", false),
                        new QuestionOption("ignore", false),
                        new QuestionOption("accept", false)
                )),
                Question.multipleChoice(
                        "What can you infer from the sentence? 'She was running late, so she skipped breakfast.'",
                        exam3, List.of(
                                new QuestionOption("She is always on time.", false),
                                new QuestionOption("She is trying to lose weight.", false),
                                new QuestionOption("She didn't have time to eat.", true),
                                new QuestionOption("She loves breakfast.", false)
                        )),
                Question.trueOrFalse("‘He don’t like pizza’는 올바른 문장이다.", exam3, false),
                Question.shortAnswer("‘beneficial’의 의미는 무엇인가요?", "유익한", exam3),
                Question.longAnswer("‘I wish I had studied harder’ 문장의 의미를 설명하세요.", exam3)
        ));
        examRepository.save(exam3);

        Exam exam4 = new Exam("과학 시험", "과학과 관련된 시험입니다.", ExamStatus.DRAFT, member2.getId(), List.of());
        exam4.addQuestions(List.of(
                Question.shortAnswer("식물의 광합성 과정에서 필요한 두 가지 요소는 무엇인가요?", "빛, 이산화탄소", exam4),
                Question.longAnswer("물의 화학식과 그 의미를 설명하세요.", exam4),
                Question.singleChoice("다음 중 뉴턴의 운동 법칙은?", exam4, List.of(
                        new QuestionOption("물체는 그 질량에 비례하여 가속된다.", false),
                        new QuestionOption("물체는 그 질량에 반비례하여 가속된다.", false),
                        new QuestionOption("물체는 그 질량에 비례하여 운동량이 변한다.", false),
                        new QuestionOption("물체는 그 힘에 비례하여 가속된다.", true)
                )),
                Question.multipleChoice("다음 중 지구의 대기 구성 성분은?", exam4, List.of(
                        new QuestionOption("산소", true),
                        new QuestionOption("이산화탄소", true),
                        new QuestionOption("헬륨", false),
                        new QuestionOption("질소", true)
                )),
                Question.trueOrFalse("DNA는 단백질을 합성하는 역할을 한다.", exam4, true),
                Question.shortAnswer("주기율표에서 원소의 원자번호는 무엇을 나타내나요?", "양성자의 수", exam4),
                Question.singleChoice("다음 중 인간의 유전자 수는 대략 얼마인가요?", exam4, List.of(
                        new QuestionOption("2만", true),
                        new QuestionOption("5천", false),
                        new QuestionOption("10만", false),
                        new QuestionOption("1천", false)
                )),
                Question.multipleChoice("다음 중 산(acid)의 특성은?", exam4, List.of(
                        new QuestionOption("pH가 7보다 낮다.", true),
                        new QuestionOption("쓴 맛이 난다.", false),
                        new QuestionOption("금속과 반응한다.", true),
                        new QuestionOption("ph가 7보다 높다.", false)
                )),
                Question.trueOrFalse("빛은 물질을 통과할 수 없다.", exam4, false),
                Question.longAnswer("지구의 내부 구조에 대해 설명하세요.", exam4)
        ));
        examRepository.save(exam4);

        Exam exam5 = new Exam("예술과 문화 시험", "예술과 문화와 관련된 시험입니다.", ExamStatus.DRAFT, member3.getId(), List.of());
        exam5.addQuestions(List.of(
                Question.shortAnswer("모나리자의 작가는 누구인가요?", "레오나르도 다빈치", exam5),
                Question.longAnswer("바흐의 음악적 기여에 대해 설명하세요.", exam5),
                Question.singleChoice("‘1984’라는 소설의 저자는 누구인가요?", exam5, List.of(
                        new QuestionOption("조지 오웰", true),
                        new QuestionOption("어니스트 헤밍웨이", false),
                        new QuestionOption("마크 트웨인", false),
                        new QuestionOption("F. 스콧 피츠제럴드", false)
                )),
                Question.multipleChoice("다음 중 아카데미 시상식에서 수상한 영화는?", exam5, List.of(
                        new QuestionOption("타이타닉", true),
                        new QuestionOption("인터스텔라", false),
                        new QuestionOption("포레스트 검프", true),
                        new QuestionOption("어벤져스: 엔드게임", false)
                )),
                Question.trueOrFalse("샤넬은 프랑스의 유명한 패션 브랜드이다.", exam5, true),
                Question.shortAnswer("파르테논 신전은 어떤 고대 문명과 관련이 있나요?", "고대 그리스", exam5),
                Question.longAnswer("발레의 역사와 발전 과정에 대해 설명하세요.", exam5),
                Question.singleChoice("‘해리 포터’ 시리즈의 저자는 누구인가요?", exam5, List.of(
                        new QuestionOption("J.K. 롤링", true),
                        new QuestionOption("스티븐 킹", false),
                        new QuestionOption("조지 마틴", false),
                        new QuestionOption("톨킨", false)
                )),
                Question.multipleChoice("다음 중 인상파 화가로 알려진 사람은?", exam5, List.of(
                        new QuestionOption("클로드 모네", true),
                        new QuestionOption("빈센트 반 고흐", false),
                        new QuestionOption("파블로 피카소", false),
                        new QuestionOption("에드가 드가", true)
                )),
                Question.trueOrFalse("비틀즈는 20세기 최고의 록 밴드 중 하나로 알려져 있다.", exam5, true),
                Question.shortAnswer("‘매트릭스’ 영화의 주제는 무엇인가요?", "가상 현실과 인간의 자유 의지", exam5),
                Question.longAnswer("세계 각국의 전통 축제에 대해 설명하세요.", exam5)
        ));
        examRepository.save(exam5);

        Exam exam6 = new Exam("기술과 정보 시험", "기술과 정보와 관련된 시험입니다.", ExamStatus.PUBLISHED, member2.getId(), List.of());
        exam6.addQuestions(List.of(
                Question.shortAnswer("컴퓨터의 기본 구성 요소 3가지를 적으세요.", "CPU, RAM, 저장장치", exam6),
                Question.longAnswer("객체지향 프로그래밍의 특징에 대해 설명하세요.", exam6),
                Question.singleChoice("인터넷 프로토콜(IP)의 주요 역할은 무엇인가요?", exam6, List.of(
                        new QuestionOption("데이터를 암호화한다.", false),
                        new QuestionOption("데이터의 주소를 지정한다.", true),
                        new QuestionOption("컴퓨터를 켠다.", false),
                        new QuestionOption("웹 페이지를 표시한다.", false)
                )),
                Question.multipleChoice("다음 중 인공지능의 응용 분야는?", exam6, List.of(
                        new QuestionOption("자율주행차", true),
                        new QuestionOption("의료 진단", true),
                        new QuestionOption("소셜 미디어", false),
                        new QuestionOption("스포츠 중계", false)
                )),
                Question.trueOrFalse("안티바이러스 소프트웨어는 컴퓨터 바이러스를 방지하는 데 도움을 준다.", exam6, true),
                Question.shortAnswer("관계형 데이터베이스의 기본 구성 요소는 무엇인가요?", "테이블", exam6),
                Question.longAnswer("클라우드 컴퓨팅의 장점에 대해 설명하세요.", exam6),
                Question.singleChoice("다음 중 블록체인의 특징은?", exam6, List.of(
                        new QuestionOption("중앙 집중형", false),
                        new QuestionOption("데이터 수정 불가", true),
                        new QuestionOption("데이터 삭제 가능", false),
                        new QuestionOption("데이터 공유 불가", false)
                )),
                Question.multipleChoice("다음 중 4차 산업혁명의 핵심 기술은?", exam6, List.of(
                        new QuestionOption("인공지능", true),
                        new QuestionOption("블록체인", true),
                        new QuestionOption("증강 현실", true),
                        new QuestionOption("비디오 게임", false)
                )),
                Question.trueOrFalse("LAN(Local Area Network)은 넓은 지역을 커버하는 네트워크이다.", exam6, false),
                Question.shortAnswer("오픈 소스 소프트웨어의 장점은 무엇인가요?", "자유로운 사용, 수정 가능", exam6),
                Question.longAnswer("기술 발전이 사회에 미친 영향에 대해 설명하세요.", exam6)
        ));
        examRepository.save(exam6);

    }
}
