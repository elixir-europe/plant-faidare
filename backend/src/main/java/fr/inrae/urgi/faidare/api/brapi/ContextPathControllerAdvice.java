package fr.inrae.urgi.faidare.api.brapi;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class ContextPathControllerAdvice {

    @ModelAttribute("contextPath")
    public String contextPath(final HttpServletRequest request) {
        return request.getContextPath();
    }

}
