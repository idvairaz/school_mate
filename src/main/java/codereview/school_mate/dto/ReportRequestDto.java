package codereview.school_mate.dto;

import codereview.school_mate.model.Student;
import codereview.school_mate.model.Subject;
import codereview.school_mate.model.Teacher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    private Subject subject;
    private Teacher teacher;
    private Student student;
    private LocalDate date;
    private String message;

    private Long SubjectId;
    private Long TeacherId;
    private Long StudentId;
}
