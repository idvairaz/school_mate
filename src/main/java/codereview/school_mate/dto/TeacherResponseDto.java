package codereview.school_mate.dto;

import codereview.school_mate.model.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherResponseDto {
    private Long id;
    private String name;
    private String lastName;
    private String patronymic;
    private Set<SubjectResponseDto> subjects;
}