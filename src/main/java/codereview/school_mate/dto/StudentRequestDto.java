package codereview.school_mate.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDto {
    private String name;
    private String surname;
    private String patronymic;
    private Long schoolClassId;
    private Long parentId;
}