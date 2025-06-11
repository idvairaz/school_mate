package codereview.school_mate.dto.responce;

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
    private String surname;
    private String patronymic;
    private Set<SubjectResponseDto> subjects;
}