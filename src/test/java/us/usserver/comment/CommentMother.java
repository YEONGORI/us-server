package us.usserver.comment;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.text.StringRandomizer;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.comment.dto.CommentContent;
import us.usserver.novel.Novel;

import java.nio.charset.StandardCharsets;

import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class CommentMother {
    public static Comment generateComment(Author author, Novel novel, Chapter chapter) {
        EasyRandomParameters randomParameters = new EasyRandomParameters()
                .charset(StandardCharsets.UTF_8)
                .randomize(named("content").and(ofType(String.class)), new StringRandomizer(100))
                .randomize(Author.class, () -> author)
                .randomize(Novel.class, () -> novel)
                .randomize(Chapter.class, () -> chapter);

        EasyRandom easyRandom = new EasyRandom(randomParameters);
        return easyRandom.nextObject(Comment.class);
    }

    public static CommentContent generateContent() {
        EasyRandomParameters randomParameters = new EasyRandomParameters()
                .charset(StandardCharsets.UTF_8)
                .randomize(String.class, new StringRandomizer(1, 300, 0));

        EasyRandom easyRandom = new EasyRandom(randomParameters);
        return easyRandom.nextObject(CommentContent.class);
    }
}
