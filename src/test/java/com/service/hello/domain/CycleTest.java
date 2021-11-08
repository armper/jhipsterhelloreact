package com.service.hello.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.service.hello.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CycleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cycle.class);
        Cycle cycle1 = new Cycle();
        cycle1.setId(1L);
        Cycle cycle2 = new Cycle();
        cycle2.setId(cycle1.getId());
        assertThat(cycle1).isEqualTo(cycle2);
        cycle2.setId(2L);
        assertThat(cycle1).isNotEqualTo(cycle2);
        cycle1.setId(null);
        assertThat(cycle1).isNotEqualTo(cycle2);
    }
}
