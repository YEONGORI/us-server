package us.usserver.chapter;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import us.usserver.base.BaseEntity;
import us.usserver.novel.Novel;
import us.usserver.paragraph.Paragraph;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chapter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    @NotNull
    private Integer part;

    @ManyToOne
    @JoinColumn
    private Novel novel;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Paragraph> paragraphs = new ArrayList<>();

    // TODO: 연관 관계 설정을 위한 method 필요
}
