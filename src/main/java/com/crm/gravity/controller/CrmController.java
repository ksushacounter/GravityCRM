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

import java.util.List;

@RestController
@RequestMapping("/crm")
public class CrmController {
    private final ChoreographerRepository choreographerRepository;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final SubscriptionService subscriptionService;
    private final AttendanceService attendanceService;
    private final ImportService importService;
    private final HallRepository hallRepository;
    private final ScheduleRepository scheduleRepository;
    private final FileService fileService;
    private final SubscriptionFreezeService freezeService;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionFreezeRepository freezeRepository;
    private final PaymentRepository paymentRepository;
    private final AttendanceRepository attendanceRepository;

    public CrmController(ChoreographerRepository choreographerRepository,
                         GroupRepository groupRepository,
                         StudentRepository studentRepository,
                         SubscriptionService subscriptionService,
                         AttendanceService attendanceService,
                         ImportService importService,
                         HallRepository hallRepository,
                         ScheduleRepository scheduleRepository,
                         FileService fileService,
                         SubscriptionFreezeService freezeService,
                         SubscriptionRepository subscriptionRepository,
                         SubscriptionFreezeRepository freezeRepository,
                         PaymentRepository paymentRepository,
                         AttendanceRepository attendanceRepository) {
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
        this.subscriptionRepository = subscriptionRepository;
        this.freezeRepository = freezeRepository;
        this.paymentRepository = paymentRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @GetMapping("/get/students")
    public List<Student> students() {
        return studentRepository.findAll();
    }

    @PostMapping("/students")
    public ResponseEntity<String> addStudent(@RequestBody Student student) {
        Long id = studentRepository.createStudent(student);
        return ResponseEntity.ok("Студент успешно зарегистрирован с ID: " + id);
    }

    @DeleteMapping("/students/{studentId}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long studentId) {
        if (studentRepository.hasActiveSubscription(studentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Нельзя удалить ученика: у него есть действующий или замороженный абонемент");
        }

        int deleted = studentRepository.deleteById(studentId);
        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ученик не найден");
        }
        return ResponseEntity.ok("Ученик удален. Архивные абонементы, платежи и посещения сохранены.");
    }

    @GetMapping("/get/choreographers")
    public List<Choreographer> choreographers() {
        return choreographerRepository.findAll();
    }

    @PostMapping("/choreographers")
    public ResponseEntity<String> addChoreographer(@RequestBody Choreographer choreographer) {
        Long id = choreographerRepository.createChoreographer(choreographer);
        return ResponseEntity.ok("Хореограф успешно добавлен в систему с ID: " + id);
    }

    @GetMapping("/get/groups")
    public List<DanceGroup> groups() {
        return groupRepository.findAll();
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

    @GetMapping("/get/halls")
    public List<Halls> halls() {
        return hallRepository.findAll();
    }

    @PostMapping("/hall/new")
    public ResponseEntity<String> newHall(@RequestBody Halls hall) {
        Long hallId = hallRepository.newHall(hall);
        return ResponseEntity.ok("Зал успешно создан с ID: " + hallId);
    }

    @GetMapping("/get/schedule")
    public List<Schedule> schedule() {
        return scheduleRepository.findAll();
    }

    @PostMapping("/schedule/new-class")
    public ResponseEntity<String> createSchedule(@RequestBody Schedule schedule) {
        try {
            Long id = scheduleRepository.newClass(schedule);
            return ResponseEntity.ok("Занятие успешно добавлено в расписание с ID: " + id);
        } catch (Exception e) {
            String errorMsg = e.getMessage();

            if (errorMsg != null && errorMsg.contains("Конфликт расписания")) {
                if (errorMsg.contains("зал")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Зал в это время занят!");
                }
                if (errorMsg.contains("Хореограф")) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("У хореографа уже есть занятие в это время!");
                }
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Конфликт расписания!");
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Не удалось добавить занятие: " + errorMsg);
        }
    }

    @DeleteMapping("/schedule/{scheduleId}")
    public ResponseEntity<String> deleteSchedule(@PathVariable Long scheduleId) {
        int deleted = scheduleRepository.deleteById(scheduleId);
        if (deleted == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Занятие не найдено");
        }
        return ResponseEntity.ok("Занятие удалено из расписания. Старые отметки посещаемости сохранены.");
    }

    @GetMapping("/get/subscription-types")
    public List<SubTypes> subscriptionTypes() {
        return subscriptionRepository.findAllTypes();
    }

    @GetMapping("/subscriptions")
    public List<Subscriptions> subscriptions() {
        return subscriptionRepository.findAll();
    }

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

    @GetMapping("/get/subscription-freezes")
    public List<SubscriptionFreeze> freezes() {
        return freezeRepository.findAll();
    }

    @PostMapping(value = "/subscription/{id}/freeze", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> freeze(
            @PathVariable Long id,
            @RequestPart("data") FreezeRequest request,
            @RequestPart("file") MultipartFile file) {
        try {
            String documentPath = fileService.saveDocument(file);
            freezeService.freezeSubscription(id, request, documentPath);
            return ResponseEntity.ok("Абонемент успешно заморожен. Срок действия продлен триггером базы данных.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка при заморозке: " + e.getMessage());
        }
    }

    @GetMapping("/get/attendance")
    public List<Attedance> attendance() {
        return attendanceRepository.findAll();
    }

    @PostMapping("/attendance/mark")
    public ResponseEntity<String> markVisit(@RequestParam Long subId, @RequestParam Long scheduleId) {
        try {
            attendanceService.recordAttendance(subId, scheduleId);
            return ResponseEntity.ok("Посещение зафиксировано по абонементу №" + subId);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка отметки посещения: " + e.getMessage());
        }
    }

    @GetMapping("/get/payments")
    public List<Payment> payments() {
        return paymentRepository.findAll();
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
