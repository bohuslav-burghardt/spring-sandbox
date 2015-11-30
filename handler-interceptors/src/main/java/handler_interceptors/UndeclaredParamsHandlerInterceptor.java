package handler_interceptors;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Handler interceptor used for ensuring that no request params other than those explicitly
 * declared via {@link RequestParam} parameters of the handler method are passed in when the
 * handler is annotated with {@link DisallowUndeclaredRequestParams}.
 *
 * @see DisallowUndeclaredRequestParams
 *
 * @author Bohuslav Burghardt
 */
public class UndeclaredParamsHandlerInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
							 Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getMethodAnnotation(DisallowUndeclaredRequestParams.class) != null) {
				checkParams(request, getDeclaredRequestParams(handlerMethod));
			}
		}
		return true;
	}

	/**
	 * Check that all of the request params of the specified servlet request are
	 * contained within the specified set of allowed parameters.
	 * @param request HttpServletRequest whose params to check. Never null.
	 * @param allowedParams Set of allowed request parameters. Never null.
	 * @throws UndeclaredRequestParamException If one of the requests parameters is
	 * not present in the specified set of allowed parameters
	 */
	private void checkParams(HttpServletRequest request, Set<String> allowedParams) {
		request.getParameterMap().entrySet().forEach(entry -> {
			String param = entry.getKey();
			if (!allowedParams.contains(param)) {
				throw new UndeclaredRequestParamException(param, allowedParams);
			}
		});
	}

	/**
	 * Extract all request parameters declared via {@link RequestParam} for the
	 * specified handler method.
	 * @param handlerMethod Handler method to extract declared params for. Never null.
	 * @return Set of declared request parameters. Never null.
	 */
	private Set<String> getDeclaredRequestParams(HandlerMethod handlerMethod) {
		Set<String> declaredRequestParams = new HashSet<>();
		MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
		ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

		for (MethodParameter methodParameter : methodParameters) {
			if (methodParameter.hasParameterAnnotation(RequestParam.class)) {
				RequestParam requestParam = methodParameter.getParameterAnnotation(RequestParam.class);
				if (StringUtils.hasText(requestParam.value())) {
					declaredRequestParams.add(requestParam.value());
				} else {
					methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
					declaredRequestParams.add(methodParameter.getParameterName());
				}
			}
		}
		return declaredRequestParams;
	}

}

