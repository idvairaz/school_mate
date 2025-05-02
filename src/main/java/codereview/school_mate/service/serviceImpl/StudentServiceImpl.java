package codereview.school_mate.service.serviceImpl;

import codereview.school_mate.dto.StudentRequestDto;
import codereview.school_mate.dto.StudentResponseDto;
import codereview.school_mate.mapper.StudentMapper;
import codereview.school_mate.model.Parent;
import codereview.school_mate.model.SchoolClass;
import codereview.school_mate.model.Student;
import codereview.school_mate.repository.ParentRepository;
import codereview.school_mate.repository.SchoolClassRepository;
import codereview.school_mate.repository.StudentRepository;
import codereview.school_mate.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentResponseDto createStudent(StudentRequestDto dto) {
        Parent parent = parentRepository.findById(dto.getParentId())
                .orElseThrow(() -> new RuntimeException("Parent not found with id: " + dto.getParentId()));

        SchoolClass schoolClass = schoolClassRepository.findById(dto.getSchoolClassId())
                .orElseThrow(() -> new RuntimeException("SchoolClass not found with id: " + dto.getSchoolClassId()));

        Student student = studentMapper.toEntity(dto);
        student.setParent(parent);
        student.setSchoolClass(schoolClass);

        Student savedStudent = studentRepository.save(student);
        return studentMapper.studentToStudentResponseDto(savedStudent);
    }

    @Override
    public StudentResponseDto findByIdStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        return studentMapper.studentToStudentResponseDto(student);
    }

    @Override
    public List<StudentResponseDto> findAllStudent() {
        return studentMapper.studentsToStudentResponseDtos(studentRepository.findAll());
    }

    @Override
    @Transactional
    public StudentResponseDto updateStudent(Long id, StudentRequestDto dto) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (dto.getParentId() != null) {
            Parent parent = parentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new RuntimeException("Parent not found"));
            existingStudent.setParent(parent);
        }

        if (dto.getSchoolClassId() != null) {
            SchoolClass schoolClass = schoolClassRepository.findById(dto.getSchoolClassId())
                    .orElseThrow(() -> new RuntimeException("SchoolClass not found"));
            existingStudent.setSchoolClass(schoolClass);
        }

        studentMapper.updateEntityFromDto(dto, existingStudent);
        Student updatedStudent = studentRepository.save(existingStudent);
        return studentMapper.studentToStudentResponseDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}