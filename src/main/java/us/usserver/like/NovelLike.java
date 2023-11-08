package us.usserver.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.author.Author;
import us.usserver.novel.Novel;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NovelLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "novelLike_id")
    private Long id;

    @ManyToOne
    @JoinColumn
    private Novel novel;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
