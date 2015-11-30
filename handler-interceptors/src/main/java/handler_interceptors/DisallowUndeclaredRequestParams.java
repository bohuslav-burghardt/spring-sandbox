package handler_interceptors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation which should be placed on a controller handler method in case that
 * request parameters which are not explicitly declared in handler method's
 * {@link org.springframework.web.bind.annotation.RequestParam} parameters should
 * trigger an error.
 *
 * @see UndeclaredParamsHandlerInterceptor
 *
 * @author Bohuslav Burghardt
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DisallowUndeclaredRequestParams {
}
