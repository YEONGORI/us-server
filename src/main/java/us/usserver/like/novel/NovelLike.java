package us.usserver.like.novel;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.like.Like;
import us.usserver.novel.Novel;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovelLike extends Like {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "novel_like_id")
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "author_id")
//    private Author author;

    @ManyToOne
    @JoinColumn
    private Novel novel;
}
