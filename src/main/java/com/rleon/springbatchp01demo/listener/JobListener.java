package com.rleon.springbatchp01demo.listener;

import com.rleon.springbatchp01demo.model.entity.People;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class JobListener extends JobExecutionListenerSupport {

    private JdbcTemplate jdbcTemplate;

    public JobListener(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("FINALIZO EL JOB!! verifica los resultados:");
            jdbcTemplate.query("Select first_name, second_name, phone from people",
                    (rs, row) -> new People(rs.getString(1), rs.getString(2), rs.getString(3)))
                    .forEach(persona -> log.info("Registro <" + persona + ">"));
        }
    }
}
