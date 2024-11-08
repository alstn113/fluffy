package com.pass.form.command.application;

import com.pass.form.command.application.dto.CreateFormAppRequest;
import com.pass.form.command.application.dto.CreateFormResponse;
import com.pass.form.command.application.dto.PublishFormAppRequest;
import com.pass.form.command.domain.Form;
import com.pass.form.command.domain.FormRepository;
import com.pass.form.command.domain.QuestionGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;
    private final FormMapper formMapper;
    private final QuestionMapper questionMapper;

    @Transactional
    public CreateFormResponse create(CreateFormAppRequest request) {
        Form form = Form.initial(request.title());
        Form savedForm = formRepository.save(form);

        return formMapper.toResponse(savedForm);
    }

    @Transactional
    public void publish(PublishFormAppRequest request) {
        Form form = formRepository.getById(request.formId());
        QuestionGroup questionGroup = questionMapper.toQuestionGroup(request, form);
        form.addQuestions(questionGroup, form);
    }
}
