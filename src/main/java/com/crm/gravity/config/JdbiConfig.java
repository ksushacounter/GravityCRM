package com.crm.gravity.config;

import com.crm.gravity.model.repositories.*;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class JdbiConfig {

    @Bean
    public Jdbi jdbi(DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        // Обязательно подключаем плагин для работы с интерфейсами (SqlObject)
        jdbi.installPlugin(new SqlObjectPlugin());
        return jdbi;
    }

    // Регистрируем GroupRepository как Spring Bean
    @Bean
    public GroupRepository groupRepository(Jdbi jdbi) {
        return jdbi.onDemand(GroupRepository.class);
    }

    @Bean
    public SubscriptionRepository subscriptionRepository(Jdbi jdbi) {
        return jdbi.onDemand(SubscriptionRepository.class);
    }

    @Bean
    public ChoreographerRepository choreographerRepository(Jdbi jdbi) {
        return jdbi.onDemand(ChoreographerRepository.class);
    }

    @Bean
    public StudentRepository studentRepository(Jdbi jdbi) {
        return jdbi.onDemand(StudentRepository.class);
    }

    @Bean
    public AttendanceRepository attendanceRepository(Jdbi jdbi) {
        return jdbi.onDemand(AttendanceRepository.class);
    }

    @Bean
    public HallRepository hallRepository(Jdbi jdbi) { return jdbi.onDemand(HallRepository.class); }

    @Bean
    public ScheduleRepository sheduleRepository(Jdbi jdbi) { return jdbi.onDemand(ScheduleRepository.class); }

    @Bean
    public SubscriptionFreezeRepository subscriptionFreezeRepository(Jdbi jdbi) { return jdbi.onDemand(SubscriptionFreezeRepository.class); }

}