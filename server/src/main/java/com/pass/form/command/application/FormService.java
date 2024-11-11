package com.pass.form.command.application;

import com.pass.auth.domain.Member;
import com.pass.auth.domain.MemberRepository;
import com.pass.form.command.application.dto.CreateFormAppRequest;
import com.pass.form.command.application.dto.CreateFormResponse;
import com.pass.form.command.application.dto.PublishFormAppRequest;
import com.pass.form.command.application.exception.FormNotWrittenByMemberException;
import com.pass.form.command.domain.Form;
import com.pass.form.command.domain.FormRepository;
import com.pass.form.command.domain.QuestionGroup;
import com.pass.global.web.Accessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;
    private final QuestionMapper questionMapper;
    private final MemberRepository memberRepository;

    @Transactional
    public CreateFormResponse create(CreateFormAppRequest request) {
        Accessor accessor = request.accessor();
        Member member = memberRepository.getById(accessor.id());

        Form form = Form.initial(request.title(), member.getId());
        Form savedForm = formRepository.save(form);

        return formMapper.toResponse(savedForm);
    }

    @Transactional
    public void publish(PublishFormAppRequest request) {
        Form form = formRepository.getById(request.formId());
        Accessor accessor = request.accessor();
        Member member = memberRepository.getById(accessor.id());

        if (form.isNotWrittenBy(member.getId())) {
            throw new FormNotWrittenByMemberException(member.getId(), form.getId());
        }

        QuestionGroup questionGroup = questionMapper.toQuestionGroup(request, form);
        form.addQuestions(questionGroup, form);
    }
}
