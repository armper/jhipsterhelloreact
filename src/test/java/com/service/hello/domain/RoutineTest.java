package com.service.hello.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.service.hello.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoutineTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Routine.class);
        Routine routine1 = new Routine();
        routine1.setId(1L);
        Routine routine2 = new Routine();
        routine2.setId(routine1.getId());
        assertThat(routine1).isEqualTo(routine2);
        routine2.setId(2L);
        assertThat(routine1).isNotEqualTo(routine2);
        routine1.setId(null);
        assertThat(routine1).isNotEqualTo(routine2);
    }
}
