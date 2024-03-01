package us.usserver.comment.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import us.usserver.author.AuthorMother;
import us.usserver.chapter.ChapterMother;
import us.usserver.comment.CommentMother;
import us.usserver.domain.author.entity.Author;
import us.usserver.domain.author.repository.AuthorRepository;
import us.usserver.domain.chapter.entity.Chapter;
import us.usserver.domain.chapter.repository.ChapterRepository;
import us.usserver.domain.comment.dto.CommentContent;
import us.usserver.domain.comment.dto.CommentInfo;
import us.usserver.domain.comment.dto.GetCommentResponse;
import us.usserver.domain.comment.entity.Comment;
import us.usserver.domain.comment.repository.CommentRepository;
import us.usserver.domain.comment.service.CommentService;
import us.usserver.domain.member.entity.Member;
import us.usserver.domain.member.repository.MemberRepository;
import us.usserver.domain.novel.entity.Novel;
import us.usserver.domain.novel.repository.NovelRepository;
import us.usserver.global.exception.*;
import us.usserver.member.MemberMother;
import us.usserver.novel.NovelMother;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Rollback
@SpringBootTest
class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentJpaRepository;
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChapterRepository chapterRepository;

    private Novel novel;
    private Author author;
    private Chapter chapter;

    @BeforeEach
    void setUp() {
        author = AuthorMother.generateAuthor();
        setMember(author);
        novel = NovelMother.generateNovel(author);
        chapter = ChapterMother.generateChapter(novel);
        novel.getChapters().add(chapter);

        authorRepository.save(author);
        novelRepository.save(novel);
        chapterRepository.save(chapter);
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 0개")
    void getCommentsOfNovel() {
        // given

        // when
        GetCommentResponse commentsOfNovel = commentService.getCommentsOfNovel(novel.getId());

        // then
        assertThat(commentsOfNovel.getCommentInfos()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - n개")
    void getCommentsOfNovel1() {
        // given
        Comment comment1 = CommentMother.generateComment(author, novel, chapter);
        Comment comment2 = CommentMother.generateComment(author, novel, chapter);
        Comment comment3 = CommentMother.generateComment(author, novel, chapter);

        // when
        novel.getComments().add(comment1);
        novel.getComments().add(comment2);
        novel.getComments().add(comment3);
        commentJpaRepository.save(comment1);
        commentJpaRepository.save(comment2);
        commentJpaRepository.save(comment3);
        GetCommentResponse commentsOfNovel = commentService.getCommentsOfNovel(novel.getId());

        // then
        assertThat(commentsOfNovel.getCommentInfos().size()).isEqualTo(3);
        assertThat(commentsOfNovel.getCommentInfos().get(0).getLocation()).isEqualTo(novel.getTitle());
        assertThat(commentsOfNovel.getCommentInfos().get(1).getLocation()).isEqualTo(novel.getTitle());
        assertThat(commentsOfNovel.getCommentInfos().get(2).getLocation()).isEqualTo(novel.getTitle());
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 삭제된 소설")
    void getCommentsOfNovel2() {
        // given
        Novel newNovel = NovelMother.generateNovel(author);
        Comment newComment = CommentMother.generateComment(author, newNovel, null);
        newNovel.getComments().add(newComment);

        // when
        novelRepository.save(newNovel);
        commentJpaRepository.save(newComment);
        GetCommentResponse before = commentService.getCommentsOfNovel(newNovel.getId());
        novelRepository.delete(newNovel);
        List<Comment> afterComments = commentJpaRepository.findAllByAuthor(author);
        assertThrows(NovelNotFoundException.class,
                () -> commentService.getCommentsOfNovel(newNovel.getId()));

        // then
        assertThat(before.getCommentInfos().get(0).getLocation()).isEqualTo(newNovel.getTitle());
        for (Comment comment : afterComments) {
            assertThat(comment.getNovel().getTitle()).isNotEqualTo(newNovel.getTitle());
        }
    }

    @Test
    @DisplayName("회차 댓글 불러오기 - 0개")
    void getCommentsOfChapter() {
        // given

        // when
        GetCommentResponse commentsOfChapter = commentService.getCommentsOfChapter(chapter.getId());

        // then
        assertThat(commentsOfChapter.getCommentInfos().size()).isZero();
    }

    @Test
    @DisplayName("회차 댓글 불러오기 - n개")
    void getCommentsOfChapter1() {
        // given
        Comment comment1 = CommentMother.generateComment(author, novel, chapter);
        Comment comment2 = CommentMother.generateComment(author, novel, chapter);
        Comment comment3 = CommentMother.generateComment(author, novel, chapter);

        // when
        chapter.getComments().add(comment1);
        chapter.getComments().add(comment2);
        chapter.getComments().add(comment3);
        commentJpaRepository.save(comment1);
        commentJpaRepository.save(comment2);
        commentJpaRepository.save(comment3);
        GetCommentResponse commentsOfChapter = commentService.getCommentsOfChapter(chapter.getId());

        // then
        assertThat(commentsOfChapter.getCommentInfos().size()).isEqualTo(3);
        assertThat(commentsOfChapter.getCommentInfos().get(0).getLocation()).isEqualTo(chapter.getTitle());
        assertThat(commentsOfChapter.getCommentInfos().get(1).getLocation()).isEqualTo(chapter.getTitle());
        assertThat(commentsOfChapter.getCommentInfos().get(2).getLocation()).isEqualTo(chapter.getTitle());
    }

    @Test
    @DisplayName("회차 댓글 불러오기 - 삭제된 회차(챕터)")
    void getCommentsOfChapter2() {
        // given
        Chapter newChapter = ChapterMother.generateChapter(novel);
        Comment newComment = CommentMother.generateComment(author, novel, newChapter);

        // when
        novel.getChapters().add(newChapter);
        newChapter.getComments().add(newComment);
        chapterRepository.save(newChapter);
        GetCommentResponse before = commentService.getCommentsOfChapter(newChapter.getId());
        chapterRepository.delete(newChapter);
        assertThrows(ChapterNotFoundException.class,
                () -> commentService.getCommentsOfChapter(newChapter.getId()));
        List<Comment> afterComments = commentJpaRepository.findAllByAuthor(author);

        // then
        assertThat(before.getCommentInfos().size()).isEqualTo(1);
        assertThat(before.getCommentInfos().get(0).getLocation()).isEqualTo(newChapter.getTitle());
        for (Comment comment : afterComments) {
            assertThat(comment.getChapter().getTitle()).isNotEqualTo(newChapter.getTitle());
        }
    }

    @Test
    @DisplayName("소설 댓글 작성하기")
    void writeCommentOnNovel() {
        // given
        CommentContent commentContent = CommentMother.generateContent();

        // when
        CommentInfo commentInfo = commentService.writeCommentOnNovel(novel.getId(), author.getId(), commentContent);

        // then
        assertThat(commentInfo.getAuthorName()).isEqualTo(author.getNickname());
        assertThat(commentInfo.getLocation()).isEqualTo(novel.getTitle());
        assertThat(commentInfo.getContent()).isEqualTo(commentContent.getContent());
        assertThat(commentInfo.getLikeCnt()).isZero();
    }

    @Test
    @DisplayName("소설 댓글 작성하기 - 존재하지 않는 작가")
    void writeCommentOnNovel1() {
        // given
        CommentContent commentContent = CommentMother.generateContent();
        Author newAuthor = AuthorMother.generateAuthor();

        // when
        assertThrows(AuthorNotFoundException.class,
                () -> commentService.writeCommentOnNovel(novel.getId(), newAuthor.getId(), commentContent));
        List<Comment> comments = commentJpaRepository.findAllByNovel(novel);

        // then
        assertThat(comments.size()).isZero();
    }

    @Test
    @DisplayName("소설 댓글 작성 오류 - 0자 or 300자 초과")
    void writeCommentOnNovel2() {
        // given
        String content1 = RandomStringUtils.random(301);
        String content2 = "";
        CommentContent commentContent1 = CommentContent.builder().content(content1).build();
        CommentContent commentContent2 = CommentContent.builder().content(content2).build();

        // when then
        assertThrows(CommentLengthOutOfRangeException.class,
                () -> commentService.writeCommentOnNovel(novel.getId(), author.getId(), commentContent1));
        assertThrows(CommentLengthOutOfRangeException.class,
                () -> commentService.writeCommentOnNovel(novel.getId(), author.getId(), commentContent2));
    }

    @Test
    @DisplayName("회차 댓글 작성하기")
    void writeCommentOnChapter() {
        // given
        CommentContent commentContent = CommentMother.generateContent();

        // when
        CommentInfo commentInfo = commentService.writeCommentOnChapter(chapter.getId(), author.getId(), commentContent);

        // then
        assertThat(commentInfo.getAuthorName()).isEqualTo(author.getNickname());
        assertThat(commentInfo.getLocation()).isEqualTo(chapter.getTitle());
        assertThat(commentInfo.getContent()).isEqualTo(commentContent.getContent());
        assertThat(commentInfo.getLikeCnt()).isZero();
    }

    @Test
    @DisplayName("회차 댓글 작성하기 - 존재하지 않는 작가")
    void writeCommentOnChapter1() {
        // given
        CommentContent commentContent = CommentMother.generateContent();
        Author newAuthor = AuthorMother.generateAuthor();

        // when
        assertThrows(AuthorNotFoundException.class,
                () -> commentService.writeCommentOnChapter(chapter.getId(), newAuthor.getId(), commentContent));
        List<Comment> comments = commentJpaRepository.findAllByChapter(chapter);

        // then
        assertThat(comments.size()).isZero();
    }

    @Test
    @DisplayName("회차 댓글 작성하기 - 0자 or 300자 초과")
    void writeCommentOnChapter2() {
        // given
        String content1 = RandomStringUtils.random(301);
        String content2 = "";
        CommentContent commentContent1 = CommentContent.builder().content(content1).build();
        CommentContent commentContent2 = CommentContent.builder().content(content2).build();

        // when then
        assertThrows(CommentLengthOutOfRangeException.class,
                () -> commentService.writeCommentOnChapter(chapter.getId(), author.getId(), commentContent1));
        assertThrows(CommentLengthOutOfRangeException.class,
                () -> commentService.writeCommentOnChapter(chapter.getId(), author.getId(), commentContent2));
    }

    @Test
    @DisplayName("내가 쓴 댓글 로드 - 0개")
    void getCommentsByAuthor() {
        // given

        // when
        GetCommentResponse commentsByAuthor = commentService.getCommentsByAuthor(author.getId());

        // then
        assertThat(commentsByAuthor.getCommentInfos().size()).isZero();
    }

    @Test
    @DisplayName("소설, 회차 모두에서 내가 쓴 댓글 로드 - n개")
    void getCommentsByAuthor1() {
        // given
        Comment novelComment = CommentMother.generateComment(author, novel, null);
        Comment chapterComment = CommentMother.generateComment(author, novel, chapter);

        // when
        novel.getComments().add(novelComment);
        chapter.getComments().add(chapterComment);
        commentJpaRepository.save(novelComment);
        commentJpaRepository.save(chapterComment);

        GetCommentResponse commentsByAuthor = commentService.getCommentsByAuthor(author.getId());

        // then
        assertThat(commentsByAuthor.getCommentInfos().size()).isEqualTo(2);
        assertThat(commentsByAuthor.getCommentInfos().get(0).getLocation()).isEqualTo(novel.getTitle());
        assertThat(commentsByAuthor.getCommentInfos().get(1).getLocation()).isEqualTo(chapter.getTitle());
    }

    @Test
    @DisplayName("삭제된 소설, 회차에서 삭제된 댓글 확인")
    void getCommentsByAuthor2() {
        // given
        Comment novelComment = CommentMother.generateComment(author, novel, null);
        Comment chapterComment = CommentMother.generateComment(author, novel, chapter);

        // when
        novel.getComments().add(novelComment);
        chapter.getComments().add(chapterComment);
        commentJpaRepository.save(novelComment);
        commentJpaRepository.save(chapterComment);
        GetCommentResponse before = commentService.getCommentsByAuthor(author.getId());
        commentJpaRepository.delete(novelComment);
        commentJpaRepository.delete(chapterComment);
        GetCommentResponse after = commentService.getCommentsByAuthor(author.getId());

        // then
        assertThat(before.getCommentInfos().size()).isEqualTo(2);
        assertThat(before.getCommentInfos().get(0).getLocation()).isEqualTo(novel.getTitle());
        assertThat(before.getCommentInfos().get(1).getLocation()).isEqualTo(chapter.getTitle());
        assertThat(after.getCommentInfos().size()).isZero();
    }

    @Test
    @DisplayName("소설 댓글 삭제")
    void deleteComment() {
        // given
        Comment comment = CommentMother.generateComment(author, novel, null);

        // when
        novel.getComments().add(comment);
        commentJpaRepository.save(comment);
        commentService.deleteComment(comment.getId(), author.getId());
        Optional<Comment> commentById = commentJpaRepository.getCommentById(comment.getId());

        // then
        assertTrue(commentById.isEmpty());
    }

    @Test
    @DisplayName("회차 댓글 삭제")
    void deleteComment1() {
        // given
        Comment comment = CommentMother.generateComment(author, novel, chapter);

        // when
        chapter.getComments().add(comment);
        commentJpaRepository.save(comment);
        commentService.deleteComment(comment.getId(), author.getId());
        Optional<Comment> commentById = commentJpaRepository.getCommentById(comment.getId());

        // then
        assertTrue(commentById.isEmpty());
    }

    @Test
    @DisplayName("댓글 삭제 - 존재하지 않는 댓글")
    void deleteComment2() {
        // given
        Comment comment = CommentMother.generateComment(author, novel, chapter);

        // when then
        assertThrows(CommentNotFoundException.class,
                () -> commentService.deleteComment(comment.getId(), author.getId()));
    }

    @Test
    @DisplayName("댓글 삭제 - 작성자가 본인이 아닌 댓글")
    void deleteComment3() {
        // given
        Author newAuthor = AuthorMother.generateAuthor();
        setMember(newAuthor);
        Comment comment = CommentMother.generateComment(author, novel, chapter);

        // when
        chapter.getComments().add(comment);
        commentJpaRepository.save(comment);
        authorRepository.save(newAuthor);
        assertThrows(AuthorNotAuthorizedException.class,
                () -> commentService.deleteComment(comment.getId(), newAuthor.getId()));
        Optional<Comment> commentById = commentJpaRepository.getCommentById(comment.getId());

        // then
        assertFalse(commentById.isEmpty());
    }

    private void setMember(Author author) {
        Member member = MemberMother.generateMember();
        memberRepository.save(member);
        author.setMember(member);
    }
}