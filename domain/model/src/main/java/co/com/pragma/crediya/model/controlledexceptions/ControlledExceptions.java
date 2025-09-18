package co.com.pragma.crediya.model.controlledexceptions;

import co.com.pragma.crediya.model.controlledexceptions.message.ErrorMessage;
import lombok.Getter;

@Getter
public class ControlledExceptions extends RuntimeException {
    private final ErrorMessage errorMessage;

    public ControlledExceptions(ErrorMessage errorMessage){
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
