package com.example.v1.dao.inmemory;

import com.example.v1.dao.QuestionDao;
import com.example.v1.model.Question;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryQuestionDao extends InMemoryCrudDao<Question> implements QuestionDao {
}
