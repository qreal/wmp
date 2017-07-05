package com.qreal.wmp.generator.server;

import com.qreal.wmp.generator.config.AppInit;
import cs.ualberta.launcher.Launcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import java.util.Set;

@ActiveProfiles("testHandler")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppInit.class})
@Transactional
public class GeneratorTest {
    private AcceleoServiceHandler handler;

    @Before
    public void setMocking() {
        if (handler == null) {
            handler = new AcceleoServiceHandler();
        }
    }

    /**
     * Test Acceleo generation.
     */
    @Test
    @Rollback
    public void AcceleoLauncherTest() throws Exception {
        Launcher launcher = new Launcher();

        launcher.runAcceleo("metamodels/Robots.ecore", "Robots",
                "models/model.xmi",
                "transformations/M2T/generateRobots.mtl", "gen/");
        try (BufferedReader br =
                     new BufferedReader(new FileReader("gen/robots.java"))) {
            Set<String> gen = new HashSet<>();
            String s;
            while ((s = br.readLine()) != null) {
                gen.add(s);
            }

            Set<String> expected = new HashSet<>();
            expected.add("InitialNode");
            expected.add("MotorsForward");
            expected.add("MotorsForward");
            expected.add("invalid");

            assertThat(gen).isEqualTo(expected);
        }

    }
}

