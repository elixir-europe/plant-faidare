package fr.inra.urgi.gpds.filter;

import fr.inra.urgi.gpds.Application;
import fr.inra.urgi.gpds.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

/**
 * Unit tests for {@link AngularRouteFilter}
 *
 * @author gcornut
 */
@ExtendWith(SpringExtension.class)
@Import(SecurityConfig.class)
@SpringBootTest(classes = Application.class)
class AngularRouteFilterTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private ResourceLoader resourceLoader;

    private MockMvc mockMvc;

    private AngularRouteFilter filter;

    @BeforeEach
    void setUp() {
        filter = new AngularRouteFilter(resourceLoader);
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .addFilter(filter, "/*")
            .build();
    }

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
        String indexBefore = "<html>\n" +
            "  <base href=\"./\">\n" +
            "</html>";
        String indexAfter = "<html>\n" +
            "  <base href=\"/gnpis-test/gpds/\">\n" +
            "</html>";

        ReflectionTestUtils.setField(filter, "serverContextPath", "/gnpis-test/gpds");

        Resource mockResource = mock(Resource.class);
        when(mockResource.getInputStream())
            .thenReturn(new ByteArrayInputStream(indexBefore.getBytes()));
        when(resourceLoader.getResource(anyString()))
            .thenReturn(mockResource);

        mockMvc.perform(get(url))
            .andExpect(content().string(indexAfter));
    }

}
