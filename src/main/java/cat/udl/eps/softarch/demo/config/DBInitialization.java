package cat.udl.eps.softarch.demo.config;

import cat.udl.eps.softarch.demo.domain.User;
import cat.udl.eps.softarch.demo.mothers.*;
import cat.udl.eps.softarch.demo.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Configuration
public class DBInitialization {
    private final UserRepository userRepository;
    private final DonorRepository donorRepository;
    private final TakeRepository takeRepository;
    private final PropagatorRepository propagatorRepository;
    private final DonationRepository donationRepository;
    private final RequestRepository requestRepository;
    @Value("${default-password}")
    String defaultPassword;
    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    public DBInitialization(UserRepository userRepository, DonorRepository donorRepository, TakeRepository takeRepository, PropagatorRepository propagatorRepository, DonationRepository donationRepository, RequestRepository requestRepository) {
        this.userRepository = userRepository;
        this.donorRepository = donorRepository;
        this.takeRepository = takeRepository;
        this.propagatorRepository = propagatorRepository;
        this.donationRepository = donationRepository;
        this.requestRepository = requestRepository;
    }

    @PostConstruct
    public void initializeDatabase() {
        // Default user
        if (!userRepository.existsById("demo")) {
            User user = new User();
            user.setEmail("demo@sample.app");
            user.setUsername("demo");
            user.setPassword(defaultPassword);
            user.encodePassword();
            userRepository.save(user);
        }
        if (isProfileActive("test")) {
            if (!userRepository.existsById("test")) {
                User user = new User();
                user.setEmail("test@sample.app");
                user.setUsername("test");
                user.setPassword(defaultPassword);
                user.encodePassword();
                userRepository.save(user);
            }
        }
        if (isProfileActive("flyio") || isProfileActive("local")) {
            // Default donor
            var donor = DonorMother.getValidDonorWith("donor1");
            if (!donorRepository.existsById("donor1")) donorRepository.save(donor);
            // Default propagator
            var propagator = PropagatorMother.getValidPropagatorWith("propagator1");
            propagatorRepository.save(propagator);
            // Default take
            var take = TakeMother.getValidTake();
            take.setTakePropagator(propagator);
            takeRepository.save(take);
            // Default donation
            var donation = DonationMother.getValidDonationFor(donor, take);
            donationRepository.save(donation);
            // Default request
            var request = RequestMother.getValidRequestFor(propagator, take);
            requestRepository.save(request);
        }
    }

    private Boolean isProfileActive(String activeProfile) {
        return Arrays.asList(activeProfiles.split(",")).contains(activeProfile);
    }
}
