package us.usserver.novel;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.misc.EnumRandomizer;
import org.jeasy.random.randomizers.range.IntegerRangeRandomizer;
import org.jeasy.random.randomizers.text.StringRandomizer;
import us.usserver.author.Author;
import us.usserver.chapter.chapterEnum.ChapterStatus;
import us.usserver.novel.Novel;
import us.usserver.novel.novelEnum.AgeRating;
import us.usserver.novel.novelEnum.Genre;
import us.usserver.novel.novelEnum.Hashtag;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class NovelMother {
    public static Novel generateNovel(Author author) {
        EasyRandomParameters randomParameters = new EasyRandomParameters()
                .charset(StandardCharsets.UTF_8)
                .randomize(named("title").and(ofType(String.class)), new StringRandomizer(16))
                .randomize(named("thumbnail").and(ofType(String.class)), new StringRandomizer(500)) // URL이 들어갈 예정이라 대충 잡아 놓음
                .randomize(named("synopsis").and(ofType(String.class)), new StringRandomizer(300))
                .randomize(named("authorDescription").and(ofType(String.class)), new StringRandomizer(300))
                .randomize(named("hashtag").and(ofType(Set.class)), () -> EnumSet.of(Hashtag.HASHTAG1))
                .randomize(Genre.class, new EnumRandomizer<>(Genre.class))
                .randomize(AgeRating.class, new EnumRandomizer<>(AgeRating.class))
                .randomize(Author.class, () -> author);

        EasyRandom easyRandom = new EasyRandom(randomParameters);
        Novel novel = easyRandom.nextObject(Novel.class);
        return novel;
    }
}
