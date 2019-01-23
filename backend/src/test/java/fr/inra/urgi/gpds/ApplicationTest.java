package fr.inra.urgi.gpds;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Unit test for {@link Application}
 *
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestPropertySource("/test.properties")
class ApplicationTest {

    @Test
    void shouldLoadContext() {
    }

}
