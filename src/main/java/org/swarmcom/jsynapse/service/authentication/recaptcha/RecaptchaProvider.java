package org.swarmcom.jsynapse.service.authentication.recaptcha;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.swarmcom.jsynapse.dao.UserRepository;
import org.swarmcom.jsynapse.domain.Authentication.AuthenticationInfo;
import org.swarmcom.jsynapse.domain.Authentication.AuthenticationResult;
import org.swarmcom.jsynapse.domain.Authentication.AuthenticationSubmission;
import org.swarmcom.jsynapse.domain.User;
import org.swarmcom.jsynapse.service.exception.InvalidRequestException;
import org.swarmcom.jsynapse.service.authentication.AuthenticationProvider;

import javax.inject.Inject;

import static org.swarmcom.jsynapse.service.authentication.recaptcha.RecaptchaInfo.*;
import static java.lang.String.format;

@Component(RECAPTCHA_TYPE)
public class RecaptchaProvider implements AuthenticationProvider {
    final static AuthenticationInfo flow = new RecaptchaInfo();
    private static final Logger LOGGER = LoggerFactory.getLogger(RecaptchaProvider.class);

    private final UserRepository repository;

    private @Value("${recaptcha.private.key:null}") String recapthaPrivateKey;

    @Inject
    public RecaptchaProvider(final UserRepository repository) {
        // TODO create and inject UserService - access to user repository through it
        this.repository = repository;
    }

    @Override
    public AuthenticationInfo getFlow() {
        return flow;
    }

    @Override
    public AuthenticationResult register(AuthenticationSubmission registration) {
        validateRecaptcha(registration);
        // TODO create and inject Password encoder, use it to hash password
        // TODO verify if user name already exists, compose it with domain
        // TODO throw register error if not a valid request
        User user = new User("user", "password");
        repository.save(user);
        return new AuthenticationResult("userid");
    }

    @Override
    public AuthenticationResult login(AuthenticationSubmission registration) {
        return null;
    }

    public void validateRecaptcha(AuthenticationSubmission registration) {
        String remoteAddr = registration.getRemoteAddr();
        String challenge = registration.get(CHALLENGE);
        String response = registration.get(RESPONSE);
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setPrivateKey(recapthaPrivateKey);
        ReCaptchaResponse reCaptchaResponse =
                reCaptcha.checkAnswer(remoteAddr, challenge, response);
        if (!reCaptchaResponse.isValid()) {
            LOGGER.error(format("Failed recaptcha for remote addr %s with errror %s", registration.getRemoteAddr(),
                    reCaptchaResponse.getErrorMessage()));
            throw new InvalidRequestException("Bad recaptcha");
        }
    }
}