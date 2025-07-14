package com.playvoice.postservice.domain;

import com.playvoice.postservice.answer.domain.AnswerPost;
import org.junit.jupiter.api.Test;

class AnswerPostTest {

    @Test
    public void test() {
        AnswerPost answerPost = AnswerPost.createDefaultAnswerPost(1L, "asdf", "sadf", 2L);
        System.out.println(answerPost.getContent());
    }
}