package com.example.v1.service;

import com.example.v1.dao.FollowUpDao;
import com.example.v1.dto.request.FollowUpReplyRequest;
import com.example.v1.dto.response.FollowUpResponse;
import com.example.v1.model.FollowUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Follow-up questions the grader asks when an explanation is vague.
 * Re-grading the explanation with the reply is not implemented yet.
 */
@Service
@RequiredArgsConstructor
public class FollowUpService {

	private final FollowUpDao followUpDao;

	public List<FollowUpResponse> findByStudentAnswer(Long studentAnswerId) {
		return followUpDao.findByStudentAnswerId(studentAnswerId).stream().map(FollowUpResponse::from).toList();
	}

	public FollowUpResponse reply(Long id, FollowUpReplyRequest request) {
		FollowUp followUp = followUpDao.findById(id).orElseThrow(() -> new NotFoundException("Follow-up", id));
		if (followUp.getStudentReply() != null) {
			throw new BadRequestException("Follow-up " + id + " was already answered");
		}
		followUp.setStudentReply(request.reply());
		followUp.setRepliedAt(Instant.now());
		return FollowUpResponse.from(followUpDao.save(followUp));
	}
}
