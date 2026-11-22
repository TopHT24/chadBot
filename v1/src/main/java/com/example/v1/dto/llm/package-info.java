/**
 * DTOs for the LLM boundary. They mirror the JSON schemas the LLM must return
 * (structured output / tool use), so its responses parse straight into them.
 * The service validates them and maps them to entities: the LLM decides
 * *whether* a criterion is met, and our code computes the points.
 */
package com.example.v1.dto.llm;
