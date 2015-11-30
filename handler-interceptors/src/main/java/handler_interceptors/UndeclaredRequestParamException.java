package handler_interceptors;

import java.util.Set;

/**
 * Thrown when request param which is not explicitly defined as
 * {@link org.springframework.web.bind.annotation.RequestParam}
 * in the handler method is encountered.
 *
 * @author Bohuslav Burghardt
 */
public class UndeclaredRequestParamException extends IllegalArgumentException {

	public UndeclaredRequestParamException(String param, Set<String> allowedParams) {
		super("Parameter '" + param + "' is not supported. Supported parameters " +
			"are " + allowedParams + ".");
	}

}

