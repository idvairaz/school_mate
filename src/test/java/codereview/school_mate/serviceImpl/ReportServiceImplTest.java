package codereview.school_mate.serviceImpl;

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
import codereview.school_mate.service.serviceImpl.ReportServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private SubjectRepository subjectRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ReportMapper reportMapper;

    @InjectMocks
    private ReportServiceImpl reportService;

    private ReportRequestDto requestDto;
    private ReportResponseDto responseDto;
    private Report report;
    private Subject subject;
    private Teacher teacher;
    private Student student;

    @BeforeEach
    void setUp() {
        subject = new Subject();
        subject.setId(1L);

        teacher = new Teacher();
        teacher.setId(1L);

        student = new Student();
        student.setId(1L);

        requestDto = new ReportRequestDto();
        requestDto.setSubjectId(1L);
        requestDto.setTeacherId(1L);
        requestDto.setStudentId(1L);
        requestDto.setMessage("Test report");

        report = new Report();
        report.setId(1L);
        report.setSubject(subject);
        report.setTeacher(teacher);
        report.setStudent(student);
        report.setDate(LocalDate.now());
        report.setMessage("Test report");

        responseDto = new ReportResponseDto();
        responseDto.setId(1L);
        responseDto.setSubject(subject);
        responseDto.setTeacher(teacher);
        responseDto.setStudent(student);
        responseDto.setDate(LocalDate.now());
        responseDto.setMessage("Test report");
    }

    @Test
    @DisplayName("Создание отчёта - успешный сценарий")
    void createReport_Success() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(reportMapper.toEntity(requestDto)).thenReturn(report);
        when(reportRepository.save(any(Report.class))).thenReturn(report);
        when(reportMapper.toDto(report)).thenReturn(responseDto);

        // Act
        ReportResponseDto result = reportService.createReport(requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        assertEquals(responseDto.getMessage(), result.getMessage());
        verify(subjectRepository).findById(1L);
        verify(teacherRepository).findById(1L);
        verify(studentRepository).findById(1L);
        verify(reportMapper).toEntity(requestDto);
        verify(reportRepository).save(report);
        verify(reportMapper).toDto(report);
    }

    @Test
    @DisplayName("Создание отчёта - предмет не найден")
    void createReport_SubjectNotFound() {
        // Arrange
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.createReport(requestDto));
        assertEquals("Subject not found with id: 1", exception.getMessage());
        verify(subjectRepository).findById(1L);
        verifyNoInteractions(teacherRepository, studentRepository, reportMapper, reportRepository);
    }

    @Test
    @DisplayName("Получение отчёта по ID - успешный сценарий")
    void findReportById_Success() {
        // Arrange
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        when(reportMapper.toDto(report)).thenReturn(responseDto);

        // Act
        ReportResponseDto result = reportService.findReportById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        verify(reportRepository).findById(1L);
        verify(reportMapper).toDto(report);
    }

    @Test
    @DisplayName("Получение отчёта по ID - отчёт не найден")
    void findReportById_NotFound() {
        // Arrange
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.findReportById(1L));
        assertEquals("Report not found with id: 1", exception.getMessage());
        verify(reportRepository).findById(1L);
        verifyNoInteractions(reportMapper);
    }

    @Test
    @DisplayName("Получение всех отчётов - успешный сценарий")
    void getAllReports_Success() {
        // Arrange
        List<Report> reports = List.of(report);
        List<ReportResponseDto> responseDtos = List.of(responseDto);
        when(reportRepository.findAll()).thenReturn(reports);
        when(reportMapper.toDtos(reports)).thenReturn(responseDtos);

        // Act
        List<ReportResponseDto> result = reportService.getAllReports();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDto.getId(), result.get(0).getId());
        verify(reportRepository).findAll();
        verify(reportMapper).toDtos(reports);
    }

    @Test
    @DisplayName("Обновление отчёта - успешный сценарий")
    void updateReport_Success() {
        // Arrange
        when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        doNothing().when(reportMapper).updateEntityFromDto(requestDto, report);
        when(reportRepository.save(report)).thenReturn(report);
        when(reportMapper.toDto(report)).thenReturn(responseDto);

        // Act
        ReportResponseDto result = reportService.updateReport(1L, requestDto);

        // Assert
        assertNotNull(result);
        assertEquals(responseDto.getId(), result.getId());
        verify(reportRepository).findById(1L);
        verify(subjectRepository).findById(1L);
        verify(teacherRepository).findById(1L);
        verify(studentRepository).findById(1L);
        verify(reportMapper).updateEntityFromDto(requestDto, report);
        verify(reportRepository).save(report);
        verify(reportMapper).toDto(report);
    }

    @Test
    @DisplayName("Обновление отчёта - отчёт не найден")
    void updateReport_NotFound() {
        // Arrange
        when(reportRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.updateReport(1L, requestDto));
        assertEquals("Report not found with id: 1", exception.getMessage());
        verify(reportRepository).findById(1L);
        verifyNoInteractions(subjectRepository, teacherRepository, studentRepository, reportMapper);
    }

    @Test
    @DisplayName("Удаление отчёта - успешный сценарий")
    void deleteReport_Success() {
        // Arrange
        when(reportRepository.existsById(1L)).thenReturn(true);
        doNothing().when(reportRepository).deleteById(1L);

        // Act
        reportService.deleteReport(1L);

        // Assert
        verify(reportRepository).existsById(1L);
        verify(reportRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Удаление отчёта - отчёт не найден")
    void deleteReport_NotFound() {
        // Arrange
        when(reportRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> reportService.deleteReport(1L));
        assertEquals("Report not found with id: 1", exception.getMessage());
        verify(reportRepository).existsById(1L);
        verifyNoMoreInteractions(reportRepository);
    }
}
