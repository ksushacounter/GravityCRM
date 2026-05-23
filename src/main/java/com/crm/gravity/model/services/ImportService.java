package com.crm.gravity.model.services;

import com.crm.gravity.model.entities.Student;
import com.crm.gravity.model.repositories.GroupRepository;
import com.crm.gravity.model.repositories.StudentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service
public class ImportService {

    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;

    public ImportService(StudentRepository studentRepository, GroupRepository groupRepository) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public int importStudentsFromCsv(MultipartFile file, Long groupId) {
        int importedCount = 0;

        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                if (csvRecord.size() == 0 || csvRecord.get(0).trim().isEmpty()) {
                    continue;
                }

                String firstCell = csvRecord.get(0).trim();

                if (firstCell.equalsIgnoreCase("Фамилия") || firstCell.equalsIgnoreCase("ФИО") || firstCell.equalsIgnoreCase("Имя") || firstCell.equalsIgnoreCase("Телефон")) {
                    continue;
                }

                String fio = "";
                String phone = null;

                if (csvRecord.size() >= 3) {
                    String lastName = firstCell;
                    String firstName = csvRecord.get(1).trim();
                    phone = csvRecord.get(2).trim();
                    fio = (lastName + " " + firstName).trim();
                } else if (csvRecord.size() == 2) {
                    fio = firstCell;
                    phone = csvRecord.get(1).trim();
                } else {
                    fio = firstCell;
                }

                if (fio.isEmpty()) {
                    continue;
                }

                if (phone != null) {
                    phone = phone.trim();
                    if (phone.isEmpty() || phone.equals(",")) {
                        phone = null;
                    }
                }

                Student student = new Student(null, fio, phone, "ACTIVE");
                Long studentId = studentRepository.createStudent(student);

                groupRepository.enrollStudent(groupId, studentId);

                importedCount++;
            }

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при парсинге CSV файла: " + e.getMessage(), e);
        }

        return importedCount;
    }
}