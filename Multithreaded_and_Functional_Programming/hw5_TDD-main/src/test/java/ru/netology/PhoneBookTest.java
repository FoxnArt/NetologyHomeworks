package ru.netology;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


import java.util.stream.Stream;

import static ru.netology.PhoneBook.storage;

public class PhoneBookTest {
    PhoneBook sut;

    @BeforeEach
    public void beforeEach() {
        sut = PhoneBook.instance.get();
    }

    @AfterEach
    public void afterEach() {
        sut = null;
    }

    @ParameterizedTest
    @MethodSource("addSource")
    public void testAdd(String name, String number, int expected) {
        // when:
        var result = sut.add(name, number);

        // then:
        Assertions.assertEquals(expected, result);
    }
    public static Stream<Arguments> addSource() {
        // given
        return Stream.of(
                Arguments.of("Petrov", "+7(965)115-23-23", 1),
                Arguments.of("Ivanov", "+7(905)464-37-32", 2),
                Arguments.of("Petrov", "+7(965)115-23-23", 2)
        );
    }


    @ParameterizedTest
    @MethodSource("findByNumberSource")
    public void testfindByNumber(String number, String expected) {
        // given
        storage.put("Petrov", "+7(965)115-23-23");
        storage.put("Ivanov", "+7(905)464-37-32");

        // when:

        var result = sut.findByNumber(number);

        // then:
        Assertions.assertEquals(expected, result);
        storage.clear();
    }
    public static Stream<Arguments> findByNumberSource() {
        // given
        return Stream.of(
                Arguments.of("+7(965)115-23-23", "Petrov"),
                Arguments.of("+7(905)464-37-32", "Ivanov")
        );
    }

    @ParameterizedTest
    @MethodSource("findByNameSource")
    public void testfindByName(String name, String expected) {
        // given
        storage.put("Petrov", "+7(965)115-23-23");
        storage.put("Ivanov", "+7(905)464-37-32");

        // when:

        var result = sut.findByName(name);

        // then:
        Assertions.assertEquals(expected, result);
        storage.clear();
    }
    public static Stream<Arguments> findByNameSource() {
        // given
        return Stream.of(
                Arguments.of("Petrov", "+7(965)115-23-23"),
                Arguments.of("Ivanov", "+7(905)464-37-32")
        );
    }

}
