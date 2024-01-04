package us.usserver.comment.novel.service;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NoCommentServiceV0Test {
//    @Autowired
//    private NoCommentServiceV0 noCommentServiceV0;
//    @Autowired
//    private NovelRepository novelRepository;
//    @Autowired
//    private AuthorRepository authorRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//
//    private Novel novel;
//    private Author author;
//
//    @BeforeEach
//    void setUp() {
//        author = AuthorMother.generateAuthor();
//        setMember(author);
//        novel = NovelMother.generateNovel(author);
//
//        authorRepository.save(author);
//        novelRepository.save(novel);
//    }
//
//    @Test
//    @DisplayName("소설 모든 댓글 불러오기")
//    void getCommentsInNovel() {
//        List<CommentsInNovelRes> commentsInNovelRes = assertDoesNotThrow(
//                () -> noCommentServiceV0.getCommentsInNovel(novel.getId()));
//
//        assertThat(commentsInNovelRes).isNotNull();
//    }
//
//    @Test
//    @DisplayName("존재 하지 않는 소설 댓글 불러오기")
//    void getCommentsInNotExistNovel() {
//        // given
//        Long randomNovelId = 99L;
//
//        // when then
//        assertThrows(NovelNotFoundException.class,
//                () -> noCommentServiceV0.getCommentsInNovel(randomNovelId));
//    }
//
//    @Test
//    @DisplayName("소설 댓글 작성하기")
//    void postCommentInNovel() {
//        // given
//        PostCommentReq testComment = PostCommentReq.builder()
//                .content("TEST COMMENT")
//                .build();
//
//        // when
//        List<CommentsInNovelRes> commentsInNovel = assertDoesNotThrow(() ->
//                noCommentServiceV0.postCommentInNovel(
//                        novel.getId(), author.getId(), testComment)
//        );
//
//        // then
//        assertThat(commentsInNovel.size()).isGreaterThan(0);
//    }
//
//    @Test
//    @DisplayName("존재 하지 않는 소설 댓글 작성하기")
//    void postCommentInNotExistNovel() {
//        // given
//        Long randomNovelId = 99L;
//        PostCommentReq req = PostCommentReq.builder().content("").build();
//
//        // when then
//        assertThrows(NovelNotFoundException.class,
//                () -> noCommentServiceV0.postCommentInNovel(
//                        randomNovelId,
//                        author.getId(),
//                        req)
//        );
//    }
//
//    @Test
//    @DisplayName("존재 하지 않는 작가의 댓글 작성")
//    void postCommentInNotExistAuthor() {
//        // given
//        Long randomAuthorId = 99L;
//        PostCommentReq req = PostCommentReq.builder().content("").build();
//
//        // when then
//        assertThrows(AuthorNotFoundException.class,
//                () -> noCommentServiceV0.postCommentInNovel(
//                        novel.getId(),
//                        randomAuthorId,
//                        req));
//    }
//
//    private void setMember(Author author) {
//        Member member = Member.builder().age(1).gender(Gender.MALE).build();
//        memberRepository.save(member);
//        author.setMember(member);
//    }
}