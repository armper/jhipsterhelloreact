package com.service.hello.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.service.hello.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ExcerciseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Excercise.class);
        Excercise excercise1 = new Excercise();
        excercise1.setId(1L);
        Excercise excercise2 = new Excercise();
        excercise2.setId(excercise1.getId());
        assertThat(excercise1).isEqualTo(excercise2);
        excercise2.setId(2L);
        assertThat(excercise1).isNotEqualTo(excercise2);
        excercise1.setId(null);
        assertThat(excercise1).isNotEqualTo(excercise2);
    }
}
