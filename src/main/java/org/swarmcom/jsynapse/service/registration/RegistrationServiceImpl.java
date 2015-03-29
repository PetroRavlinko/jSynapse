package org.swarmcom.jsynapse.service.registration;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.swarmcom.jsynapse.domain.Registration.*;
import org.swarmcom.jsynapse.service.exception.InvalidRequestException;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Validated
public class RegistrationServiceImpl implements RegistrationService {

    final ApplicationContext applicationContext;
    private Map<String, RegistrationProvider> providers;

    @Inject
    public RegistrationServiceImpl(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public RegistrationFlows getSupportedFlows() {
        List<RegistrationInfo> flows = new ArrayList<>();
        for (RegistrationProvider provider : getProviders().values()) {
            flows.add(provider.getFlow());
        }
        return new RegistrationFlows(flows);
    }

    @Override
    public RegistrationResult register(RegistrationSubmission registration) {
        RegistrationProvider provider = getProvider(registration.getType());
        checkSubmission(provider, registration);
        return provider.register(registration);
    }

    public RegistrationProvider getProvider(String type) {
        RegistrationProvider provider = getProviders().get(type);
        if (null == provider) {
            throw new InvalidRequestException("Bad login type");
        }
        return provider;
    }

    public void checkSubmission(RegistrationProvider provider, RegistrationSubmission registration) {
        if (!provider.getFlow().validateKeys(registration)) {
            throw new InvalidRequestException("Missing registration keys");
        }
    }

    private Map<String, RegistrationProvider> getProviders() {
        if (null == providers) {
            providers = applicationContext.getBeansOfType(RegistrationProvider.class);
        }
        return providers;
    }
}
