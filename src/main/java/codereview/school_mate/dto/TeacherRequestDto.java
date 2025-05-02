package codereview.school_mate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@SuperBuilder
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeacherRequestDto {
    @NotBlank(message = "First name cannot be empty")
    private String name;

    @NotBlank(message = "Last name cannot be empty")
    private String lastName;

    private String patronymic;

    @Builder.Default
    private Set<Long> subjectIds = new HashSet<>();
}