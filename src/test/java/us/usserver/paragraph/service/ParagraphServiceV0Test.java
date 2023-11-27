package us.usserver.paragraph.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ParagraphServiceV0Test {
    @Autowired
    ParagraphServiceV0 paragraphServiceV0;

    @Test
    void getParagraphs() {
        paragraphServiceV0.getParagraphs()

    }

    @Test
    void getInVotingParagraphs() {
    }

    @Test
    void postParagraph() {
    }

    @Test
    void selectParagraph() {
    }
}