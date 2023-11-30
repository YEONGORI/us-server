package us.usserver.novel.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import us.usserver.author.Author;
import us.usserver.author.AuthorMother;
import us.usserver.author.AuthorRepository;
import us.usserver.global.exception.MainAuthorIsNotMatchedException;
import us.usserver.global.exception.NovelNotFoundException;
import us.usserver.member.Member;
import us.usserver.member.MemberRepository;
import us.usserver.member.memberEnum.Gender;
import us.usserver.novel.Novel;
import us.usserver.novel.NovelMother;
import us.usserver.novel.NovelRepository;
import us.usserver.novel.dto.AuthorDescription;
import us.usserver.novel.dto.DetailInfoResponse;
import us.usserver.novel.dto.NovelInfoResponse;
import us.usserver.novel.dto.NovelSynopsis;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class NovelServiceV0Test {
    @Autowired
    private NovelRepository novelRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private NovelServiceV0 novelServiceV0;

    private Novel novel;
    private Novel dummyNovel;
    private Author author;
    private Author dummyAuthor;

    @BeforeEach
    void setup() {
        Member member1 = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member1);

        Member member2 = Member.builder().age(1).gender(Gender.MALE).build();
        memberRepository.save(member2);

        author = AuthorMother.generateAuthor();
        author.setMember(member1);
        authorRepository.save(author);

        dummyAuthor = AuthorMother.generateAuthor();
        dummyAuthor.setMember(member2);
        authorRepository.save(dummyAuthor);

        novel = NovelMother.generateNovel(author);
        novelRepository.save(novel);

        dummyNovel = NovelMother.generateNovel(dummyAuthor);
        novelRepository.save(dummyNovel);
    }

    @Test
    @DisplayName("소설 정보 확인")
    void getNovelInfo() {
        NovelInfoResponse novelInfoResponse = assertDoesNotThrow(
                () -> novelServiceV0.getNovelInfo(novel.getId()));

        assertThat(novelInfoResponse.getTitle()).isEqualTo(novel.getTitle());
        assertThat(novelInfoResponse.getCreatedAuthor().getId()).isEqualTo(novel.getMainAuthor().getId());
        assertThat(novelInfoResponse.getGenre()).isEqualTo(novel.getGenre());
        assertThat(novelInfoResponse.getHashtag()).isEqualTo(novel.getHashtag());

        assertThat(novelInfoResponse.getNovelSharelUrl()).contains("/novel/" + novel.getId());
        assertThat(novelInfoResponse.getDetailNovelInfoUrl()).contains("novel/" + novel.getId() + "/detail");
    }

    @Test
    @DisplayName("존재하지 않는 소설 정보 확인")
    void getNotExistNovel() {
        assertThrows(NovelNotFoundException.class,
                () -> novelServiceV0.getNovelInfo(novel.getId() + 9999));
    }

    @Test
    @DisplayName("소설 상세 정보 확인")
    void getNovelDetailInfo() {
        DetailInfoResponse detailInfoResponse = assertDoesNotThrow(
                () -> novelServiceV0.getNovelDetailInfo(novel.getId()));

        assertThat(detailInfoResponse.getTitle()).isEqualTo(novel.getTitle());
        assertThat(detailInfoResponse.getThumbnail()).isEqualTo(novel.getThumbnail());
        assertThat(detailInfoResponse.getSynopsis()).isEqualTo(novel.getSynopsis());
        assertThat(detailInfoResponse.getAuthorName()).isEqualTo(novel.getMainAuthor().getNickname());
        assertThat(detailInfoResponse.getAuthorIntroduction()).isEqualTo(novel.getAuthorDescription());
        assertThat(detailInfoResponse.getAgeRating()).isEqualTo(novel.getAgeRating());
        assertThat(detailInfoResponse.getGenre()).isEqualTo(novel.getGenre());
        assertThat(detailInfoResponse.getHashtags()).isEqualTo(novel.getHashtag());
        assertThat(detailInfoResponse.getStakeInfos()).isEqualTo(Collections.emptyList());
    }

    @Test
    @DisplayName("쇼셜 소개 수정")
    void modifyNovelSynopsis() {
        NovelSynopsis synopsisRequest = NovelMother.generateSysnopsis();
        NovelSynopsis synopsisResponse = assertDoesNotThrow(
                () -> novelServiceV0.modifyNovelSynopsis(novel.getId(), author.getId(), synopsisRequest));

        assertThat(synopsisRequest.getSynopsis()).isEqualTo(synopsisResponse.getSynopsis());
    }
    
    @Test
    @DisplayName("권한 없는 작가의 소설 소개 수정")
    void modifySysnopsisNotAuthority() {
        NovelSynopsis synopsisRequest = NovelMother.generateSysnopsis();
        assertThrows(MainAuthorIsNotMatchedException.class,
                () -> novelServiceV0.modifyNovelSynopsis(novel.getId(), dummyAuthor.getId(), synopsisRequest));
    }

    @Test
    @DisplayName("작가 소개 수정")
    void modifyAuthorDescription() {
        AuthorDescription descriptionRequest = NovelMother.generateDescription();
        AuthorDescription desriptionResponse = assertDoesNotThrow(
                () -> novelServiceV0.modifyAuthorDescription(novel.getId(), author.getId(), descriptionRequest));

        assertThat(descriptionRequest.getDescription()).isEqualTo(desriptionResponse.getDescription());
    }

    @Test
    @DisplayName("권한 없는 작가의 작가 소개 수정")
    void modifyDescriptionNotAuthority() {
        AuthorDescription authorDescription = NovelMother.generateDescription();
        assertThrows(MainAuthorIsNotMatchedException.class,
                () -> novelServiceV0.modifyAuthorDescription(novel.getId(), dummyAuthor.getId(), authorDescription));
    }
}