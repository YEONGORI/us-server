package us.usserver.author;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.jeasy.random.randomizers.text.StringRandomizer;
import us.usserver.domain.author.entity.Author;

import java.nio.charset.StandardCharsets;

import static org.jeasy.random.FieldPredicates.named;
import static org.jeasy.random.FieldPredicates.ofType;

public class AuthorMother {

    public static Author generateAuthor() {
        EasyRandomParameters randomParameters = new EasyRandomParameters()
                .charset(StandardCharsets.UTF_8)
                .randomize(named("nickname").and(ofType(String.class)), new StringRandomizer(11))
                .randomize(named("introduction").and(ofType(String.class)), new StringRandomizer(100))
                .randomize(named("profileImg").and(ofType(String.class)), new StringRandomizer(300));

        EasyRandom easyRandom = new EasyRandom(randomParameters);
        return easyRandom.nextObject(Author.class);
    }

}
