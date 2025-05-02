package codereview.school_mate.dto;
import codereview.school_mate.model.Parent;
import codereview.school_mate.model.SchoolClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto {
    private Long id;
    private String name;
    private String surname;
    private String patronymic;
    private SchoolClass schoolClass;
    private Parent parent;
}