package codereview.school_mate.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportDto {

    private Long id;
    private String subject;
    private String teacher;
    private LocalDate date;
    private String student;
    private String message;
}
