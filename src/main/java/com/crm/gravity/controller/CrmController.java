package com.crm.gravity.controller;

import com.crm.gravity.dto.FreezeRequest;
import com.crm.gravity.model.entities.*;
import com.crm.gravity.model.repositories.*;
import com.crm.gravity.model.services.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/crm")
public class CrmController {
    private final ChoreographerRepository choreographerRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository; // Добавили репозиторий студентов
    private final SubscriptionService subscriptionService;
    private final AttendanceService attendanceService;
    private final ImportService importService;
    private final HallRepository hallRepository;
    private final ScheduleRepository scheduleRepository;
    private final FileService fileService;
    private final SubscriptionFreezeService freezeService;


    public CrmController(ChoreographerRepository choreographerRepository,
                         GroupRepository groupRepository,
                         StudentRepository studentRepository,
                         SubscriptionService subscriptionService,
                         AttendanceService attendanceService, ImportService importService, HallRepository hallRepository, ScheduleRepository scheduleRepository, FileService fileService, SubscriptionFreezeService freezeService) {
        this.choreographerRepository = choreographerRepository;
        this.groupRepository = groupRepository;
        this.studentRepository = studentRepository;
        this.subscriptionService = subscriptionService;
        this.attendanceService = attendanceService;
        this.importService = importService;
        this.hallRepository = hallRepository;
        this.scheduleRepository = scheduleRepository;
        this.fileService = fileService;
        this.freezeService = freezeService;
    }

    @PostMapping("/choreographers")
    public ResponseEntity<String> addChoreographer(@RequestBody Choreographer choreographer) {
        Long id = choreographerRepository.createChoreographer(choreographer);
        return ResponseEntity.ok("Хореограф успешно добавлен в систему с ID: " + id);
    }

    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        Long id = studentRepository.createStudent(student);
        return ResponseEntity.ok("Студент успешно зарегистрирован с ID: " + id);
    }

    @PostMapping("/groups")
    public ResponseEntity<String> addGroup(@RequestBody DanceGroup group) {
        Long id = groupRepository.createGroup(group);
        return ResponseEntity.ok("Группа успешно создана с ID: " + id);
    }

    @PostMapping("/groups/{groupId}/enroll/{studentId}")
    public ResponseEntity<String> enroll(@PathVariable Long groupId, @PathVariable Long studentId) {
        groupRepository.enrollStudent(groupId, studentId);
        return ResponseEntity.ok("Студент привязан к группе");
    }

    @PostMapping("/attendance/mark")
    public ResponseEntity<String> markVisit(@RequestParam Long subId, @RequestParam Long scheduleId) {
        try {
            attendanceService.recordAttendance(subId, scheduleId);
            return ResponseEntity.ok("Посещение зафиксировано по абонементу №" + subId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/hall/new")
    public ResponseEntity<String> newHall(@RequestBody Halls hall) {
        Long hallId = hallRepository.newHall(hall);
        return ResponseEntity.ok("Зал успешно создан с ID: " + hallId);
    }

    @PostMapping("/schedule/new-class")
    public ResponseEntity<String> createSchedule(@RequestBody Schedule schedule) {
        try {
            Long id = scheduleRepository.newClass(schedule);
            return ResponseEntity.ok("Занятие успешно добавлено в расписание с ID: " + id);
        } catch (Exception e) {
            String errorMsg = e.getMessage();

            if (errorMsg != null && errorMsg.contains("Конфликт расписания")) {
                if (errorMsg.contains("Этот зал уже занят")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Зал в это время занят!");
                }
                if (errorMsg.contains("Хореограф уже ведет")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("У хореографа уже есть занятие в это время!");
                }

                return ResponseEntity.status(HttpStatus.CONFLICT).body("Конфликт расписания!");
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось добавить занятие: " + errorMsg);
        }
    }

    // POST http://localhost:8080/api/v2/crm/students/{studentId}/buy-sub?typeId=1
    @PostMapping("/students/{studentId}/buy-sub")
    public ResponseEntity<String> buySub(@PathVariable Long studentId, @RequestParam Long typeId) {
        try {
            Long subId = subscriptionService.issueSubscription(studentId, typeId);
            return ResponseEntity.ok("Абонемент успешно оформлен! ID абонемента: " + subId + ". Платеж зафиксирован.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при оформлении абонемента: " + e.getMessage());
        }
    }

    @PostMapping(value = "/subscription/{id}/freeze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> freeze(
            @PathVariable Long id,
            @RequestPart("data") FreezeRequest request,
            @RequestPart("file") MultipartFile file) {
        try {
            String documentPath = fileService.saveDocument(file);

            freezeService.freezeSubscription(id, request, documentPath);

            return ResponseEntity.ok("Абонемент успешно заморожен и продлен.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при заморозке: " + e.getMessage());
        }
    }

    @PostMapping(value = "/groups/{groupId}/import-csv", consumes = "multipart/form-data")
    public ResponseEntity<String> importStudents(@PathVariable Long groupId,
                                                 @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл пустой!");
        }

        try {
            int count = importService.importStudentsFromCsv(file, groupId);
            return ResponseEntity.ok("Импорт успешно завершен! Добавлено и привязано студентов: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при импорте: " + e.getMessage());
        }
    }
}