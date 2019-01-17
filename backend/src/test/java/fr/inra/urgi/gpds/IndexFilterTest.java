package fr.inra.urgi.gpds;

import fr.inra.urgi.gpds.api.gnpis.v1.DataDiscoveryController;
import fr.inra.urgi.gpds.filter.IndexFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

/**
 * Unit tests for {@link IndexFilter}
 *
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DataDiscoveryController.class)
//@Import(SecurityConfig.class)
class IndexFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldForwardToIndexForAngularUrl() throws Exception {
        mockMvc.perform(get("/search?query=vitis"))
               .andExpect(forwardedUrl("/index.html"));
    }

    @Test
    void shouldNotForwardToIndexWhenNotGet() throws Exception {
        mockMvc.perform(post("/search?query=vitis"))
               .andExpect(forwardedUrl(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/api/search",
        "/index.html",
        "/script.js",
        "/style.css",
        "/image.gif",
        "/icon.ico",
        "/image.png",
        "/image.jpg",
        "/font.woff",
        "/font.ttf",
        "/actuator/info"
    })
    void shouldNotForwardToIndexWhenStaticResource(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(forwardedUrl(null));
    }

    @Test
    void shouldCorrectlyHandleContextPath() throws Exception {
        mockMvc.perform(get("/rare/api/search").contextPath("/rare"))
               .andExpect(forwardedUrl(null));
        mockMvc.perform(get("/rare/search?query=vitis").contextPath("/rare"))
               .andExpect(forwardedUrl("/index.html"));
    }
}
