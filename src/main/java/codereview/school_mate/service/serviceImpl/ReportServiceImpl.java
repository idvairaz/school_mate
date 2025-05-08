package codereview.school_mate.service.serviceImpl;

import codereview.school_mate.dto.ReportRequestDto;
import codereview.school_mate.dto.ReportResponseDto;
import codereview.school_mate.mapper.ReportMapper;
import codereview.school_mate.model.Report;
import codereview.school_mate.model.Subject;
import codereview.school_mate.model.Teacher;
import codereview.school_mate.model.Student;
import codereview.school_mate.repository.ReportRepository;
import codereview.school_mate.repository.SubjectRepository;
import codereview.school_mate.repository.TeacherRepository;
import codereview.school_mate.repository.StudentRepository;
import codereview.school_mate.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ReportMapper reportMapper;

    @Override
    @Transactional
    public ReportResponseDto createReport(ReportRequestDto dto) {
        Subject subject = subjectRepository.findById(dto.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getSubjectId()));
        Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + dto.getTeacherId()));
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));

        Report report = reportMapper.toEntity(dto);
        report.setSubject(subject);
        report.setTeacher(teacher);
        report.setStudent(student);

        // Сохранение и возврат DTO
        Report savedReport = reportRepository.save(report);
        return reportMapper.toDto(savedReport);
    }

    @Override
    public ReportResponseDto findReportById(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));
        return reportMapper.toDto(report);
    }

    @Override
    public List<ReportResponseDto> getAllReports() {
        return reportMapper.toDtos(reportRepository.findAll());
    }

    @Override
    @Transactional
    public ReportResponseDto updateReport(Long id, ReportRequestDto dto) {
        Report existingReport = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Report not found with id: " + id));

        if (dto.getSubjectId() != null) {
            Subject subject = subjectRepository.findById(dto.getSubjectId())
                    .orElseThrow(() -> new RuntimeException("Subject not found with id: " + dto.getSubjectId()));
            existingReport.setSubject(subject);
        }
        if (dto.getTeacherId() != null) {
            Teacher teacher = teacherRepository.findById(dto.getTeacherId())
                    .orElseThrow(() -> new RuntimeException("Teacher not found with id: " + dto.getTeacherId()));
            existingReport.setTeacher(teacher);
        }
        if (dto.getStudentId() != null) {
            Student student = studentRepository.findById(dto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));
            existingReport.setStudent(student);
        }

        reportMapper.updateEntityFromDto(dto, existingReport);

        Report updatedReport = reportRepository.save(existingReport);
        return reportMapper.toDto(updatedReport);
    }

    @Override
    @Transactional
    public void deleteReport(Long id) {
        if (!reportRepository.existsById(id)) {
            throw new RuntimeException("Report not found with id: " + id);
        }
        reportRepository.deleteById(id);
    }
}