package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.util.Profiles;

@ActiveProfiles(profiles = {Profiles.DATAJPA})
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

}