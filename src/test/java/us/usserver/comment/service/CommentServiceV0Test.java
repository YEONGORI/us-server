package us.usserver.comment.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.chapter.Chapter;
import us.usserver.chapter.ChapterMother;
import us.usserver.chapter.ChapterRepository;
import us.usserver.comment.Comment;
import us.usserver.comment.CommentMother;
import us.usserver.comment.CommentRepository;
import us.usserver.comment.dto.CommentContent;
import us.usserver.comment.dto.CommentInfo;
import us.usserver.global.exception.*;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.repository.NovelJpaRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceV0Test {
    @Autowired
    private CommentServiceV0 commentServiceV0;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private NovelJpaRepository novelJpaRepository;
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
        novelJpaRepository.save(novel);
        chapterRepository.save(chapter);
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 0개")
    void getCommentsOfNovel() {
        // given

        // when
        List<CommentInfo> comments = commentServiceV0.getCommentsOfNovel(novel.getId());

        // then
        assertThat(comments).isEqualTo(Collections.emptyList());
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
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        List<CommentInfo> comments = commentServiceV0.getCommentsOfNovel(novel.getId());

        // then
        assertThat(comments.size()).isEqualTo(3);
        assertThat(comments.get(0).getLocation()).isEqualTo(novel.getTitle());
        assertThat(comments.get(1).getLocation()).isEqualTo(novel.getTitle());
        assertThat(comments.get(2).getLocation()).isEqualTo(novel.getTitle());
    }

    @Test
    @DisplayName("소설 댓글 불러오기 - 삭제된 소설")
    void getCommentsOfNovel2() {
        // given
        Novel newNovel = NovelMother.generateNovel(author);
        Comment newComment = CommentMother.generateComment(author, newNovel, null);
        newNovel.getComments().add(newComment);

        // when
        novelJpaRepository.save(newNovel);
        commentRepository.save(newComment);
        List<CommentInfo> beforeComments = commentServiceV0.getCommentsOfNovel(newNovel.getId());
        novelJpaRepository.delete(newNovel);
        List<Comment> afterComments = commentRepository.findAllByAuthor(author);
        assertThrows(NovelNotFoundException.class,
                () -> commentServiceV0.getCommentsOfNovel(newNovel.getId()));

        // then
        assertThat(beforeComments.get(0).getLocation()).isEqualTo(newNovel.getTitle());
        for (Comment comment : afterComments) {
            assertThat(comment.getNovel().getTitle()).isNotEqualTo(newNovel.getTitle());
        }
    }

    @Test
    @DisplayName("회차 댓글 불러오기 - 0개")
    void getCommentsOfChapter() {
        // given

        // when
        List<CommentInfo> comments = commentServiceV0.getCommentsOfChapter(chapter.getId());

        // then
        assertThat(comments.size()).isZero();
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
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        List<CommentInfo> comments = commentServiceV0.getCommentsOfChapter(chapter.getId());

        // then
        assertThat(comments.size()).isEqualTo(3);
        assertThat(comments.get(0).getLocation()).isEqualTo(chapter.getTitle());
        assertThat(comments.get(1).getLocation()).isEqualTo(chapter.getTitle());
        assertThat(comments.get(2).getLocation()).isEqualTo(chapter.getTitle());
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
        List<CommentInfo> beforeComments = commentServiceV0.getCommentsOfChapter(newChapter.getId());
        chapterRepository.delete(newChapter);
        assertThrows(ChapterNotFoundException.class,
                () -> commentServiceV0.getCommentsOfChapter(newChapter.getId()));
        List<Comment> afterComments = commentRepository.findAllByAuthor(author);

        // then
        assertThat(beforeComments.size()).isEqualTo(1);
        assertThat(beforeComments.get(0).getLocation()).isEqualTo(newChapter.getTitle());
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
        CommentInfo commentInfo = commentServiceV0.writeCommentOnNovel(novel.getId(), author.getId(), commentContent);

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
                () -> commentServiceV0.writeCommentOnNovel(novel.getId(), newAuthor.getId(), commentContent));
        List<Comment> comments = commentRepository.findAllByNovel(novel);

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
                () -> commentServiceV0.writeCommentOnNovel(novel.getId(), author.getId(), commentContent1));
        assertThrows(CommentLengthOutOfRangeException.class,
                () -> commentServiceV0.writeCommentOnNovel(novel.getId(), author.getId(), commentContent2));
    }

    @Test
    @DisplayName("회차 댓글 작성하기")
    void writeCommentOnChapter() {
        // given
        CommentContent commentContent = CommentMother.generateContent();

        // when
        CommentInfo commentInfo = commentServiceV0.writeCommentOnChapter(chapter.getId(), author.getId(), commentContent);

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
                () -> commentServiceV0.writeCommentOnChapter(chapter.getId(), newAuthor.getId(), commentContent));
        List<Comment> comments = commentRepository.findAllByChapter(chapter);

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
                () -> commentServiceV0.writeCommentOnChapter(chapter.getId(), author.getId(), commentContent1));
        assertThrows(CommentLengthOutOfRangeException.class,
                () -> commentServiceV0.writeCommentOnChapter(chapter.getId(), author.getId(), commentContent2));
    }

    @Test
    @DisplayName("내가 쓴 댓글 로드 - 0개")
    void getCommentsByAuthor() {
        // given

        // when
        List<CommentInfo> commentsByAuthor = commentServiceV0.getCommentsByAuthor(author.getId());

        // then
        assertThat(commentsByAuthor.size()).isZero();
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
        commentRepository.save(novelComment);
        commentRepository.save(chapterComment);
        List<CommentInfo> commentsByAuthor = commentServiceV0.getCommentsByAuthor(author.getId());

        // then
        assertThat(commentsByAuthor.size()).isEqualTo(2);
        assertThat(commentsByAuthor.get(0).getLocation()).isEqualTo(novel.getTitle());
        assertThat(commentsByAuthor.get(1).getLocation()).isEqualTo(chapter.getTitle());
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
        commentRepository.save(novelComment);
        commentRepository.save(chapterComment);
        List<CommentInfo> beforeComments = commentServiceV0.getCommentsByAuthor(author.getId());
        commentRepository.delete(novelComment);
        commentRepository.delete(chapterComment);
        List<CommentInfo> afterComments = commentServiceV0.getCommentsByAuthor(author.getId());

        // then
        assertThat(beforeComments.size()).isEqualTo(2);
        assertThat(beforeComments.get(0).getLocation()).isEqualTo(novel.getTitle());
        assertThat(beforeComments.get(1).getLocation()).isEqualTo(chapter.getTitle());
        assertThat(afterComments.size()).isZero();
    }

    @Test
    @DisplayName("소설 댓글 삭제")
    void deleteComment() {
        // given
        Comment comment = CommentMother.generateComment(author, novel, null);

        // when
        novel.getComments().add(comment);
        commentRepository.save(comment);
        commentServiceV0.deleteComment(comment.getId(), author.getId());
        Optional<Comment> commentById = commentRepository.getCommentById(comment.getId());

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
        commentRepository.save(comment);
        commentServiceV0.deleteComment(comment.getId(), author.getId());
        Optional<Comment> commentById = commentRepository.getCommentById(comment.getId());

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
                () -> commentServiceV0.deleteComment(comment.getId(), author.getId()));
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
        commentRepository.save(comment);
        authorRepository.save(newAuthor);
        assertThrows(AuthorNotAuthorizedException.class,
                () -> commentServiceV0.deleteComment(comment.getId(), newAuthor.getId()));
        Optional<Comment> commentById = commentRepository.getCommentById(comment.getId());

        // then
        assertFalse(commentById.isEmpty());
    }

    private void setMember(Author author) {
        Member member = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member);
        author.setMember(member);
    }
}