package us.usserver.domain.authority;

import jakarta.persistence.*;
import lombok.*;
import us.usserver.domain.member.entity.Author;
import us.usserver.domain.novel.Novel;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn
    private Novel novel;

    @Setter
    @ManyToOne
    @JoinColumn
    private Author author;

    public void takeNovel(Novel novel) {
        this.novel = novel;
        novel.getAuthorities().add(this);
    }
}
