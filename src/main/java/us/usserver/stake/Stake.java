package us.usserver.stake;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import us.usserver.author.Author;
import us.usserver.base.BaseEntity;
import us.usserver.novel.Novel;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Stake extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stake_id")
    private Long id;

    @Setter
    @NotNull
    private Float percentage;

    @ManyToOne
    @JoinColumn
    private Novel novel;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;
}
