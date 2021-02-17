package com.rleon.springbatchp01demo.config;

import com.rleon.springbatchp01demo.listener.JobListener;
import com.rleon.springbatchp01demo.model.entity.People;
import com.rleon.springbatchp01demo.processor.PeopleItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;


    @Bean
    public FlatFileItemReader<People> reader() {
        return new FlatFileItemReaderBuilder<People>()
                .name("personaItemReader")
                .resource(new ClassPathResource("sample-data.csv"))
                .delimited()
                .names("firstName", "secondName", "phone")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<People>() {{
                    setTargetType(People.class);
                }}).build();
    }

    @Bean
    public PeopleItemProcessor processor() {
        return new PeopleItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<People> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<People>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("INSERT INTO people (first_name, second_name, phone) values (:firstName, :secondName, :phone)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Job importPeopleJob(JobListener listener, Step step1) {
        return jobBuilderFactory.get("importPeopleJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<People> writer) {
        return stepBuilderFactory.get("step1")
                .<People, People>chunk(10)
                .reader(reader())
                .processor(processor())
                .writer(writer)
                .build();

    }

}
