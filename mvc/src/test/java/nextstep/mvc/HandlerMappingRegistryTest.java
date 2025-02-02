package nextstep.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class HandlerMappingRegistryTest {

    @Test
    @DisplayName("핸들러 매핑을 찾는다.")
    void getHandlerMapping() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        assertThat(handlerMappingRegistry.getHandlerMapping(request)).isInstanceOf(HandlerExecution.class);
    }

    @Test
    @DisplayName("해당하는 uri가 없어서 핸들러 매핑을 찾지 못하면 예외가 발생한다.")
    void getHandlerMappingUriException() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/invalid");
        when(request.getMethod()).thenReturn("GET");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        assertThatThrownBy(() -> handlerMappingRegistry.getHandlerMapping(request))
                .isExactlyInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("http method를 지원하지 않아서 핸들러 매핑을 찾지 못하면 예외가 발생한다.")
    void getHandlerMappingMethodException() {
        final HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("id")).thenReturn("gugu");
        when(request.getRequestURI()).thenReturn("/get-test");
        when(request.getMethod()).thenReturn("POST");

        final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();
        handlerMappingRegistry.add(new AnnotationHandlerMapping("samples"));
        handlerMappingRegistry.init();

        assertThatThrownBy(() -> handlerMappingRegistry.getHandlerMapping(request))
                .isExactlyInstanceOf(RuntimeException.class);
    }
}
