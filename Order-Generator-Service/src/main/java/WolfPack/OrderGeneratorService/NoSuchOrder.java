package WolfPack.OrderGeneratorService;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No Such Order Exists")
public class NoSuchOrder extends IOException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
