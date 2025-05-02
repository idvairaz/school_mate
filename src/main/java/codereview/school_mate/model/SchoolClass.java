package codereview.school_mate.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta. persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;

import java.util.Set;
import lombok. Data;
import lombok. EqualsAndHashCode;

@Entity
@Data
@Table(name = "school_classes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SchoolClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "classes")
    @EqualsAndHashCode.Exclude
    private Set<Teacher> teachers;

}