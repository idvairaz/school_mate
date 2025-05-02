package codereview.school_mate.model;

import jakarta.persistence.*;

import java.util.Set;
import lombok. Data;
import lombok. EqualsAndHashCode;

@Entity
@Data
@Table(name = "teachers")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "surname", nullable = false)
    private String surname;

    @Column(name = "patronymic")
    private String patronymic;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teachers_classes",
            joinColumns = @JoinColumn(name = "teacher_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "class_id",referencedColumnName = "id")
    )
    @EqualsAndHashCode.Exclude
    private Set<SchoolClass> classes;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teachers_subjects",
            joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "id")
    )
    @EqualsAndHashCode.Exclude
    private Set<Subject> subjects;
}