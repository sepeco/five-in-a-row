package net.seancoyne.fiveinarowserver.validate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class RequestDetailsValidator {

    public boolean requiredParametersIsInvalid(Object... parameters) {

        for (Object param : parameters) {

            if (param == null) {
                return true;
            }
            if (param instanceof String && StringUtils.isBlank((String) param)) {
                return true;
            }
        }
        return false;
    }
}
