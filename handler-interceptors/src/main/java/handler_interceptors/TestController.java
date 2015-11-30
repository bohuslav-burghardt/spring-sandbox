package handler_interceptors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

	@RequestMapping("/optional")
	@DisallowUndeclaredRequestParams
	public ResponseEntity optional(@RequestParam(required = false) String p) {
		return ResponseEntity.ok(p);
	}

	@RequestMapping("/required")
	public ResponseEntity required(@RequestParam(value = "p", required = true) String p) {
		return ResponseEntity.ok(p);
	}

	@ExceptionHandler(UndeclaredRequestParamException.class)
	public ResponseEntity<String> handle(UndeclaredRequestParamException e) {
		return ResponseEntity.badRequest().body(e.getMessage());
	}

}
