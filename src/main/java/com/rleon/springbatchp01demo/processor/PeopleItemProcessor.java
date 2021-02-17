package com.rleon.springbatchp01demo.processor;

import com.rleon.springbatchp01demo.model.entity.People;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.item.ItemProcessor;

@Log4j2
public class PeopleItemProcessor implements ItemProcessor<People, People> {


    @Override
    public People process(People peopleItem) throws Exception {
        String firstName = peopleItem.getFirstName().toUpperCase();
        String secondName = peopleItem.getSecondName().toUpperCase();
        String phone = peopleItem.getPhone();

        People people = new People(firstName, secondName, phone);

        log.info("Convert (" + peopleItem + ") to (" + people + ")");


        return people;

    }
}
