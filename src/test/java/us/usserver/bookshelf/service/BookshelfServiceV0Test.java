package us.usserver.bookshelf.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.authority.Authority;
import us.usserver.authority.AuthorityRepository;
import us.usserver.bookshelf.dto.NovelPreview;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.ChapterRepository;
import us.usserver.global.exception.AuthorNotFoundException;
import us.usserver.like.novel.NovelLike;
import us.usserver.like.novel.NovelLikeRepository;
import us.usserver.member.Member;
import us.usserver.member.MemberMother;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Rollback
@SpringBootTest
class BookshelfServiceV0Test {
    @Autowired
    private BookshelfServiceV0 bookshelfServiceV0;

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private ChapterRepository chapterRepository;
    @Autowired
    private AuthorityRepository authorityRepository;
    @Autowired
    private NovelLikeRepository novelLikeRepository;

    private Author author;
    private Novel novel;
    private Chapter chapter;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        authorRepository.save(author);

        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);

        novel.getChapters().add(chapter);
        novelRepository.save(novel);
        chapterRepository.save(chapter);
        authorityRepository.save(Authority.builder().novel(novel).author(author).build());
    }

    @AfterEach
    void setOff() {
        author.getViewedNovels().clear();
    }

    @Test
    @DisplayName("최근 본 소설 조회")
    void recentViewedNovels() {
        // given
        Novel newNovel = NovelMother.generateNovel(author);
        Chapter newChapter = ChapterMother.generateChapter(novel);
        newNovel.getChapters().add(newChapter);

        // when
        novelRepository.save(newNovel);
        chapterRepository.save(newChapter);
        author.getViewedNovels().add(novel);
        author.getViewedNovels().add(newNovel);
        authorRepository.save(author);
        List<NovelPreview> novelPreviews = bookshelfServiceV0.recentViewedNovels(author.getId());

        // then
        assertThat(novelPreviews.size()).isEqualTo(2);
    }


    @Test
    @DisplayName("최근 본 소설 조회 - 최근 본 소설 없음")
    void recentViewedNovels2() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);

        // when
        authorRepository.save(newAuthor);
        List<NovelPreview> novelPreviews = bookshelfServiceV0.recentViewedNovels(newAuthor.getId());

        // then
        assertThat(novelPreviews.size()).isZero();
    }

    @Test
    @DisplayName("내가 생성한 소설 조회")
    void createdNovels() {
        // given

        // when
        List<NovelPreview> novelPreviews = bookshelfServiceV0.createdNovels(author.getId());

        // then
        assertThat(novelPreviews.size()).isEqualTo(1);
        assertThat(novelPreviews.get(0).getTitle()).isEqualTo(novel.getTitle());
        assertThat(novelPreviews.get(0).getMainAuthor().getNickName()).isEqualTo(author.getNickname());
        assertThat(novelPreviews.get(0).getJoinedAuthor()).isEqualTo(1);
        assertThat(novelPreviews.get(0).getShortcuts()).contains(novel.getId().toString());
    }

    @Test
    @DisplayName("내가 생성한 소설 조회 - 0개")
    void createdNovels1() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);

        // when
        authorRepository.save(newAuthor);
        List<NovelPreview> novelPreviews = bookshelfServiceV0.createdNovels(newAuthor.getId());

        // then
        assertThat(novelPreviews.size()).isZero();
    }

    @Test
    @DisplayName("내가 생성한 소설 조회 - 2개 이상")
    void createdNovels2() {
        // given
        Novel newNovel1 = NovelMother.generateNovel(author);
        Novel newNovel2 = NovelMother.generateNovel(author);
        Novel newNovel3 = NovelMother.generateNovel(author);

        // when
        novelRepository.save(newNovel1);
        novelRepository.save(newNovel2);
        novelRepository.save(newNovel3);
        authorityRepository.save(Authority.builder().novel(newNovel1).author(author).build());
        authorityRepository.save(Authority.builder().novel(newNovel2).author(author).build());
        authorityRepository.save(Authority.builder().novel(newNovel3).author(author).build());
        List<NovelPreview> novelPreviews = bookshelfServiceV0.createdNovels(author.getId());

        // then
        assertThat(novelPreviews.size()).isEqualTo(4);
    }

    @Test
    @DisplayName("내가 생성한 소설 조회 - 삭제된 소설")
    void createdNovels3() {
        // given
        Novel newNovel1 = NovelMother.generateNovel(author);
        Novel newNovel2 = NovelMother.generateNovel(author);
        Novel newNovel3 = NovelMother.generateNovel(author);

        // when
        novelRepository.save(newNovel1);
        novelRepository.save(newNovel2);
        novelRepository.save(newNovel3);
        authorityRepository.save(Authority.builder().novel(newNovel1).author(author).build());
        authorityRepository.save(Authority.builder().novel(newNovel2).author(author).build());
        authorityRepository.save(Authority.builder().novel(newNovel3).author(author).build());
        List<NovelPreview> beforeNP = bookshelfServiceV0.createdNovels(author.getId());

        novelRepository.delete(newNovel1);
        novelRepository.delete(newNovel2);
        novelRepository.delete(newNovel3);
        List<NovelPreview> afterNP = bookshelfServiceV0.createdNovels(author.getId());


        // then
        assertThat(beforeNP.size()).isEqualTo(4);
        assertThat(afterNP.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("내가 참여 중인 소설 조회")
    void joinedNovels() {
        // given

        // when
        List<NovelPreview> novelPreviews = bookshelfServiceV0.joinedNovels(author.getId());

        // then
        assertThat(novelPreviews.size()).isEqualTo(1);
        assertThat(novelPreviews.get(0).getTitle()).isEqualTo(novel.getTitle());
        assertThat(novelPreviews.get(0).getThumbnail()).isEqualTo(novel.getThumbnail());
    }

    @Test
    @DisplayName("내가 참여 중인 소설 조회 - 0개")
    void joinedNovels1() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);

        // when
        authorRepository.save(newAuthor);
        List<NovelPreview> novelPreviews = bookshelfServiceV0.joinedNovels(newAuthor.getId());

        // then
        assertThat(novelPreviews.size()).isZero();
    }

    @Test
    @DisplayName("내가 참여 중인 소설 조회 - 2개 이상")
    void joinedNovels2() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);
        Novel newNovel1 = NovelMother.generateNovel(newAuthor);
        Novel newNovel2 = NovelMother.generateNovel(newAuthor);

        // when
        authorRepository.save(newAuthor);
        novelRepository.save(newNovel1);
        novelRepository.save(newNovel2);
        authorityRepository.save(Authority.builder().novel(novel).author(newAuthor).build());
        authorityRepository.save(Authority.builder().novel(newNovel1).author(newAuthor).build());
        authorityRepository.save(Authority.builder().novel(newNovel2).author(newAuthor).build());
        List<NovelPreview> novelPreviews = bookshelfServiceV0.joinedNovels(newAuthor.getId());

        // then
        assertThat(novelPreviews.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("내가 참여 중인 소설 조회 - 삭제된 소설")
    void joinedNovels3() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);
        Novel newNovel1 = NovelMother.generateNovel(newAuthor);
        Novel newNovel2 = NovelMother.generateNovel(newAuthor);

        // when
        authorRepository.save(newAuthor);
        novelRepository.save(newNovel1);
        novelRepository.save(newNovel2);
        authorityRepository.save(Authority.builder().novel(newNovel1).author(newAuthor).build());
        authorityRepository.save(Authority.builder().novel(newNovel2).author(newAuthor).build());
        List<NovelPreview> beforeNP = bookshelfServiceV0.joinedNovels(newAuthor.getId());

        novelRepository.delete(newNovel1);
        novelRepository.delete(newNovel2);
        List<NovelPreview> afterNP = bookshelfServiceV0.joinedNovels(newAuthor.getId());
        
        // then
        assertThat(beforeNP.size()).isEqualTo(2);
        assertThat(afterNP.size()).isZero();
    }


    @Test
    @DisplayName("좋아요 누른 소설 조회")
    void likedNovels() {
        // given
        NovelLike like = NovelLike.builder().novel(novel).author(author).build();

        // when
        novelLikeRepository.save(like);
        List<NovelPreview> novelPreviews = bookshelfServiceV0.likedNovels(author.getId());

        // then
        assertThat(novelPreviews.size()).isEqualTo(1);
        assertThat(novelPreviews.get(0).getTitle()).isEqualTo(novel.getTitle());
        assertThat(novelPreviews.get(0).getThumbnail()).isEqualTo(novel.getThumbnail());
    }

    @Test
    @DisplayName("좋아요 누른 소설 조회 - 0개")
    void likedNovels1() {
        // given

        // when
        List<NovelPreview> novelPreviews = bookshelfServiceV0.likedNovels(author.getId());

        // then
        assertThat(novelPreviews.size()).isZero();
    }


    @Test
    @DisplayName("좋아요 누른 소설 조회 - 2개 이상")
    void likedNovels2() {
        // given
        Novel newNovel1 = NovelMother.generateNovel(author);
        Novel newNovel2 = NovelMother.generateNovel(author);
        NovelLike like1 = NovelLike.builder().novel(newNovel1).author(author).build();
        NovelLike like2 = NovelLike.builder().novel(newNovel2).author(author).build();

        // when
        novelRepository.save(newNovel1);
        novelRepository.save(newNovel2);
        novelLikeRepository.save(like1);
        novelLikeRepository.save(like2);
        List<NovelPreview> novelPreviews = bookshelfServiceV0.likedNovels(author.getId());

        // then
        assertThat(novelPreviews.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("좋아요 누른 소설 조회 - 좋아요 누른 소설 삭제")
    void likedNovels3() {
        // given
        Novel newNovel = NovelMother.generateNovel(author);
        NovelLike like = NovelLike.builder().novel(newNovel).author(author).build();

        // when
        novelRepository.save(newNovel);
        novelLikeRepository.save(like);
        List<NovelPreview> beforeNP = bookshelfServiceV0.likedNovels(author.getId());
        novelRepository.delete(newNovel);
        List<NovelPreview> afterNP = bookshelfServiceV0.likedNovels(author.getId());

        // then
        assertThat(beforeNP.size()).isEqualTo(1);
        assertThat(afterNP.size()).isZero();
    }



    @Test
    @DisplayName("모든 책장 조회 기능 중 - 존재하지 않는 사용자")
    void recentViewedNovels1() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);

        // when // then
        assertThrows(AuthorNotFoundException.class,
                () -> bookshelfServiceV0.recentViewedNovels(newAuthor.getId()));
        assertThrows(AuthorNotFoundException.class,
                () -> bookshelfServiceV0.createdNovels(newAuthor.getId()));
        assertThrows(AuthorNotFoundException.class,
                () -> bookshelfServiceV0.joinedNovels(newAuthor.getId()));
        assertThrows(AuthorNotFoundException.class,
                () -> bookshelfServiceV0.likedNovels(newAuthor.getId()));

    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}