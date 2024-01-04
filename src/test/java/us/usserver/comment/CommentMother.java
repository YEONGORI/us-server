package us.usserver.comment;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.novel.Novel;

import java.nio.charset.StandardCharsets;

import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class CommentMother {
    public static Chapter generateChapter(Novel novel) {
        EasyRandomParameters randomParameters = new EasyRandomParameters()
                .charset(StandardCharsets.UTF_8)
                .randomize(named("title").and(ofType(String.class)), new StringRandomizer(100))
                .randomize(named("part").and(ofType(Integer.class)), new IntegerRangeRandomizer(0, 100))
                .randomize(ChapterStatus.class, () -> ChapterStatus.IN_PROGRESS)
                .randomize(Novel.class, () -> novel);


        EasyRandom easyRandom = new EasyRandom(randomParameters);
        return easyRandom.nextObject(Chapter.class);
    }
}
