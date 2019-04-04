package Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {
	private static final long serialVersionUID = -7068430810410300028L;
	public ForbiddenException() {
		super();
	}
	public ForbiddenException(String msg) {
		super(msg);
	}
}
