package davis.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Data
public class Grammar {
    @Id
    @Column(name="gid")
    private String gid;

    @Column(name="title")
    private String title;

    @Column(name="anchors")
    private String anchors;

    @Column(name="structure")
    private String structure;

    @Column(name="description")
    private String description;
}
