package com.example.v1.dao;

import com.example.v1.model.Question;

/**
 * Questions are reached through their exam (ExamQuestionDao), so only CRUD is needed.
 */
public interface QuestionDao extends CrudDao<Question, Long> {
}
