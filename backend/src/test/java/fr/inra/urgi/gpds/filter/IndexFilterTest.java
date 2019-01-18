package fr.inra.urgi.gpds.filter;

import fr.inra.urgi.gpds.api.gnpis.v1.DataDiscoveryController;
import fr.inra.urgi.gpds.config.SecurityConfig;
import fr.inra.urgi.gpds.repository.es.DataDiscoveryRepository;
import fr.inra.urgi.gpds.repository.file.DataSourceRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

/**
 * Unit tests for {@link IndexFilter}
 *
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = DataDiscoveryController.class)
@Import(SecurityConfig.class)
class IndexFilterTest {

    @MockBean
    private DataDiscoveryRepository repository;

    @MockBean
    private DataSourceRepository dataSourceRepository;

    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @ValueSource(strings = {
        // Static files
        "/index.html",
        "/script.js",
        "/style.css",
        "/image.gif",
        "/icon.ico",
        "/image.png",
        "/image.jpg",
        "/font.woff",
        "/font.ttf",
        // APIs
        "/brapi/v1/studies",
        "/gnpis/v1/datadiscovery/suggest",
        "/actuator/info",
    })
    void shouldNotForward(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(forwardedUrl(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "/home",
        "/studies/foo",
        "/germplasm/bar",
    })
    void shouldForward(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(forwardedUrl("/index.html"));
    }

}
