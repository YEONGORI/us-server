package us.usserver.paragraph;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import us.usserver.author.Author;
import us.usserver.chapter.Chapter;
import us.usserver.paragraph.paragraphEnum.ParagraphStatus;

import java.nio.charset.StandardCharsets;

import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class ParagraphMother {
    public static Paragraph generateParagraph(Author author, Chapter chapter) {
        EasyRandomParameters randomParameters = new EasyRandomParameters()
                .charset(StandardCharsets.UTF_8)
                .randomize(named("content").and(ofType(String.class)), new StringRandomizer(300))
                .randomize(named("sequence").and(ofType(int.class)), new IntegerRangeRandomizer(0, 15))
                .randomize(ParagraphStatus.class, () -> ParagraphStatus.IN_VOTING)
                .randomize(Author.class, () -> author)
                .randomize(Chapter.class, () -> chapter);

        EasyRandom easyRandom = new EasyRandom(randomParameters);
        return easyRandom.nextObject(Paragraph.class);
    }
}
